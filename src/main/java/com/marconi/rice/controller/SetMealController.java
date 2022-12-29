package com.marconi.rice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.marconi.rice.common.Response;
import com.marconi.rice.pojo.DTO.SetMealDTO;
import com.marconi.rice.service.SetMealService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Marconi
 * @date 2022/7/21
 */
@RequestMapping("/setmeal")
@RestController
public class SetMealController {

    private final SetMealService setMealService;

    public SetMealController(SetMealService setMealService) {
        this.setMealService = setMealService;
    }

    @PostMapping
    public Response save(@RequestBody SetMealDTO setMealDTO){
        boolean save = setMealService.save(setMealDTO);
        if (save){
            return new Response(200,"保存成功!");
        }
        return new Response(400,"保存失败!");
    }

    @GetMapping("/page")
    public Response<Page> page(Integer page, Integer pageSize, String name){
        Page<SetMealDTO> pageInfo = setMealService.page(page, pageSize, name);
        return new Response<>(200, pageInfo);
    }

    @DeleteMapping
    public Response remove(@RequestBody ArrayList<String> removeList){
        String message = setMealService.remove(removeList);
        if (StringUtils.hasText(message)){
            return new Response(400,message);
        }
        return new Response(200,"删除套餐成功!");
    }

    @PostMapping("/status")
    public Response updateStatus(@RequestBody Map<String,Object> map){
        List<String> updateList = (List<String>) map.get("id");
        Integer status = Integer.parseInt((String) map.get("status"));
        String message = setMealService.updateStatus(updateList, status);
        if (org.apache.commons.lang.StringUtils.isEmpty(message)){
            String option = status==1 ? "启售" : "停售";
            return new Response(200,"菜品"+ option +"成功!");
        }
        return new Response(400,message);
    }

    @GetMapping("/{id}")
    public Response<SetMealDTO> getById(@PathVariable Long id){
        SetMealDTO dto = setMealService.getByIdWithSetMealDishes(id);
        return new Response(200,dto);
    }

    @PutMapping
    public Response update(@RequestBody SetMealDTO setMealDTO){
        boolean update = setMealService.update(setMealDTO);
        if (update){
            return new Response(200, "套餐修改成功!");
        }
        return new Response(400,"套餐修改失败!");
    }
}
