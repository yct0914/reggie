package com.marconi.reggie.pojo.DTO;

import com.marconi.reggie.pojo.DO.Dish;
import com.marconi.reggie.pojo.DO.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marconi
 * @date 2022/7/18
 */
@Data
public class DishDTO extends Dish {

    /**
     * 口味List
     */
    private List<DishFlavor> flavors;

    /**
     * 种类名称
     */
    private String categoryName;

    private Integer copies;
}
