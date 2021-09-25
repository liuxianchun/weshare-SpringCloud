package com.lxc.gateway.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.net.ssl.SSLHandshakeException;

/**
 * @Author: liuxianchun
 * @Date: 2021/06/22
 * @Description: 全局异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public String handleException(Exception e){
        return "出现异常";
    }

    @ExceptionHandler(value = SSLHandshakeException.class)
    public String handleSSLException(Exception e){
        return "认证异常";
    }

}
