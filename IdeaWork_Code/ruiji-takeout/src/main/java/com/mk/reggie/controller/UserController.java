package com.mk.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.mk.reggie.common.R;
import com.mk.reggie.common.ValidateCodeUtils;
import com.mk.reggie.dto.UserDto;
import com.mk.reggie.entity.User;
import com.mk.reggie.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户信息 前端控制器
 * </p>
 *
 * @author cc
 * @since 2022-05-30
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 验证码发送
     * @param phone 接收用户电话号码
     * @param session 把验证码存入session，后续登陆验证要用
     * @return RequestBody
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestParam String phone, HttpSession session) {
        //获取手机号
//        String userPhone = user.getPhone();
        //判断手机号是否为空
        if (StringUtils.isNotEmpty(phone)) {
            //利用验证码生成类来生成验证码
            String code = ValidateCodeUtils.generateValidateCode4String(4);
            //这里不太可能去真的发验证码，所以把生成的验证码在后台看一眼就好
            log.info("手机号Phone:{}   验证码Code:{}",phone,code);

            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);

//            return R.success("验证码发送成功，有效时间为5分钟");
            return R.success("验证码发送成功，有效时间为5分钟").add("loginCode", code);



        }

        return R.error("验证码发送失败");
    }


    /**
     * 前台登陆功能
     * @return
     */
    @PostMapping("/login")
    public R<? extends Object> login(@RequestParam String phone,String code, HttpSession codeInSession) {
        //从Redis中拿验证
        System.out.println("phone = "+phone);
        String tempCode = (String) redisTemplate.opsForValue().get(phone);
        System.out.println("验证：："+tempCode);
        System.out.println("输入的验证：："+code);

        int temp = 0;
        //验证码相
        if (code.equals(tempCode) && codeInSession != null) {
            //是否为新用户，如果是新用户顺手注册了
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getPhone, phone);
            //只能用getOne来匹配，不能用getById，因为没有Id给你匹配，都是空的
            User user = userService.getOne(lambdaQueryWrapper);
            if (user==null){
                //用户不存在，注册完放行
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            //此时已经登陆成功，向Redis中存入userId的信息留给过滤器进行验证放行
            redisTemplate.opsForValue().set("user", user.getId());
            System.out.println("用户ID: " + user.getId());

            //再删掉验证码
            redisTemplate.delete(phone);

            return R.success(user).add("sss",1);
        }

        return R.error("验证码错误").add("sss",0);
    }


    @PostMapping("/login22")
    public R<? extends Object> login22(@RequestParam String phone,String code, HttpSession codeInSession) {


        User user = new User();
        user.setPhone(phone);
        user.setStatus(1);
        userService.save(user);

        //此时已经登陆成功，向Redis中存入userId的信息留给过滤器进行验证放行
        redisTemplate.opsForValue().set("user", user.getId());
        System.out.println("用户ID: " + user.getId());

        //再删掉验证码
//        redisTemplate.delete(phone);

        return R.success(user).add("sss", 1);
    }



}
