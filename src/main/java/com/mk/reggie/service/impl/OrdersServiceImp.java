package com.mk.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mk.reggie.common.BaseContext;
import com.mk.reggie.entity.*;
import com.mk.reggie.exception.CustException;
import com.mk.reggie.mapper.OrdersMapper;
import com.mk.reggie.service.*;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImp extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {
    @Autowired
    private IOrdersService ordersService;
    @Autowired
    private IShoppingCartService shoppingCartService;
    @Autowired
    private IOrderDetailService orderDetailService;
    @Autowired
    private IAddressBookService addressBookService;
    @Autowired
    private IUserService userService;

    /**
     * 用户下单
     * @param orders
     */
    @Override
    //操作多张表记得加事务
    @Transactional
    public void submit(Orders orders) {
        //获得当前id
        Long userId=BaseContext.getCurrentId();
        //查询当前用户购物车
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCartList=shoppingCartService.list(queryWrapper);
        if(shoppingCartList==null||shoppingCartList.size()==0){
            throw new CustException("购物车为空，不能下单");
        }

        //查询用户数据
        User user=userService.getById(userId);
        //查询地址数据
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        if(addressBook==null){
            throw new CustException("没有地址信息，不能下单");
        }


        long orderId = IdWorker.getId();//订单号

        //原子操作，保证多线程情况下计算值没有问题
        AtomicInteger amount = new AtomicInteger(0);

        List<OrderDetail> orderDetails = shoppingCartList.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            //金额累加操作------单份的金额-------乘------------------------份数------------转换陈intValue
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());


        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));

        //向订单表插入数据，一条数据
        this.save(orders);

        //向订单明细表插入数据，多条
        orderDetailService.saveBatch(orderDetails);

        //清空购物车数据
        shoppingCartService.remove(queryWrapper);
    }
}
