package com.lxc.system.controller;

import com.lxc.common.entity.ResultBean;
import com.lxc.common.entity.user.User;
import com.lxc.common.utils.CookieUtil;
import com.lxc.system.feign.api.LoginService;
import com.lxc.system.util.LoginUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


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
    @GetMapping(value = "/isLogin")
    public ResultBean isLogin(HttpServletRequest request){
        String token = CookieUtil.getCookieValue(request, "token", false);
        if (StringUtils.isEmpty(token)||!token.contains("@"))
            return ResultBean.error("未登录");
        return loginService.isLogin(token);
    }

    @ApiOperation("查询用户是否登录(token)")
    @GetMapping(value = "/isLoginByToken")
    public ResultBean isLogin(@RequestParam("token")String token){
        if (StringUtils.isEmpty(token)||!token.contains("@"))
            return ResultBean.error("未登录");
        return loginService.isLogin(token);
    }

    @ApiOperation("用户退出登录")
    @DeleteMapping(value = "/userLogout")
    public ResultBean userLogout(HttpServletRequest request){
        String token = CookieUtil.getCookieValue(request, "token", false);
        return loginService.userLogout(token);
    }

    @ApiOperation("用户登录")
    @PostMapping(value = "/userLogin")
    public ResultBean userLogin(HttpServletRequest request, HttpServletResponse response, @RequestBody User user){
        //获取session中的验证码
        String loginCode = (String)request.getSession().getAttribute("loginCode");
        ResultBean resultBean = loginService.userLogin(loginCode, user);
        //将token写入cookie
        LoginUtil.setToken(request,response,resultBean);
        return resultBean;
    }

    @ApiOperation("用户注册")
    @PostMapping(value = "/userRegister")
    public ResultBean userRegister(HttpServletRequest request, HttpServletResponse response,@RequestBody User user){
        ResultBean resultBean = loginService.userRegister(user);
        LoginUtil.setToken(request,response,resultBean);
        return resultBean;
    }

    @ApiOperation("获取验证码")
    @GetMapping(value = "/getVerifyCode")
    public ResponseEntity<byte[]> getVerifyCode(HttpServletRequest request){
        ResponseEntity<byte[]> entity = loginService.getVerifyCode();
        List<String> loginCode = entity.getHeaders().get("loginCode");
        if (loginCode!=null&&loginCode.size()>0){
            //将验证码写入session
            request.getSession().setAttribute("loginCode",loginCode.get(0));
        }
        //重新生成ResponseEntity，去除loginCode
        byte[] body = entity.getBody();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","image/png");
        return new ResponseEntity<>(body,headers, HttpStatus.OK);
    }

    @ApiOperation("获取邮箱注册验证码")
    @GetMapping(value = "/getMailCode")
    public ResultBean getMailCode(String account){
        return loginService.getMailCode(account);
    }

}
