package com.marconi.rice.pojo.DO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 员工角色信息表
 * @author Marconi
 * @date 2022/6/6
 */
@Data
@TableName("emp_role")
public class EmployeeRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableField
    private Long id;

    /**
     * 角色id
     */
    @TableField("role")
    private Long role;
}
