package com.marconi.rice.pojo.DTO;

import com.marconi.rice.pojo.DO.Dish;
import com.marconi.rice.pojo.DO.DishFlavor;
import lombok.Data;

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
