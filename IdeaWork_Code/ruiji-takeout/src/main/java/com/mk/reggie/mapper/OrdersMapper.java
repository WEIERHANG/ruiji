package com.mk.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mk.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrdersMapper extends BaseMapper<Orders> {
}
