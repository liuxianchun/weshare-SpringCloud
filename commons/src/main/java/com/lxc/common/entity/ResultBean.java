package com.lxc.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liuxianchun
 * @date 2021/5/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "通用返回值")
public class ResultBean {

    @ApiModelProperty("请求是否符合要求")
    private boolean result;

    @ApiModelProperty("详细消息")
    private String message;

    @ApiModelProperty("返回数据")
    private Object data;

    @ApiModelProperty("数据总数")
    private Integer total;

    public static ResultBean success(String message){
        return new ResultBean(true,message,null,null);
    }

    public static ResultBean success(String message,Object data){
        return new ResultBean(true,message,data,null);
    }

    public static ResultBean error(String message){
        return new ResultBean(false,message,null,null);
    }

    public static ResultBean error(String message,Object data){
        return new ResultBean(false,message,data,null);
    }
}
