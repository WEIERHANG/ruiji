package com.mk.reggie.controller;


import com.mk.reggie.common.R;
import com.mk.reggie.entity.Orders;
import com.mk.reggie.service.IOrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/order")
@Slf4j
@RestController
public class OrdersController {

    @Autowired
    private IOrdersService ordersService;

    @PostMapping("/submit")
    public R<String> submitOrders(@RequestBody Orders orders){
        //addressBookId
        log.info("{}",orders);
        ordersService.submit(orders);
        return R.success("支付成功");
    }
}
