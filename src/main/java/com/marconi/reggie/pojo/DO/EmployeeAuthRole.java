package com.marconi.reggie.pojo.DO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Marconi
 * @date 2022/6/6
 */
@Data
@TableName("emp_auth_role")
public class EmployeeAuthRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限id
     */
    @TableField("auth_id")
    private Long authId;

    /**
     * 角色id
     */
    @TableField("role_id")
    private Long roleId;
}
