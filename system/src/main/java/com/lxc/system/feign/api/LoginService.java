package com.lxc.system.feign.api;

import com.lxc.common.entity.ResultBean;
import com.lxc.common.entity.user.User;
import com.lxc.system.feign.fallback.LoginServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

/**
 * @author liuxianchun
 * @date 2021/5/23
 * 正常调用微服务,参数必须加@RequestParam,异常时调用Impl方法.
 * Feign里不用传递HttpServletResponse和HttpServletRequest.产者接口参数有HttpServletResponse
 * 和HttpServletRequest就行，也不会是空的.不能写回HttpServletResponse
 * */
@Service
@FeignClient(value = "user-service",contextId = "login",fallback = LoginServiceImpl.class)
public interface LoginService {

    @ApiOperation("查询账号是否存在")
    @GetMapping(value = "/login/findAccount")
    public ResultBean findAccount(@RequestParam("account") String account);

    @ApiOperation("查询用户名是否存在")
    @GetMapping(value = "/login/findUserName")
    public ResultBean findUserName(@RequestParam("username") String username);

    @ApiOperation("用户退出登录")
    @DeleteMapping(value = "/login/userLogout")
    public ResultBean userLogout(@RequestParam("token") String token);

    @ApiOperation("查询用户是否登录")
    @GetMapping("/login/isLogin")
    public ResultBean isLogin(@RequestParam("token") String token);

    @ApiOperation("用户登录")
    @PostMapping(value = "/login/userLogin")
    public ResultBean userLogin(@RequestParam("loginCode") String loginCode, @RequestBody User user);

    @ApiOperation("用户注册")
    @PostMapping(value = "/login/userRegister")
    public ResultBean userRegister(@RequestBody User user);

    @ApiOperation("获取验证码(RPC)")
    @GetMapping(value = "/login/getVerifyCodeRPC")
    public ResponseEntity<byte[]> getVerifyCode();

    @ApiOperation("获取邮箱注册验证码")
    @GetMapping(value = "/login/getMailCode")
    public ResultBean getMailCode(@RequestParam("account") String account);

}
