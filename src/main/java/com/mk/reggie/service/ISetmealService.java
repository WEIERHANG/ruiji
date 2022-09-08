package com.mk.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mk.reggie.dto.SetmealDto;
import com.mk.reggie.entity.Setmeal;

import java.util.List;

public interface ISetmealService extends IService<Setmeal> {
    /**
     * 新套餐和关联关系
     * @param setmealDto
     */
    void saveMealAndDish(SetmealDto setmealDto);

    SetmealDto editSetmealDish(Long id);

    void updateSetealDish(SetmealDto setmealDto);

    void deleteSetealDish(List<Long> ids);
}
