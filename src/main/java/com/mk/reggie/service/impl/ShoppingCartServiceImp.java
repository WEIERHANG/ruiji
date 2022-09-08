package com.mk.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mk.reggie.entity.ShoppingCart;
import com.mk.reggie.mapper.ShoppingCartMapper;
import com.mk.reggie.service.IShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImp extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements IShoppingCartService {
}
