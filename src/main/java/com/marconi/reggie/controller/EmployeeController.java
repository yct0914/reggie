package com.marconi.reggie.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.marconi.reggie.pojo.DO.Employee;
import com.marconi.reggie.service.EmployeeService;
import com.marconi.reggie.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 11482
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    @PreAuthorize("hasAnyRole('admin')")
    public Response signup(@RequestBody Employee employee, HttpServletRequest request){
        boolean save = employeeService.signup(employee);
        if (!save){
            return new Response(HttpStatus.BAD_REQUEST.value(),"BadRequest");
        }
        return new Response(HttpStatus.CREATED.value(), "Created");
    }
    @PostMapping("login")
    public Response login(@RequestBody Employee employee){
        try {
            Response response = employeeService.login(employee);
            return response;
        }
        catch (UsernameNotFoundException e){
            e.printStackTrace();
            return new Response(400,"用户已存在！");
        }
    }

    @PostMapping("logout")
    @PreAuthorize("hasAnyRole('admin','employee')")
    public Response logout(HttpServletRequest request){
        boolean logout = employeeService.logout(request);
        if (logout){
            return new Response(HttpStatus.OK.value(), "注销成功");
        }
        return new Response(HttpStatus.BAD_REQUEST.value(), "注销失败");
    }

    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('admin','employee')")
    public Response<Page> page(Integer page, Integer pageSize, String name){
        Page pageInfo = employeeService.page(page, pageSize, name);
        return new Response<>(200,pageInfo);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('admin')")
    public Response update(HttpServletRequest request, @RequestBody Employee employee){
        boolean update = employeeService.update(request, employee);
        if (update){
            return new Response(200,"修改成功!");
        }
        return new Response(400,"修改失败!");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin','employee')")
    public Response<Employee> getEmployeeById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        return new Response<Employee>(200,employee);
    }

}
