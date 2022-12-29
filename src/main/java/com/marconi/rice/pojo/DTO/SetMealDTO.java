package com.marconi.rice.pojo.DTO;

import com.marconi.rice.pojo.DO.SetMeal;
import com.marconi.rice.pojo.DO.SetMealDish;
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
