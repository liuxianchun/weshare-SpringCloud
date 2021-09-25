package com.lxc.system.controller;

import com.lxc.common.entity.ResultBean;
import com.lxc.common.entity.user.User;
import com.lxc.common.enums.UserLevelEnum;
import com.lxc.common.utils.CookieUtil;
import com.lxc.system.feign.api.ResourcesService;
import com.lxc.system.feign.api.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuxianchun
 * @date 2021/7/31
 */
@RestController
@Api(tags = "用户信息操作")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ResourcesService resourcesService;

    @ApiOperation("根据id数组获取用户信息")
    @PostMapping("/user/getUserInfoByIds")
    public ResultBean getUserInfoByIds(@RequestBody List<Integer> ids){
        return userService.getUserInfoByIds(ids);
    }

    @ApiOperation("根据uid获取用户信息")
    @GetMapping(value = "/user/getUserInfoById")
    ResultBean getUserInfoById(@RequestParam("uid") Integer uid){
        return userService.getUserInfoById(uid);
    }

    @ApiOperation("根据token获取用户详细信息")
    @GetMapping("/user/getUserDetails")
    ResultBean getUserDetails(HttpServletRequest request){
        String token = CookieUtil.getCookieValue(request, "token", false);
        User user = userService.getUserByToken(token);
        if (user==null)
            return ResultBean.error("请先登录");
        Map userLevelMap = UserLevelEnum.getUserLevelMap(user.getGrowth());
        Map<String, Object> map = new HashMap<>(){{put("userLevel",userLevelMap);put("userInfo",user);}};
        return ResultBean.success("获取成功",map);
    }

    @GetMapping("/user/subscribe/getSubscribeList")
    @ApiOperation("获取用户的关注列表")
    public ResultBean getSubscribeList(HttpServletRequest request){
        String token = CookieUtil.getCookieValue(request, "token", false);
        if (StringUtils.isEmpty(token))
            return ResultBean.error("用户未登录");
        return userService.getSubscribeList(token);
    }

    @GetMapping("/user/subscribe/getSubscribeDetail")
    @ApiOperation("获取用户的关注列表详细信息")
    public ResultBean getSubscribeDetail(HttpServletRequest request){
        String token = CookieUtil.getCookieValue(request, "token", false);
        return userService.getSubscribeDetail(token);
    }

    @PostMapping("/user/subscribe")
    @ApiOperation("关注用户/取消关注")
    public ResultBean subscribe(HttpServletRequest request,
                                @RequestParam("flowerId")Integer flowerId,
                                @RequestParam("status")Boolean status){
        String token = CookieUtil.getCookieValue(request, "token", false);
        if (StringUtils.isEmpty(token))
            return ResultBean.error("用户未登录");
        return userService.subscribe(token,flowerId,status);
    }

    @PostMapping("/user/subscribe/like")
    @ApiOperation("点赞")
    public ResultBean like(HttpServletRequest request,
                           @RequestParam("objectNo")String objectNo,
                           @RequestParam("objectType")String objectType,
                           @RequestParam("status")Boolean status){
        String token = CookieUtil.getCookieValue(request, "token", false);
        if (StringUtils.isEmpty(token))
            return ResultBean.error("用户未登录");
        return userService.like(token,objectNo,objectType,status);
    }

    @PostMapping("/user/subscribe/star")
    @ApiOperation("收藏")
    public ResultBean star(HttpServletRequest request,
                           @RequestParam("objectNo")String objectNo,
                           @RequestParam("objectType")String objectType,
                           @RequestParam("status")Boolean status){
        String token = CookieUtil.getCookieValue(request, "token", false);
        if (StringUtils.isEmpty(token))
            return ResultBean.error("用户未登录");
        return userService.star(token,objectNo,objectType,status);
    }

    @PostMapping("/user/subscribe/coin")
    @ApiOperation("投币")
    public ResultBean coin(HttpServletRequest request,
                           @RequestParam("objectNo")String objectNo,
                           @RequestParam("autherId")Integer autherId){
        String token = CookieUtil.getCookieValue(request, "token", false);
        if (StringUtils.isEmpty(token))
            return ResultBean.error("用户未登录");
        return userService.coin(token,objectNo,autherId);
    }

    @GetMapping("/user/subscribe/getNum")
    @ApiOperation("获取点赞收藏数据")
    public ResultBean like(HttpServletRequest request,
                           @RequestParam("objectNo")String objectNo,
                           @RequestParam("objectType")String objectType){
        String token = CookieUtil.getCookieValue(request, "token", false);
        return userService.getNum(token,objectNo,objectType);
    }

    @PostMapping("/user/subscribe/addView")
    @ApiOperation("增加播放量/浏览量")
    public ResultBean addView(HttpServletRequest request,
                           @RequestParam("objectNo")String objectNo,
                           @RequestParam("objectType")String objectType){
        String ip = CookieUtil.getIp(request);
        return userService.addView(objectNo,objectType,ip);
    }

    @PostMapping("/userInfo/updateAvatar")
    @ApiOperation("用户更换头像")
    public ResultBean updateAvatar(@RequestParam("picture") MultipartFile avatar,HttpServletRequest request){
        String token = CookieUtil.getCookieValue(request, "token", false);
        User userByToken = userService.getUserByToken(token);
        if (userByToken==null){
            return ResultBean.error("请先登录");
        }
        ResultBean resultBean = resourcesService.uploadPicture(avatar);
        if (!resultBean.isResult())
            return resultBean;
        return userService.updateAvatar(userByToken.getUid(), (String) resultBean.getData());
    }

    @PostMapping("/userInfo/updateUser")
    @ApiOperation("用户更新信息")
    public ResultBean updateUser(HttpServletRequest request,@RequestBody User user){
        String token = CookieUtil.getCookieValue(request, "token", false);
        return userService.updateUser(token,user);
    }

}
