package com.lxc.user.controller;

import com.lxc.common.entity.ResultBean;
import com.lxc.common.entity.user.User;
import com.lxc.common.utils.CookieUtil;
import com.lxc.user.service.api.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: liuxianchun
 * @Date: 2021/05/19
 * @Description:
 */
@RestController
@Api(tags = "用户登录注册")
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @ApiOperation("查询账号是否存在")
    @GetMapping(value = "/findAccount")
    public ResultBean findAccount(String account){
        return loginService.findAccount(account);
    }

    @ApiOperation("查询用户名是否存在")
    @GetMapping(value = "/findUserName")
    public ResultBean findUserName(String username){
        return loginService.findUserName(username);
    }

    @ApiOperation("查询用户是否登录")
    @GetMapping("/isLogin")
    public ResultBean isLogin(@RequestParam("token") String token){
        return loginService.isLogin(token);
    }

    @ApiOperation("用户登录")
    @PostMapping(value = "/userLogin")
    public ResultBean userLogin(HttpServletRequest req,@RequestParam("loginCode")String loginCode, @RequestBody User user){
        return loginService.login(req,loginCode,user);
    }

    @ApiOperation("用户退出登录")
    @DeleteMapping(value = "/userLogout")
    public ResultBean userLogout(String token){
        return loginService.logout(token);
    }

    @ApiOperation("用户注册")
    @PostMapping(value = "/userRegister")
    public ResultBean userRegister(HttpServletRequest request,@RequestBody User user){
        return loginService.register(request,user);
    }

    /*@ApiOperation("获取验证码(本服务调用,弃用)")
    @GetMapping(value = "/getVerifyCode")
    public void getVerifyCode(HttpServletRequest req,HttpServletResponse res){
        loginService.getVerifyCode(req,res);
    }*/

    @ApiOperation("获取验证码(微服务调用)")
    @GetMapping(value = "/getVerifyCodeRPC")
    public ResponseEntity<byte[]> getVerifyCodeRPC(){
        return loginService.getVerifyCodeRPC();
    }

    @ApiOperation("获取邮箱验证码")
    @GetMapping(value = "/getMailCode")
    public ResultBean getMailCode(@RequestParam("account") String account){
        return loginService.getMailCode(account);
    }

}
