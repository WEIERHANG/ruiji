package com.mk.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mk.reggie.dto.DishDto;
import com.mk.reggie.entity.Dish;
import com.mk.reggie.entity.DishFlavor;
import com.mk.reggie.mapper.DishMapper;
import com.mk.reggie.service.IDishFlavorService;
import com.mk.reggie.service.IDishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DishServiceImp extends ServiceImpl<DishMapper, Dish> implements IDishService {
    /**
     * 新增菜品，同时保存口味数据
     * @param dishDto
     */
    @Autowired
    private IDishFlavorService dishFlavorService;
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);
        Long dishId=dishDto.getId();
        //保存菜品口味dish_flavor----saveBatch保存集合（批量保存）
        List<DishFlavor> flavors = dishDto.getFlavors();
//        for(DishFlavor dish:flavors){
////            flavors.set(0,dish).setDishId(dishId);
//            dish.setDishId(dishId);
//
//        }
        //获取集合里面的dishId---item代表DishFlavor对象
        flavors.stream().map((item)->{
            item.setDishId(dishId);
            return  item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 查询菜品信息和对应口味信息
     * @param id
     * @return
     */
    @Override
    public DishDto selectFlavors(Long id) {
        //查询菜品基本信息
        Dish dish=this.getById(id);
        DishDto dishDto=new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        //查询口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(dishFlavors);
        return dishDto;
    }

    /**
     * 更新菜品和口味
     * @param dishDto
     */
    @Override
    public void updateDishAndFlavors(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);
        //清理当前菜品对应口味数据--dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //添加当前提交过的口味数据--dish_flavor表的insert操作
        List<DishFlavor> dishFlavors=dishDto.getFlavors();
        dishFlavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(dishFlavors);
    }


}
