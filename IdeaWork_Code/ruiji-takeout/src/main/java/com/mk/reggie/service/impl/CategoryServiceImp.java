package com.mk.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mk.reggie.entity.Category;
import com.mk.reggie.entity.Dish;
import com.mk.reggie.entity.Setmeal;
import com.mk.reggie.exception.CustException;
import com.mk.reggie.mapper.CategoryMapper;
import com.mk.reggie.service.ICategoryService;
import com.mk.reggie.service.IDishService;
import com.mk.reggie.service.ISetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImp extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {
    @Autowired
    private IDishService dishService;
    @Autowired
    private ISetmealService setmealService;
    /**
     * 根据id删除分类，删除之前需要进行判断
     * @param ids
     */
    @Override
    public void remove(Long ids) {
        //用ids进行查询
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper=new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,ids);
        int count1= dishService.count(dishLambdaQueryWrapper);
        //查询当前分类是否关联了菜品，如果已经关联，抛出异常
        if(count1>0){
            //已经关联菜品，发出异常
            throw new CustException("当前菜品下关联了菜品，不能删除");

        }
        //查询当前分类是否关联了套餐，如果已关联，抛出异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper=new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,ids);
        int count2= setmealService.count(setmealLambdaQueryWrapper);
        if(count2>0){
            throw new CustException("当前菜品下关联了贪残，不能删除");
        }
        //正常删除
        super.removeById(ids);
    }
}
