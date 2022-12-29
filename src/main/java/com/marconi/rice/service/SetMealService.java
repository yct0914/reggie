package com.marconi.rice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.marconi.rice.pojo.DO.SetMeal;
import com.marconi.rice.pojo.DTO.SetMealDTO;
import com.marconi.rice.pojo.DO.SetMealDish;

import java.util.List;

/**
 * @author Marconi
 * @date 2022/7/15
 */
public interface SetMealService extends IService<SetMeal> {

    /***
     * 保存套餐信息 包括以下两个DAO
     * @see SetMeal
     * @see SetMealDish
     * @param setMealDTO DTO对象
     * @return 是否保存成功
     */
    boolean save(SetMealDTO setMealDTO);

    /**
     * 分页查询DTO数据
     * @param page 需要分页的页数
     * @param pageSize 每页的个数
     * @param name
     * @return
     */
    Page<SetMealDTO> page(Integer page, Integer pageSize, String name);

    /**
     * 删除一个或批量删除套餐
     * @param removeList
     * @return
     */
    String remove(List<String> removeList);

    /**
     * 更新菜套餐状态
     * @param updateList
     * @param status
     * @return
     */
    String updateStatus(List<String> updateList, Integer status);

    /**
     * 获取SetMealDTO
     * @param id
     * @return
     */
    SetMealDTO getByIdWithSetMealDishes(Long id);

    /**
     * 更新套餐信息 包括SetMealDish
     * @param setMealDTO
     * @return
     */
    boolean update(SetMealDTO setMealDTO);
}
