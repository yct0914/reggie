package com.marconi.reggie.pojo.DTO;

import com.marconi.reggie.pojo.DO.SetMeal;
import com.marconi.reggie.pojo.DO.SetMealDish;
import lombok.Data;

import java.util.List;

/**
 * @author Marconi
 * @date 2022/7/21
 */
@Data
public class SetMealDTO extends SetMeal {

    private List<SetMealDish> setMealDishes;

    private String categoryName;
}
