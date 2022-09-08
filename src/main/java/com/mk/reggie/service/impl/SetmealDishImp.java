package com.mk.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mk.reggie.entity.SetmealDish;
import com.mk.reggie.mapper.SetmealDishMapper;
import com.mk.reggie.service.ISetmealDishService;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishImp extends ServiceImpl<SetmealDishMapper, SetmealDish>implements ISetmealDishService {
}
