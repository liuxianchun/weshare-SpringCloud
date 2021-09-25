package com.lxc.management.controller;

import cn.hutool.core.lang.UUID;
import com.lxc.common.entity.ResultBean;
import com.lxc.common.utils.CookieUtil;
import com.lxc.management.component.AdminCache;
import com.lxc.management.dao.AdminMapper;
import com.lxc.management.pojo.Admin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: liuxianchun
 * @Date: 2021/06/22
 * @Description:
 */
@RestController
@Api(tags = "管理员模块")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AdminCache adminCache;

    @PostMapping("/login")
    @ApiOperation("登录")
    public ResultBean login(@RequestBody Admin admin, HttpServletRequest req,HttpServletResponse res){
        String ip = CookieUtil.getIp(req);
        Admin cache = adminCache.getAdminMap().get(ip);
        if(cache!=null&&cache.getName().equals(admin.getName())&&cache.getPassword().equals(admin.getPassword())) {
            String token = admin.getUid()+"@"+UUID.fastUUID().toString();
            CookieUtil.setCookie(req,res,"admin", token,3600,false);
            req.getSession().setAttribute("admin",token);
            return ResultBean.success("登录成功");
        }
        return ResultBean.error("用户名或密码错误");
    }

    @PostMapping("/add")
    @ApiOperation("添加管理员")
    public ResultBean uid(@RequestBody Admin admin,HttpServletRequest request){
        String token = (String) request.getSession().getAttribute("admin");
        boolean success = adminCache.addAdmin(admin,token);
        if(success)
            return ResultBean.success("添加成功");
        return ResultBean.error("添加失败");
    }

    @GetMapping("/all")
    public ResultBean hello(){
        return ResultBean.success("获取成功",adminCache.getAdminMap());
    }

}
