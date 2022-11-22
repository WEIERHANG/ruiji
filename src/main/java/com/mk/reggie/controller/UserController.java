package com.mk.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.mk.reggie.common.R;
import com.mk.reggie.common.SSSUtil;
import com.mk.reggie.common.ValidateCodeUtils;
import com.mk.reggie.entity.User;
import com.mk.reggie.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 发送手机验证码短信
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone=user.getPhone();
        if(StringUtils.isNotBlank(phone)){
            //生成随机4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);

            //调用阿里云短信api
            //SSSUtil.sendMsg();
            //SMSUtils.sendMessage("阿里云短信测试","SMS_154950909",phone,code);

            //生成的验证码保存到session
//            session.setAttribute(phone,code);

            //将生成的验证码缓存到redis中，设置有效时间
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);

            return R.success("手机验证码短信发送成功");
        }
        return R.error("短信发送失败");
    }

    /**
     * 移动端用户登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map,HttpSession session){
        log.info(map.toString());
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码---
        String code=map.get("code").toString();

        //进行验证码比对，（页面提交的验证码和Session中保存的验证码比对）
//        Object codeInSession=session.getAttribute(phone);

        //从redisTemplate中获取验证码
        Object codeInSession = redisTemplate.opsForValue().get(phone);

        if(codeInSession.equals(code)&&codeInSession!=null){
            //如果能比对成功，那么登录成功
            //判断手机号对应的用户是否为新用户，如果是，自动完成注册
            LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user=userService.getOne(queryWrapper);
            if(user==null){
                user=new User();
                 user.setPhone(phone);
                 user.setStatus(1);
                 userService.save(user);
            }
            session.setAttribute("user",user.getId());

            //如果登录成功，删除redis中的phone
            redisTemplate.delete(phone);
            return R.success(user);
        }
        return R.error("登陆失败");
    }
    @PostMapping("/loginout")
    public R<String> loginout(HttpServletRequest request){
        //清楚session当前的id
       request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }
}
