package com.marconi.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.marconi.reggie.pojo.DO.Category;

import java.util.List;

/**
 * @author Marconi
 * @date 2022/7/15
 */
public interface CategoryService extends IService<Category> {

    /**
     * 分类信息分页查询
     * @param page 查询页数
     * @param pageSize 每页的大小
     * @param name 指定名称的分页查询 可以为空
     * @return
     */
    Page page(Integer page, Integer pageSize, String name);

    /**
     * 通过id删除分类, 需要判断关联关系
     * @param id 需要删除的分类id
     * @return
     */
    boolean remove(Long id);

    /**
     * 根据分类依据查询数据
     * @param type 分类的类型 1为菜品分类 2为套餐分类
     * @return
     */
    List<Category> listByType(Integer type);
}
