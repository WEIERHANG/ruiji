package com.mk.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mk.reggie.dto.DishDto;
import com.mk.reggie.entity.Dish;
import com.mk.reggie.entity.DishFlavor;
import com.mk.reggie.mapper.DishFlavorMapper;
import com.mk.reggie.mapper.DishMapper;
import com.mk.reggie.service.IDishFlavorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishFlavorServiceImp extends ServiceImpl<DishFlavorMapper, DishFlavor>implements IDishFlavorService {

@Autowired
private DishFlavorMapper dishFlavorMapper;
//    @Override
//    public void removeFlavors(Long ids) {
//        dishFlavorMapper.removeFlavors(ids);
//    }
}
