package com.lxc.file.handler;

import com.lxc.common.entity.ResultBean;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchStatusException;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    @ExceptionHandler(value = ElasticsearchStatusException.class)
    public ResultBean handleElasticsearchStatusException(Exception e){
        log.error("查询失败,原因:{}",e.getMessage());
        return ResultBean.success("未查询到结果");
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

}
