package com.mk.reggie.entity;

import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OrderDetail2 implements Serializable {

    private Long order_id;
    private Integer status;
    private Date order_time;
    private Integer amount;
    private String remark;
    private String phone;
    private String address;
    private String consignee;
    private String name;
    private String image;
    private Integer number;


}
