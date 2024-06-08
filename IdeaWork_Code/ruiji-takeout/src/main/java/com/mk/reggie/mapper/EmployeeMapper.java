package com.mk.reggie.mapper;

import com.mk.reggie.entity.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 员工信息 Mapper 接口
 * </p>
 *
 * @author maKun
 * @since 2022-09-01
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
