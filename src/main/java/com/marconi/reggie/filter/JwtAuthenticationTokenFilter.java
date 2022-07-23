package com.marconi.reggie.filter;

import com.marconi.reggie.common.EmployeeDetail;
import com.marconi.reggie.common.JwtUtil;
import com.marconi.reggie.common.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 接口鉴权过滤器
 * @author 11482
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisUtil redisUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }
//        //如果是登录操作，则放行
//        String loginUri = "/employee/login";
//        String uri = request.getRequestURI();
//        if (Objects.equals(loginUri,uri)){
//            filterChain.doFilter(request,response);
//            return;
//        }
        //解析token
        String userid;
        try {
            Claims claims = JwtUtil.parseJwt(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            if (e.getClass().equals(ExpiredJwtException.class)){
                log.error("JWT已过期，请重新登录");
            }
            log.error("token非法");
            e.printStackTrace();
            filterChain.doFilter(request,response);
            return;
        }
        //从redis中获取用户信息
        String redisKey = "login:" + userid;
        EmployeeDetail employeeDetail = (EmployeeDetail) redisUtil.get(redisKey);
        if(Objects.isNull(employeeDetail)){
            throw new RuntimeException("用户未登录");
        }
        //存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(employeeDetail,null,employeeDetail.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }
}
