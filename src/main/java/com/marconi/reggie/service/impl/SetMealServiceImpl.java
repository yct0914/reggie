package com.marconi.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.marconi.reggie.config.properties.CommonProperties;
import com.marconi.reggie.pojo.DO.Category;
import com.marconi.reggie.pojo.DO.SetMeal;
import com.marconi.reggie.mapper.SetMealMapper;
import com.marconi.reggie.pojo.DO.SetMealDish;
import com.marconi.reggie.pojo.DTO.SetMealDTO;
import com.marconi.reggie.service.CategoryService;
import com.marconi.reggie.service.SetMealDishService;
import com.marconi.reggie.service.SetMealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Marconi
 * @date 2022/7/15
 */
@Slf4j
@Service
public class SetMealServiceImpl extends ServiceImpl<SetMealMapper, SetMeal> implements SetMealService {

    @Autowired
    SetMealDishService setMealDishService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    CommonProperties commonProperties;

    @Autowired
    SetMealMapper setMealMapper;

    @Override
    public boolean save(SetMealDTO setMealDTO) {

        SetMeal setMeal = new SetMeal();
        BeanUtils.copyProperties(setMealDTO,setMeal);
        this.save(setMeal);
        Long setMealId = setMeal.getId();
        List<SetMealDish> setMealDishes = setMealDTO.getSetMealDishes();

        setMealDishes.forEach(setMealDish -> setMealDish.setSetMealId(setMealId));
        setMealDishService.saveBatch(setMealDishes);

        return true;
    }

    @Override
    public Page<SetMealDTO> page(Integer page, Integer pageSize, String name) {
        Page<SetMeal> setMealPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<SetMeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), SetMeal::getName, name);
        this.page(setMealPage, queryWrapper);

        List<SetMeal> setMealRecords = setMealPage.getRecords();
        List<SetMealDTO> setMealDTORecords = new ArrayList<>();
        Page<SetMealDTO> setMealDTOPage = new Page<>();

        setMealRecords.forEach(setMeal -> {
            Long categoryId = setMeal.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            SetMealDTO setMealDTO = new SetMealDTO();
            setMealDTO.setCategoryName(categoryName);
            BeanUtils.copyProperties(setMeal, setMealDTO);
            setMealDTORecords.add(setMealDTO);
        });
        BeanUtils.copyProperties(setMealPage,setMealDTOPage,"records");
        setMealDTOPage.setRecords(setMealDTORecords);
        return setMealDTOPage;
    }


    @Override
    public String remove(List<String> removeList) {

        ArrayList<String> errList = new ArrayList<>(removeList.size());
        boolean remove;
        for (String id : removeList){
            SetMeal setMeal = this.getById(Long.parseLong(id));
            String image = setMeal.getImage();
            remove = this.removeById(Long.parseLong(id));
            //删除SetMealDish 通过 SetMealId
            LambdaQueryWrapper<SetMealDish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SetMealDish::getSetMealId, Long.parseLong(id));
            setMealDishService.remove(queryWrapper);
            if (!remove){
                try {
                    errList.add(this.getById(Long.parseLong(id)).getName());
                }catch (NullPointerException e){
                    log.error("Remove Dish Exception",e);
                }
            }
            //删除套餐图片
            String directory = commonProperties.getImageDirectory();
            File file = new File(directory, image);
            boolean delete = file.delete();
            if (!delete){
                log.info("删除文件:{}{}失败!",directory,image);
            }
        }
        if (errList.isEmpty()){
            return null;
        }
        StringBuffer message = new StringBuffer("以下套餐删除失败: ");
        for (String name : errList){
            message.append(name+" ");
        }
        return message.toString();
    }

    @Override
    public String updateStatus(List<String> updateList, Integer status) {
        Long id;
        Integer oldStatus;
        StringBuffer message;

        List<String> errList = new ArrayList<>(updateList.size());
        String option = status == 1 ? "启售" : "停售";


        for (String i : updateList){
            id = Long.parseLong(i);
            oldStatus = setMealMapper.getStatusById(id);
            if (Objects.equals(status,oldStatus)){
                errList.add(this.getById(id).getName());
            }
            setMealMapper.updateStatusById(id,status);
        }
        if (errList.isEmpty()){
            return null;
        }
        message = new StringBuffer("以下商品"+option+"时出错: ");
        errList.forEach(name -> {
            message.append(name+" ");
        });
        return message.toString();

    }

    @Override
    public SetMealDTO getByIdWithSetMealDishes(Long id) {

        SetMeal setMeal = this.getById(id);
        SetMealDTO setMealDTO = new SetMealDTO();
        BeanUtils.copyProperties(setMeal, setMealDTO);
        LambdaQueryWrapper<SetMealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetMealDish::getSetMealId, id);
        List<SetMealDish> setMealDishes = setMealDishService.list(queryWrapper);
        setMealDTO.setSetMealDishes(setMealDishes);
        return setMealDTO;


    }

    @Override
    public boolean update(SetMealDTO setMealDTO) {
        SetMeal setMeal = new SetMeal();
        BeanUtils.copyProperties(setMealDTO, setMeal);
        boolean update = this.updateById(setMeal);
        if (!update){
            return false;
        }
        List<SetMealDish> setMealDishes = setMealDTO.getSetMealDishes();
        boolean batch = setMealDishService.updateBatchById(setMealDishes);
        return batch;
    }
}
