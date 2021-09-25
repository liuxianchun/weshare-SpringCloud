package com.lxc.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author: liuxianchun
 * @Date: 2021/05/20
 * @Description:
 */
@Controller
@Api(tags = "页面跳转")
public class ViewController {

    @ApiOperation("首页")
    @GetMapping(value = "/")
    public String index(){
        return "home";
    }


    @ApiOperation("页面跳转")
    @GetMapping(value = "/view/{path}")
    public String view(@PathVariable("path") String path){
        return path;
    }

}
