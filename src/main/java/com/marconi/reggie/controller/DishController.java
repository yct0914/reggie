package com.marconi.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.marconi.reggie.common.Response;
import com.marconi.reggie.pojo.DO.Dish;
import com.marconi.reggie.pojo.DTO.DishDTO;
import com.marconi.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Marconi
 * @date 2022/7/16
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    DishService dishService;

    /**
     * 分页查询菜品信息
     * @param page 页数
     * @param pageSize 每页的数据条数
     * @param name 根据指定名称分页查询
     * @return http响应
     */
    @GetMapping("/page")
    public Response<Page> page(Integer page, Integer pageSize, String name){
        Page<DishDTO> pageInfo = dishService.page(page, pageSize, name);
        return new Response<>(200, pageInfo);
    }

    /**
     * 删除单个或者批量删除菜品
     * @param removeList 需要删除菜品的id数组
     * @return http响应
     */
    @DeleteMapping
    public Response remove(@RequestBody ArrayList<String> removeList){
        String message = dishService.remove(removeList);
        if (StringUtils.isEmpty(message)){
            return new Response(200,"删除成功!");
        }
        return new Response(400,message);
    }

    @PostMapping("/status")
    public Response updateStatus(@RequestBody Map<String, Object> map){
        List<String> updateList = (List<String>) map.get("id");
        Integer status = Integer.parseInt((String) map.get("status"));
        String message = dishService.updateStatus(updateList, status);
        if (StringUtils.isEmpty(message)){
            String option = status==1 ? "启售" : "停售";
            return new Response(200,"菜品"+ option +"成功!");
        }
        return new Response(400,message);
    }

    @PostMapping
    public Response save(@RequestBody DishDTO dishDTO){
        dishService.saveDishWithFlavors(dishDTO);
        return new Response(200,"success");
    }

    @GetMapping("list")
    public Response listByCategoryId(Long categoryId){
        List<Dish> dishes = dishService.listByCategoryId(categoryId);
        return new Response(200, dishes);
    }

    @GetMapping("/{id}")
    public Response getById(@PathVariable Long id){
        DishDTO dishDTO = dishService.getByIdWithFlavors(id);
        return new Response(200,dishDTO);
    }

    @PutMapping
    public Response update(@RequestBody DishDTO dishDTO){

        boolean update = dishService.update(dishDTO);
        if (update){
            return new Response(200,"更新菜品成功!");
        }
        return new Response(400, "更新失败!");
    }
}
