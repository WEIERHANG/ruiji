package com.mk.reggie.service.impl;

import com.mk.reggie.entity.Employee;
import com.mk.reggie.mapper.EmployeeMapper;
import com.mk.reggie.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 员工信息 服务实现类
 * </p>
 *
 * @author maKun
 * @since 2022-09-01
 */
@Service
public class EmployeeServiceImp extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

}
