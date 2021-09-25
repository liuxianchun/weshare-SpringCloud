package com.lxc.management.controller;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author: liuxianchun
 * @Date: 2021/06/21
 * @Description:
 */
@Controller
@Api(tags = "页面跳转")
public class ViewController {

    @GetMapping("/")
    public String view(){
        return "login";
    }

    @GetMapping("/view/{path}")
    public String home(@PathVariable("path") String path){
        return path;
    }

}
