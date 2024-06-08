package com.mk.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mk.reggie.dto.DishDto;
import com.mk.reggie.entity.Dish;
import com.mk.reggie.entity.DishFlavor;

import java.util.List;

public interface IDishService extends IService<Dish> {
    //新菜品，同时插入菜品对应的口味数据，需要操作两张表：dish、dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    /**
     * 查询菜品信息和对应口味信息
     * @param id
     * @return
     */
    public DishDto selectFlavors(Long id);


    void updateDishAndFlavors(DishDto dishDto);
}
