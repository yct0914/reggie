package com.marconi.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.marconi.reggie.pojo.DO.Dish;
import com.marconi.reggie.pojo.DTO.DishDTO;

import java.util.List;
import java.util.Map;

/**
 * @author Marconi
 * @date 2022/7/15
 */
public interface DishService extends IService<Dish> {

    /**
     * 分页查询菜品信息
     * @param page 分页页数
     * @param pageSize 每页大小
     * @param name 根据名称分页查询 可以为空
     * @return 分页数据
     */
    Page<DishDTO> page(Integer page, Integer pageSize, String name);

    /**
     * 单个或批量删除菜品
     * @param removeList 需要删除所有菜品的数组
     * @return 如果没有删除成功 把删除失败的菜品名称拼接 否则返回null
     */
    String remove(List<String> removeList);

    /**
     * 启售停售 或者 批量启售停售
     * 更改status字段
     * @param updateList 需要更改的菜品idList
     * @param status 更改后的状态 (需要更改为的参数)
     * @return
     */
    String updateStatus(List<String> updateList, Integer status);

    /**
     * 保存菜品信息 包含Flavors 需要操作多张表
     * @param dishDTO
     */
    void saveDishWithFlavors(DishDTO dishDTO);

    /**
     * 通过CategoryId列出所有的Dish
     * @param categoryId 套餐分类id
     * @return Dish列表
     */
    List<Dish> listByCategoryId(Long categoryId);

    /**
     * 通过id 获取DIsh
     * @param id
     * @return
     */
    DishDTO getByIdWithFlavors(Long id);

    /**
     * 通过dishDTO更新dish信息以及dish_flavors信息
     * @param dishDTO 封装dish和flavors的对象
     * @return
     */
    boolean update(DishDTO dishDTO);

    /**
     * 判断菜品是否关联套餐 若关联则无法删除
     * @param removeList
     * @param cantMap
     */
    void handleRelatedSetMeal(List removeList, Map cantMap);

    boolean removeDishFlavorByDishId(Long dishId);
}
