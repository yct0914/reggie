package com.marconi.rice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.marconi.rice.common.CustomException;
import com.marconi.rice.config.properties.CommonProperties;
import com.marconi.rice.pojo.DO.*;
import com.marconi.rice.mapper.DishMapper;
import com.marconi.rice.pojo.DTO.DishDTO;
import com.marconi.rice.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.*;

/**
 * @author Marconi
 * @date 2022/7/15
 */
@Slf4j
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    DishMapper dishMapper;

    @Autowired
    DishFlavorService dishFlavorService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    CommonProperties commonProperties;

    @Autowired
    SetMealDishService setMealDishService;

    @Autowired
    SetMealService setMealService;

    @Override
    public Page<DishDTO> page(Integer page, Integer pageSize, String name) {
        Page<Dish> pageInfo = new Page(page,pageSize);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        this.page(pageInfo, queryWrapper);

        List<Dish> records = pageInfo.getRecords();
        Page<DishDTO> pageDTO = new Page<>();
        List<DishDTO> recordsDTO = new ArrayList<>();

        /**
         * records的转换
         * 因为页面展示需要CategoryName，而page方法直接从Dish获取的数据没有该属性
         * 因此只能通过循环创建DishDTO对象来封装Page<DishDTO>对象的List<DishDTO> records属性
         */
        records.forEach(item -> {
            Long categoryId =  item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            DishDTO dishDTO = new DishDTO();
            BeanUtils.copyProperties(item,dishDTO);
            dishDTO.setCategoryName(categoryName);
            recordsDTO.add(dishDTO);
        });
        BeanUtils.copyProperties(pageInfo,pageDTO,"records");
        pageDTO.setRecords(recordsDTO);

        return pageDTO;
    }

    @Override
    public String remove(List<String> removeList) {
        int size = removeList.size();
        List<String> errList = new ArrayList<>(size);
        Map<String,List<String>> cantMap = new HashMap(size);
        boolean remove;
        String image;
        String directory;
        File file;
        boolean delete;
        StringBuilder message = new StringBuilder();
        //查看菜品是否关联套餐 若关联则无法删除
        this.handleRelatedSetMeal(removeList, cantMap);
        //删除菜品
        for (String id : removeList){
            boolean r = removeDishFlavorByDishId(Long.parseLong(id));
            if (!r) {
                throw new CustomException("Remove Dish Flavors Error!");
            }
            Dish dish = this.getById(Long.parseLong(id));
            image = dish.getImage();
            remove = this.removeById(Long.parseLong(id));
            if (!remove){
                try {
                    errList.add(this.getById(Long.parseLong(id)).getName());
                }catch (NullPointerException e){
                    log.error("Remove Dish Exception",e);
                }
            }
            //删除菜品图片
            directory = commonProperties.getImageDirectory();
            file = new File(directory, image);
            delete = file.delete();
            if (!delete){
                log.info("删除文件:{}{}失败!",directory,image);
            }
        }
        if (!CollectionUtils.isEmpty(errList)){
            message.append("以下菜品删除失败：:");
            for (String name : errList){
                message.append(name+" ");
            }
        }
        if (!CollectionUtils.isEmpty(cantMap)){
            cantMap.forEach( (k, v) -> {
                message.append("《"+k+"》"+"关联了套餐:");
                v.forEach(setMeal -> {
                    message.append("<"+setMeal+">");
                });
            });

        }
        return message.toString();
    }

    @Override
    public String updateStatus(List<String> updateList, Integer status) throws NullPointerException{
        Long id;
        Integer oldStatus;

        List<String> errList = new ArrayList<>(updateList.size());
        String option = status==1 ? "启售" : "停售";

        StringBuilder message;

        for (String i : updateList){
            id = Long.parseLong(i);
            oldStatus = dishMapper.getStatusById(id);
            if (Objects.equals(status,oldStatus)){
                errList.add(this.getById(id).getName());
            }
            dishMapper.updateStatusById(id,status);
        }
        if (errList.isEmpty()){
            return null;
        }
        message = new StringBuilder("以下商品"+option+"时出错: ");
        errList.forEach(name -> {
            message.append(name+" ");
        });
        return message.toString();
    }

    @Override
    public void saveDishWithFlavors(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        boolean saveDish = this.save(dish);
        if (!saveDish){
            throw new CustomException("Save Dish Error!");
        }
        Long dishId = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (Objects.isNull(flavors)){
            throw new NullPointerException("Flavors are null!");
        }
        flavors.forEach(f -> {
            f.setDishId(dishId);
        });
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public List<Dish> listByCategoryId(Long categoryId) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Dish::getCategoryId, categoryId);
        List<Dish> dishes = this.list(queryWrapper);
        return dishes;
    }

    @Override
    public DishDTO getByIdWithFlavors(Long id) {
        Dish dish = this.getById(id);
        DishDTO dishDTO = new DishDTO();
        BeanUtils.copyProperties(dish, dishDTO);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDTO.setFlavors(flavors);
        return dishDTO;
    }

    @Override
    public boolean update(DishDTO dishDTO) {
        List<DishFlavor> flavors = dishDTO.getFlavors();
        boolean updateFlavors = dishFlavorService.updateBatchById(flavors);
        if (!updateFlavors){
            return false;
        }
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        boolean update = this.updateById(dish);
        if (!update){
            return false;
        }
        return true;
    }

    @Override
    public void handleRelatedSetMeal(List removeList, Map cantMap) {
        long count;
        //判断菜品是否关联套餐，若关联则无法删除
        LambdaQueryWrapper<SetMealDish> queryWrapper = new LambdaQueryWrapper<>();
        Iterator<String> iterator = removeList.iterator();
        while (iterator.hasNext()){
            String id = iterator.next();
            queryWrapper.eq(SetMealDish::getDishId, Long.parseLong(id));
            count = setMealDishService.count(queryWrapper);
            if (count > 0){
                //获取同一个菜品关联的多个套餐
                List<SetMealDish> list = setMealDishService.list(queryWrapper);
                String key = this.getById(Long.parseLong(id)).getName();
                List<String> values = new ArrayList<>();
                list.forEach( item -> {
                    Long setMealId = item.getSetMealId();
                    SetMeal setMeal = setMealService.getById(setMealId);
                    String value = setMeal.getName();
                    values.add(value);
                });
                cantMap.put(key, values);
                iterator.remove();
            }
            queryWrapper.clear();
        }
    }

    @Override
    public boolean removeDishFlavorByDishId(Long dishId) {
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishId);
        boolean remove = dishFlavorService.remove(queryWrapper);
        return remove;
    }


}
