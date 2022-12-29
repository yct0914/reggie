package com.marconi.rice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.marconi.rice.pojo.DO.Employee;
import com.marconi.rice.common.Response;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 11482
 */
public interface EmployeeService extends IService<Employee> {

    /**
     * 添加员工接口
     * @param employee 注册信息封装的员工对象
     * @return 注册是否成功
     */
    boolean signup(Employee employee);

    /**
     * Employee登录接口
     * @param employee 登录信息封装的员工对象
     * @return 返回登录信息传到前端服务器
     */
    Response login(Employee employee);

    /**
     * Employee登录注销接口
     * @param request http请求
     * @return 是否注销成功
     */
    boolean logout(HttpServletRequest request);

    /**
     * 分页查询返回前端的数据
     * @param page 分页的页数
     * @param pageSize 每页的数据条数
     * @param name 指定名称数据 可以为空
     * @return 分页响应数据
     */
    Page page(Integer page, Integer pageSize, String name);

    /**
     * 通过员工id获取对应角色
     * @param employeeId 员工id
     * @return 员工对应的所有角色字符串列表
     */
    List<String> getRolesByEmployeeId(Long employeeId);

    /**
     * 更新员工信息
     * @param request http请求
     * @param employee 员工更新的信息封装 信息可以不完全
     * @return 是否更新成功
     */
    boolean update(HttpServletRequest request, Employee employee);
}
