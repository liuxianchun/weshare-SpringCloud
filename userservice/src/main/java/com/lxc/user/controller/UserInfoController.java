package com.lxc.user.controller;

import com.lxc.common.entity.ResultBean;
import com.lxc.common.entity.user.User;
import com.lxc.user.service.api.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liuxianchun
 * @date 2021/7/7
 */
@RestController
@Api(tags = "用户信息")
@RequestMapping("/userInfo")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/isRoleCheck")
    @ApiOperation("判断登录，是否有资格审核")
    public ResultBean isRoleCheck(String token){
        return userInfoService.isRoleCheck(token);
    }

    @ApiOperation("判断登录并获取用户")
    @GetMapping(value = "/getUserByToken")
    public User getUserByToken(@RequestParam("token") String token){
        return userInfoService.getUserByToken(token);
    }

    @ApiOperation("根据uid获取用户")
    @GetMapping(value = "/getUserByUid")
    public User getUserByUid(@RequestParam("uid") Integer uid){
        return userInfoService.getUserByUid(uid);
    }

    @ApiOperation("根据id数组获取用户信息")
    @PostMapping(value = "/getUserInfoByIds")
    public ResultBean getUserInfoByIds(@RequestBody List<Integer> ids){
        return userInfoService.getUserInfoByIds(ids);
    }

    @ApiOperation("根据uid获取用户信息")
    @GetMapping(value = "/getUserInfoById")
    public ResultBean getUserInfoById(@RequestParam("uid") Integer uid){
        return userInfoService.getUserInfoById(uid);
    }

    @ApiOperation("更换头像")
    @PostMapping("/updateAvatar")
    public ResultBean updateAvatar(@RequestParam("uid") Integer uid,@RequestParam("path") String path){
        return userInfoService.updateAvatar(uid,path);
    }

    @ApiOperation("更新用户信息")
    @PostMapping("/updateUser")
    public ResultBean updateUser(@RequestParam("token") String token,@RequestBody User user){
        return userInfoService.updateUser(token,user);
    }

}
