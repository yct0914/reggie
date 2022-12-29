package com.marconi.rice.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marconi.rice.pojo.DO.Employee;
import com.marconi.rice.common.EmployeeDetail;
import com.marconi.rice.mapper.EmployeeMapper;
import com.marconi.rice.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * UserDetailsService实现
 * @author Marconi
 * @date 2022/3/19
 */
@Slf4j
@Service
public class EmployeeDetailsServiceImpl implements UserDetailsService {


    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    EmployeeService employeeService;

    private List<String> auths;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        Employee employee = employeeMapper.selectOne(wrapper);
        if (Objects.isNull(employee) || employee.getStatus()==0){
            log.error("用户不存在");
            throw new UsernameNotFoundException("用户不存在");
        }
        Long employeeId = employee.getId();
        auths = employeeService.getRolesByEmployeeId(employeeId);
        return new EmployeeDetail(employee,auths);
    }
}