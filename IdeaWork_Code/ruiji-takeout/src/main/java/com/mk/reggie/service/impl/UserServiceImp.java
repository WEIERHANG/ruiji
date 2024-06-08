package com.mk.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mk.reggie.entity.User;
import com.mk.reggie.mapper.UserMapper;
import com.mk.reggie.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp extends ServiceImpl<UserMapper, User>implements IUserService {
}
