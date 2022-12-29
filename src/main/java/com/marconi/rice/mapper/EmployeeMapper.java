package com.marconi.rice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.marconi.rice.pojo.DO.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Marconi
 * @date 2022/3/15
 */
@SuppressWarnings("ALL")
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

    /**
     * 通过员工id查询角色id
     * @param employeeId 员工id
     * @return 该员工的角色
     */
    @Select("select role_id from emp_emp_role where emp_id = #{employeeId}")
    List<Long> selectRoleIdsByEmployeeId(Long employeeId);

    /**
     * 通过id查询角色
     * @param id 角色id
     * @return 角色名称
     */
    @Select("select role from emp_role where id = #{id}")
    String selectRoleById(Long id);

    /**
     * 通过角色名称查询角色id
     * @param role 角色名称
     * @return 角色id
     */
    @Select("select id from emp_role where role = #{role}")
    Long selectRoleIdByRole(String role);

    /**
     * 插入员工-角色关联表
     * @param employeeId 员工id
     * @param roleId 角色id
     */
    @Insert("insert into emp_emp_role values (#{employeeId}, #{roleId})")
    void insertEmployeeRole(Long employeeId, Long roleId);

}
