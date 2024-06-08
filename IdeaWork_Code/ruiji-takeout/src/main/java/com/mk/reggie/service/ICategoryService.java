package com.mk.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mk.reggie.entity.Category;


public interface ICategoryService extends IService<Category> {
    public void remove(Long ids);
}
