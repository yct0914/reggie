package com.marconi.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.marconi.reggie.common.Response;
import com.marconi.reggie.pojo.DO.Category;
import com.marconi.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类Controller
 * 分为:
 * 菜品分类 1
 * 套餐分类 2
 * @author Marconi
 * @date 2022/7/15
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    /**
     * 分页查询所有Category信息
     * @param page 页数
     * @param pageSize  每页大小
     * @param name 根据名称查询,可以为空
     * @return 返回分页信息
     */
    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('admin','employee')")
    Response<Page> page(Integer page, Integer pageSize, String name){
        Page pageInfo = categoryService.page(page, pageSize, name);
        return new Response<>(200,pageInfo);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('admin','employee')")
    Response save(@RequestBody Category category){
        boolean save = categoryService.save(category);
        return save ? new Response(200,"新增分类成功！") : new Response(400,"新增分类失败!");
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('admin','employee')")
    Response remove(@RequestParam("ids") Long id){
        boolean remove = categoryService.remove(id);
        return remove ? new Response(200,"删除分类成功!") : new Response(400,"删除分类失败!");
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('admin','employee')")
    Response update(@RequestBody Category category){
        boolean update = categoryService.updateById(category);
        return update ? new Response(200,"更新成功!") : new Response(400,"更新失败!");
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('admin','employee')")
    Response<List<Category>> listByType(Integer type){
        return new Response<List<Category>>(200,"success",categoryService.listByType(type));
    }

}
