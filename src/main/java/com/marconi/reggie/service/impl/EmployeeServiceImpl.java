package com.marconi.reggie.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.marconi.reggie.common.*;
import com.marconi.reggie.config.properties.RedisProperties;
import com.marconi.reggie.mapper.EmployeeMapper;
import com.marconi.reggie.pojo.DO.Employee;
import com.marconi.reggie.service.EmployeeService;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * @author 11482
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    private Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    RedisProperties redisProperties;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public boolean signup(Employee employee){
        boolean save = this.save(employee);
        //创建员工角色权限
        Long roleId = employeeMapper.selectRoleIdByRole("ROLE_employee");
        employeeMapper.insertEmployeeRole(employee.getId(),roleId);
        return save;
    }
    @Override
    public Response login(Employee employee) {
        //将用户输入的用户名和密码传入一个实现
        String username = employee.getUsername();
        String password = employee.getPassword();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)){
            throw new CustomException("用户名或密码错误");
        }

        //创建JWT并返回响应
        EmployeeDetail employeeDetail = (EmployeeDetail) authenticate.getPrincipal();
        String  id = employeeDetail.getEmployee().getId().toString();
        String jwt = JwtUtil.createJwt(id);
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("token", jwt);
        Employee employee1 = employeeDetail.getEmployee();
        String userInfo = JSONObject.toJSONString(employee1, SerializerFeature.WriteNonStringValueAsString);
        String s = JSON.toJSONString(employee1);
        map.put("userInfo", userInfo);

        //将用户登录信息存入Redis并设置过期时间
        Long ttlMillis = redisProperties.getTtlMillis();
        redisUtil.set("login:"+id, employeeDetail, ttlMillis);
        redisUtil.set("id",id,ttlMillis);
        //将request放入ThreadLocal以便MetaObjectHandler使用
        return new Response(200,"登录成功", map);

    }

    @Override
    public boolean logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        try {
            Claims claims = JwtUtil.parseJwt(token);
            String id = claims.getSubject();
            redisUtil.del("login:"+id);
//            BaseContext.remove();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("token非法");
            return false;
        }
    }

    @Override
    public Page page(Integer page, Integer pageSize, String name) {
        Page pageInfo = new Page(page,pageSize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        this.page(pageInfo,queryWrapper);
        return pageInfo;
    }

    @Override
    public List<String> getRolesByEmployeeId(Long employeeId) {
        List<Long> roleIds = employeeMapper.selectRoleIdsByEmployeeId(employeeId);
        List<String> roles = new ArrayList<>(8);
        for (Long roleId : roleIds){
            roles.add(employeeMapper.selectRoleById(roleId));
        }
        return roles;
    }

    @Override
    public boolean update(HttpServletRequest request, Employee employee) {
        return updateById(employee);
    }
}
