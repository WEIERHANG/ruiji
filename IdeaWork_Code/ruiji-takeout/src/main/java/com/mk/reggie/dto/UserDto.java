package com.mk.reggie.dto;


import com.mk.reggie.entity.User;
import lombok.Data;

@Data
public class UserDto extends User {
    private String code;
}