package com.mk.reggie.service.impl;

import com.mk.reggie.entity.AddressBook;
import com.mk.reggie.mapper.AddressBookMapper;
import com.mk.reggie.service.IAddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 地址管理 服务实现类
 * </p>
 *
 * @author maKun
 * @since 2022-09-01
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements IAddressBookService {

}
