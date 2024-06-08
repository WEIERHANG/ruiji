package com.mk.reggie.mapper;

import com.mk.reggie.entity.AddressBook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 地址管理 Mapper 接口
 * </p>
 *
 * @author maKun
 * @since 2022-09-01
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {

}
