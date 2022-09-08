package com.mk.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mk.reggie.entity.Setmeal;
import com.mk.reggie.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SetmealDishMapper extends BaseMapper<SetmealDish> {
}
