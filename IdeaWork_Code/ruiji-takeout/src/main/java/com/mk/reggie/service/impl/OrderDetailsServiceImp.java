package com.mk.reggie.service.impl;
import com.mk.reggie.entity.OrderDetail;
 
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mk.reggie.entity.OrderDetail2;
import com.mk.reggie.mapper.OrderDetailMapper;
import com.mk.reggie.mapper.OrderDetailMapper2;
import com.mk.reggie.service.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailsServiceImp extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;




}
