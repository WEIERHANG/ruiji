package com.mk.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mk.reggie.dto.SetmealDto;
import com.mk.reggie.entity.Setmeal;
import com.mk.reggie.entity.SetmealDish;
import com.mk.reggie.exception.CustException;
import com.mk.reggie.mapper.SetmealMapper;
import com.mk.reggie.service.ISetmealDishService;
import com.mk.reggie.service.ISetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service

@Transactional
public class SetmealServiceImp extends ServiceImpl<SetmealMapper, Setmeal> implements ISetmealService {
    @Autowired
    private ISetmealDishService setmealDishService;
    @Autowired
    private ISetmealService setmealService;

    @Override
    @Transactional
    public void saveMealAndDish(SetmealDto setmealDto) {
        //保存套餐基本信息
        this.save(setmealDto);
        //获取SetemalDish集合，但是这个集合里setmealId没有值
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //保存套餐和菜品关联信息，操作setema_dish表
        //将集合遍历出来，给setmealId赋值
        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getCategoryId());
            return  item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 查看套餐和菜品对应信息
     * @param id
     * @return
     */
    @Override
    public SetmealDto editSetmealDish(Long id) {
        //查询套餐信息
        Setmeal setmeal=this.getById(id);
        SetmealDto setmealDto=new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        //查询菜品信息
        LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(SetmealDish::getCreateTime);
        queryWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());
        List<SetmealDish> setmealDishList=setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(setmealDishList);
        return setmealDto;
    }
    @Override
    public void updateSetealDish(SetmealDto setmealDto) {
        this.updateById(setmealDto);
        //将这条id信息查出来
        Setmeal setmeal=setmealService.getById(setmealDto.getId());
        //将套餐和菜品对应表中的菜品先删除
        LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());
        queryWrapper.orderByDesc(SetmealDish::getCreateTime);
        setmealDishService.remove(queryWrapper);
        //执行插入操作
        List<SetmealDish> setmealDishList=setmealDto.getSetmealDishes();
        setmealDishList.stream().map((item)->{
            //item指的是SetmealDish对象
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishList);

    }

    /**
     * 删除套餐和  套餐--菜品对应信息
     * @param ids
     */
    @Override
            //也可以是(List<Long> ids)
    public void deleteSetealDish(List<Long> ids) {
        //第一种方法
        //List<String> setMealDishList=Arrays.asList(ids);
//        //查询套餐状态，确定是否可以删除
//        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
//        queryWrapper.eq(Setmeal::getStatus,1);
//        int count=this.count(query());
//        if(count>0){
//            throw new CustException("套餐正在售卖中...不能删除");
//        }
//        //删除套餐表
//        this.removeByIds(setMealDishList);
//        //删除套餐--菜品表信息
//        for(String str:ids){
//            LambdaQueryWrapper<SetmealDish> queryWrapper1=new LambdaQueryWrapper<>();
//            queryWrapper1.eq(SetmealDish::getSetmealId,str);
//            setmealDishService.remove(queryWrapper1);
//        }
        //第二种方法
        //查看套餐状态，查看是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        //count* from Setmeal where id in(1,2,3) and status=1
        queryWrapper.eq(Setmeal::getStatus,1);
        queryWrapper.in(Setmeal::getId,ids);
        int count=this.count(queryWrapper);
        if(count >0){
            throw new CustException("套餐正在售卖...不能删除");
        }
        this.removeByIds(ids);
        //删除套餐--菜品对应信息
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        //如果getSetmealId in(ids)一致，直接删除
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(lambdaQueryWrapper);
    }
}
