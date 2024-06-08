package com.mk.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mk.reggie.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

}
