package com.lxc.system.handler;

import com.lxc.common.entity.ResultBean;
import com.netflix.client.ClientException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;

/**
 * @Author: liuxianchun
 * @Date: 2021/06/22
 * @Description: 全局异常处理
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResultBean handleException(Exception e){
        log.error("未处理异常:"+e);
        e.printStackTrace();
        return ResultBean.error("出现异常");
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class,MethodArgumentTypeMismatchException.class})
    public ResultBean handleNotReadableException(Exception e){
        log.error("参数格式错误,原因:"+e.getCause());
        return ResultBean.error("参数格式错误");
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ResultBean handleNullException(Exception e){
        e.printStackTrace();
        return ResultBean.error("空指针异常");
    }

    @ExceptionHandler(value = IOException.class)
    public ResultBean handleIOException(Exception e){
        log.error("IO异常,原因:"+e);
        return ResultBean.error("IO异常");
    }

    @ExceptionHandler(value = {ClientException.class, FeignException.class})
    public ResultBean handleClientException(Exception e){
        log.error("feign调用异常,原因:"+e.getMessage());
        return ResultBean.error("服务器故障中，请稍后再试");
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResultBean handleMissRequestParamException(Exception e){
        log.error("参数丢失异常,原因:"+e);
        return ResultBean.error("参数丢失");
    }

}
