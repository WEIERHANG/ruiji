package com.mk.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mk.reggie.common.BaseContext;
import com.mk.reggie.common.R;
import com.mk.reggie.entity.ShoppingCart;
import com.mk.reggie.service.IShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/shoppingCart")
@Slf4j
@RestController
public class ShoppingCartController {
    @Autowired
    private IShoppingCartService shoppingCartService;

    /**
     * 将菜品添加至购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("{}",shoppingCart);
        //设置用户id，指定当前用户的购物车
        Long currentId= BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        //查询菜品或套餐是否在购物车中
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);

        if(shoppingCart.getDishId()!=null){
            //--菜品
            queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }else {
            //套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        //查询当前菜品或者套餐是否在购物车当中
        //SQL--Select * from shopping_cart where user_id=? and dish_id/setmeal_id=?
        ShoppingCart shoppingCartServiceOne = shoppingCartService.getOne(queryWrapper);

        if(shoppingCartServiceOne!=null){
            //如果存在，在原有数量基础加1
            Integer num=shoppingCartServiceOne.getNumber();
            shoppingCartServiceOne.setNumber(num+1);
            shoppingCartServiceOne.setCreateTime(LocalDateTime.now());
            shoppingCartService.updateById(shoppingCartServiceOne);
        }else {
            //如果不存在，添加到购物车，数量默认就是1
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            shoppingCartServiceOne=shoppingCart;
        }
        return R.success(shoppingCartServiceOne);
    }

    /**
     * 查看当前用的购物车
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("查看购物车...");
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCartList=shoppingCartService.list(queryWrapper);
        return R.success(shoppingCartList);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean(){
        //SQL---delete from shopping_cart where user_id=?
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("购物车已清空");

    }

    /**
     * 减少购物车数量
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public R<ShoppingCart> subShopp(@RequestBody ShoppingCart shoppingCart){
        log.info("{}",shoppingCart);
        long userId=BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);

        if(shoppingCart.getDishId()!=null){
            queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }else {
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        //查询出当前菜品或者套餐
        ShoppingCart shoppingCartServiceOne = shoppingCartService.getOne(queryWrapper);
        Integer num=shoppingCartServiceOne.getNumber();
        if(num>1&&num!=null){
        if(shoppingCartServiceOne!=null){
            shoppingCartServiceOne.setNumber(num-1);
            shoppingCart=shoppingCartServiceOne;
            shoppingCartService.updateById(shoppingCart);
        }
        }else {
            shoppingCartService.remove(queryWrapper);
            shoppingCart=shoppingCartServiceOne;
        }
        return  R.success(shoppingCart);
    }
}
