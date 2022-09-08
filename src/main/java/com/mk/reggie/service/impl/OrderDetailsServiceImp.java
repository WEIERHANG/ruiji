package com.mk.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mk.reggie.entity.OrderDetail;
import com.mk.reggie.mapper.OrderDetailMapper;
import com.mk.reggie.service.IOrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailsServiceImp extends ServiceImpl<OrderDetailMapper, OrderDetail>implements IOrderDetailService {
}
