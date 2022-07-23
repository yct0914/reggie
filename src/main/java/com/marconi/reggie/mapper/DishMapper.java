package com.marconi.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.marconi.reggie.pojo.DO.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author Marconi
 * @date 2022/7/15
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {

    /**
     * 通过菜品id获取status
     * 1 为在售 0 为停售
     * @param id 菜品id
     * @return status
     */
    @Select("select status from dish where id = #{id};")
    Integer getStatusById(Long id);

    /**
     * 通过菜品id以及status更改菜品的status 1->0 ; 0->1
     * @param id 菜品id
     * @param status 1 为在售 0 为停售
     */
    @Update("update dish set status = #{status} where id = #{id}")
    void updateStatusById(Long id, Integer status);
}
