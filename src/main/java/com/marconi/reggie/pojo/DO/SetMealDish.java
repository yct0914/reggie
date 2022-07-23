package com.marconi.reggie.pojo.DO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Marconi
 * @date 2022/7/21
 */
@Data
@TableName("setmeal_dish")
public class SetMealDish implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 套餐id
     */
    @TableField("setmeal_id")
    private Long setMealId;

    /**
     * 菜品id
     */
    private Long dishId;

    /**
     * 套餐名称
     */
    private String name;

    /**
     * 菜品原价
     */
    private BigDecimal price;

    /**
     * 套餐中菜品的份数
     */
    private Integer copies;

    /**
     * 排序
     */
    private Integer sort;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @TableField(fill = FieldFill.INSERT)
    private Long createUser;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 是否删除
     */
    @TableField("is_deleted")
    private Integer deleted;
}
