package com.mk.reggie.exception;

import com.mk.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理---基于代理，代理controller,aop把save方法或者其他方法拦截
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
/**
 * 进行异常处理
 */
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry")){
            String[] spilt=ex.getMessage().split(" ");
            String msg=spilt[2] + "已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }
    @ExceptionHandler(CustException.class)
    public R<String> exceptionHandler(CustException ex){
        log.error(ex.getMessage());
        return R.error(ex.getMessage());
    }
}
