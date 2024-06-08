package com.mk.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
 import com.mk.reggie.entity.OrderDetail2;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface OrderDetailMapper2 extends BaseMapper<OrderDetail2> {

}
