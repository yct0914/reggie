package com.marconi.rice.pojo.DO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 员工id和角色id联合表
 * @author Marconi
 * @date 2022/6/6
 */

@TableName("emp_emp_role")
@Data
public class EmployeeEmployeeRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 员工id
     */
    @TableField("emp_id")
    private Long employeeId;

    /**
     * 角色id
     */
    @TableField("role_id")
    private Long roleId;



}
