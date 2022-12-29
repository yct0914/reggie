package com.marconi.rice.pojo.DO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 员工信息表
 * @author marconi
 * @date 2022/3/16
 */
@Data
@TableName("employee")
public class Employee implements Serializable {
    private static final Long serialVersionUID = 1L;

    /**
     * pk
     */
    @TableId
    private Long id;

    /**
     * 员工名字
     */
    @TableField
    private String name;

    /**
     * 员工登录用户名
     */
    @TableField
    private String username;

    /**
     * 员工登录密码
     */
    @TableField(fill = FieldFill.INSERT)
    private String password;

    /**
     * 员工电话
     */
    @TableField
    private String phone;

    /**
     * 性别
     */
    @TableField
    private String sex;

    /**
     * 身份证号码
     */
    @TableField
    private String idNumber;

    /**
     * 员工账号创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 账号更新信息时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 员工状态 是否离职
     * 1 为在职 0 为离职
     */
    @TableField
    private Integer status;
}
