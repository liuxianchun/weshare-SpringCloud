package com.lxc.user.service.api;

import com.lxc.common.entity.ResultBean;
import com.lxc.common.entity.user.User;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: liuxianchun
 * @Date: 2021/05/20
 * @Description:
 */
public interface LoginService {

    ResultBean findAccount(String account);

    ResultBean findUserName(String username);

    ResultBean isLogin(String token);

    ResultBean register(HttpServletRequest request,User user);

    ResultBean logout(String token);

    ResultBean login(HttpServletRequest req,String loginCode, User user);

    @Deprecated
    void getVerifyCode(HttpServletRequest req, HttpServletResponse res);

    ResponseEntity<byte[]> getVerifyCodeRPC();

    ResultBean getMailCode(String account);

}
