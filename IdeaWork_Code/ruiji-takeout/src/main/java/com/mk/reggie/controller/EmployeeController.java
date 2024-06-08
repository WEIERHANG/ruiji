package com.mk.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mk.reggie.common.R;
import com.mk.reggie.entity.Employee;
import com.mk.reggie.service.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import javax.el.ELProcessor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 员工信息 前端控制器
 * </p>
 *
 * @author maKun
 * @since 2022-09-01
 */
@RestController
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private IEmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login( HttpServletRequest request ,HttpServletResponse response ,@RequestBody Employee employee){
        //将页面提交的密码进行md5加密
        String password=employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());
        //根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername()) ;
        Employee emp=  employeeService.getOne(queryWrapper);
        //如果没有查询到则返回登录失败结果
        if(emp==null){
            return R.error("登陆失败");
        }
        //密码比对，如果不一致的情况
        if(!emp.getPassword().equals(password)){
            return R.error("密码不正确");
        }
        //员工状态是否禁用
       if(emp.getStatus()==0){
           return R.error("账号已禁用");
       }
       //登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * 退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清除session当前存的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }
    @PostMapping
    public R<String> addEmployee(@RequestParam HttpServletRequest request,@RequestBody Employee employee){
        if (employee != null) {
            log.info("添加员工信息:{}",employee.toString());
            //密码进行md5加密
            employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
            //公共字段自动填充，以下语句不需要了
//            employee.setCreateTime(LocalDateTime.now());
//            employee.setUpdateTime(LocalDateTime.now());
//            employee.setCreateUser((Long) request.getSession().getAttribute("employee"));
//            employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
            employeeService.save(employee);
            return R.success("添加成功");
        }else{
            return R.error("添加失败");
        }
    }

    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(@RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "10") int pageSize,String name){
        log.info("page={},pageSzie={},name={}",page,pageSize,name);
        //构造分页构造器
        Page pageInfo=new Page(page,pageSize);
        //构造条件
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotBlank(name),Employee::getName,name);
        //添加条件排序
        queryWrapper.orderByDesc(Employee::getCreateTime);
        //执行查询
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> updateEmployee(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
//        Long empId= (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);
        Long id=Thread.currentThread().getId();
        log.info("当前线程id为{}",id);
        employeeService.updateById(employee);
        return R.success("更新成功");
    }
    @GetMapping("/{id}")
    public R<Employee> editEmployee(@PathVariable Long id){
        log.info("根据id查询员工信息...");
        Employee employee=employeeService.getById(id);
        if(employee!=null){
            return R.success(employee);
        }
       return R.error("没有查询对应员工");
    }
}
