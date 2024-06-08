package com.mk.reggie.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private int resultCode;
    private String message;
    private T data;

    public Result(){

    }

    public Result(int resultCode, String message){
        this.resultCode = resultCode;
        this.message = message;
    }

    // 服务器处理失败
    public Result failure(){
        return new Result(Constants.RESULT_CODE_SERVER_ERROR, "服务器错误");
    }

}