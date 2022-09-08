package com.mk.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mk.reggie.entity.Orders;

public interface IOrdersService extends IService<Orders> {

    /**
     * 用户下单
     * @param orders
     */
    public void submit(Orders orders);
}
