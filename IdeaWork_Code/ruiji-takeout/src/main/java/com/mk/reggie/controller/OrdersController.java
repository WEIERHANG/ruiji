package com.mk.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mk.reggie.common.R;
import com.mk.reggie.dto.OrderStatus;
import com.mk.reggie.dto.OrdersDto;
import com.mk.reggie.entity.*;
import com.mk.reggie.service.IAddressBookService;
import com.mk.reggie.service.IOrderDetailService;
import com.mk.reggie.service.IOrdersService;
import com.mk.reggie.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private IOrdersService ordersService;

    @Autowired
    private IOrderDetailService orderDetailService;


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private IUserService userService;

    @Autowired
    private IAddressBookService addressBookService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        ordersService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/page")
    public R<Page<OrdersDto>> page(int page, int pageSize, String number,
                                   Date beginTime,
                                   Date endTime) {
        LocalDateTime localDateTimeBegin = null;
        LocalDateTime localDateTimeEnd = null;
        // 对其时间参数进行处理
        if (beginTime != null && endTime != null) {
            // beginTime处理
            Instant instant = beginTime.toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            localDateTimeBegin = instant.atZone(zoneId).toLocalDateTime();
            //formatBeginTime = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            // endTime 进行处理
            Instant instant1 = endTime.toInstant();
            ZoneId zoneId1 = ZoneId.systemDefault();
            localDateTimeEnd = instant1.atZone(zoneId1).toLocalDateTime();
            //formatEndTime = localDateTime1.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrdersDto> pageDto = new Page<>();

        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(number)) {
            wrapper.eq("number", number);
        }
        if (!StringUtils.isEmpty(localDateTimeBegin)) {
            wrapper.ge("order_time", localDateTimeBegin);
        }
        if (!StringUtils.isEmpty(localDateTimeEnd)) {
            wrapper.le("order_time", localDateTimeEnd);
        }
        wrapper.orderByDesc("order_time");
        ordersService.page(pageInfo, wrapper);
        // 将其除了records中的内存复制到pageDto中
        BeanUtils.copyProperties(pageInfo, pageDto, "records");

        List<Orders> records = pageInfo.getRecords();

        List<OrdersDto> collect = records.stream().map((order) -> {
            OrdersDto ordersDto = new OrdersDto();

            BeanUtils.copyProperties(order, ordersDto);
            // 根据订单id查询订单详细信息

            QueryWrapper<OrderDetail> wrapperDetail = new QueryWrapper<>();
            wrapperDetail.eq("order_id", order.getId());

            List<OrderDetail> orderDetails = orderDetailService.list(wrapperDetail);
            ordersDto.setOrderDetails(orderDetails);

            // 根据userId 查询用户姓名
            Long userId = order.getUserId();
            User user = userService.getById(userId);
            ordersDto.setUserName(user.getName());
            ordersDto.setPhone(user.getPhone());

            // 获取地址信息
            Long addressBookId = order.getAddressBookId();
            AddressBook addressBook = addressBookService.getById(addressBookId);
            ordersDto.setAddress(addressBook.getDetail());
            ordersDto.setConsignee(addressBook.getConsignee());

            return ordersDto;
        }).collect(Collectors.toList());

        pageDto.setRecords(collect);

        return R.success(pageDto);
    }

    @PutMapping
    public R<String> statusOrder(@RequestBody OrderStatus orderStatus) {
        Orders orders = ordersService.getById(orderStatus.getId());
        orders.setStatus(orderStatus.getStatus());
        ordersService.updateById(orders);
        return R.success("派送完成");
    }

    @GetMapping("/userPage")
    public R<Page> userPage(int page, int pageSize) {
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrdersDto> pageDto = new Page<>();

        ordersService.page(pageInfo);
        // 将其除了records中的内存复制到pageDto中
        BeanUtils.copyProperties(pageInfo, pageDto, "records");

        List<Orders> records = pageInfo.getRecords();

        List<OrdersDto> collect = records.stream().map((order) -> {
            OrdersDto ordersDto = new OrdersDto();

            BeanUtils.copyProperties(order, ordersDto);
            // 根据订单id查询订单详细信息

            QueryWrapper<OrderDetail> wrapperDetail = new QueryWrapper<>();
            wrapperDetail.eq("order_id", order.getId());

            List<OrderDetail> orderDetails = orderDetailService.list(wrapperDetail);
            ordersDto.setOrderDetails(orderDetails);

            // 根据userId 查询用户姓名
            Long userId = order.getUserId();
            User user = userService.getById(userId);
            ordersDto.setUserName(user.getName());
            ordersDto.setPhone(user.getPhone());

            // 获取地址信息
            Long addressBookId = order.getAddressBookId();
            AddressBook addressBook = addressBookService.getById(addressBookId);
            ordersDto.setAddress(addressBook.getDetail());
            ordersDto.setConsignee(addressBook.getConsignee());

            return ordersDto;
        }).collect(Collectors.toList());

        pageDto.setRecords(collect);
        return R.success(pageDto);
    }

    @GetMapping("/list")
    public R<List<OrderDetail2>> list() {
        Long userId = (Long) redisTemplate.opsForValue().get("user");

//        Long userId = 1794619251252826114L;

        String sql = "SELECT orders.id, orders.status, orders.order_time, orders.amount, orders.remark, orders.phone, orders.address, orders.consignee, order_detail.* " +
                "FROM orders " +
                "INNER JOIN order_detail ON orders.id = order_detail.order_id " +
                "WHERE orders.user_id = ?";
        List<OrderDetail2> orderDetails = jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> {

            OrderDetail2 orderDetail = new OrderDetail2();
            orderDetail.setOrder_id(rs.getLong("id"));
            orderDetail.setStatus(Integer.valueOf(rs.getString("status")));
            orderDetail.setOrder_time(rs.getTimestamp("order_time"));
            orderDetail.setAmount((int) rs.getDouble("amount"));
            orderDetail.setRemark(rs.getString("remark"));
            orderDetail.setPhone(rs.getString("phone"));
            orderDetail.setAddress(rs.getString("address"));
            orderDetail.setConsignee(rs.getString("consignee"));
            orderDetail.setName(rs.getString("name"));
            orderDetail.setImage(rs.getString("image"));
//            ordeqrDetail.setNumber(rs.getInt("number"));

            return orderDetail;
        });
        return R.success(orderDetails);

    }
}
