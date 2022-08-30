package com.marconi.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.marconi.reggie.common.CustomException;
import com.marconi.reggie.pojo.DO.Category;
import com.marconi.reggie.pojo.DO.Dish;
import com.marconi.reggie.pojo.DO.SetMeal;
import com.marconi.reggie.mapper.CategoryMapper;
import com.marconi.reggie.service.CategoryService;
import com.marconi.reggie.service.DishService;
import com.marconi.reggie.service.SetMealService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Marconi
 * @date 2022/7/15
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    DishService dishService;

    @Autowired
    SetMealService setMealService;

    @Override
    public Page page(Integer page, Integer pageSize, String name) {
        Page pageInfo = new Page(page,pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Category::getName, name);
        queryWrapper.orderByDesc(Category::getUpdateTime);
        this.page(pageInfo, queryWrapper);
        return pageInfo;
    }

    @Override
    public boolean remove(Long id) {
        //判断分类是否关联菜品
        LambdaQueryWrapper<Dish> dishWrapper = new LambdaQueryWrapper<>();
        dishWrapper.eq(Dish::getCategoryId, id);

        if (dishService.count(dishWrapper) > 0){
            List<Map<String, Object>> dishes = dishService.listMaps(dishWrapper);
            StringBuilder msg = new StringBuilder("当前分类关联了以下菜品,无法删除!\t");
            for (Map map : dishes){
                msg.append(map.get("name")+",");
            }
            throw new CustomException(msg.toString());
        }
        //判断分类是否关联套餐
        LambdaQueryWrapper<SetMeal> setMealWrapper = new LambdaQueryWrapper<>();
        setMealWrapper.eq(SetMeal::getCategoryId, id);
        if (setMealService.count(setMealWrapper) > 0){
            List<Map<String, Object>> setMeals = setMealService.listMaps(setMealWrapper);
            StringBuilder msg = new StringBuilder("当前分类关联了以下套餐,无法删除!\t");
            for (Map map : setMeals){
                msg.append("<"+map.get("name")+">");
            }
            throw new CustomException(msg.toString());
        }
        //正常删除
        return this.removeById(id);
    }

    @Override
    public List<Category> listByType(Integer type) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(!Objects.isNull(type), Category::getType, type);
        wrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        return this.list(wrapper);
    }
}
