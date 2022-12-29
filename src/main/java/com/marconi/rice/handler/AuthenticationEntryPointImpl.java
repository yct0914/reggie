package com.marconi.rice.handler;

import com.alibaba.fastjson.JSON;
import com.marconi.rice.common.Response;
import com.marconi.rice.common.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 没有登录 或 请求时没有在请求头中添加token
 * @author 11482
 * @date 2022/3/18
 */
@Component
@Slf4j
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        String errLogin = "用户名或密码错误";
        if (errLogin.equals(authException.getMessage())){
            Response result = new Response(HttpStatus.UNAUTHORIZED.value(),errLogin);
            String json = JSON.toJSONString(result);
            //处理异常
            log.error("用户登录错误!");
            WebUtils.renderString(response,json);
            return;
        }
        String uri = request.getRequestURI();
        Response result = new Response(HttpStatus.UNAUTHORIZED.value(),"not login");
            String json = JSON.toJSONString(result);
            //处理异常
            log.error("权限不足:{}",uri);
            WebUtils.renderString(response,json);


    }
}
