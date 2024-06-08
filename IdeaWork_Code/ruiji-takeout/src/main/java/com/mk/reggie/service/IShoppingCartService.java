package com.mk.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mk.reggie.entity.ShoppingCart;

import java.util.List;

public interface IShoppingCartService extends IService<ShoppingCart> {


    List<ShoppingCart> getShoppingCartItems(Long userId);


}
