package com.lxc.user.controller;

import com.lxc.common.entity.ResultBean;
import com.lxc.common.entity.user.User;
import com.lxc.user.service.api.SubscribeService;
import com.lxc.user.service.api.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuxianchun
 * @date 2021/8/1
 */
@RestController
@Api(tags = "关注/投币/收藏/点赞/下载")
public class SubscribeController {

    @Autowired
    private SubscribeService subscribeService;

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/subscribe/getSubscribeList")
    @ApiOperation("获取用户的关注列表")
    public ResultBean getSubscribeList(@RequestParam("token")String token){
        User user = userInfoService.getUserByToken(token);
        if (user==null)
            return ResultBean.error("用户未登录");
        return subscribeService.getSubscribeList(user.getUid());
    }

    @GetMapping("/subscribe/getSubscribeDetail")
    @ApiOperation("获取用户的关注列表详细信息")
    public ResultBean getSubscribeDetail(@RequestParam("token")String token){
        return subscribeService.getSubscribeDetail(token);
    }

    @PostMapping("/subscribe")
    @ApiOperation("关注用户/取消关注")
    public ResultBean subscribe(@RequestParam("token")String token,
                                @RequestParam("flowerId")Integer flowerId,
                                @RequestParam("status")Boolean status){
        User user = userInfoService.getUserByToken(token);
        if (user==null)
            return ResultBean.error("用户未登录");
        return subscribeService.subscribe(user.getUid(),flowerId,status);
    }

    @PostMapping("/subscribe/like")
    @ApiOperation("点赞")
    public ResultBean like(@RequestParam("token")String token,
                           @RequestParam("objectNo")String objectNo,
                           @RequestParam("objectType")String objectType,
                           @RequestParam("status")Boolean status){
        User user = userInfoService.getUserByToken(token);
        if (user==null)
            return ResultBean.error("用户未登录");
        return subscribeService.like(user.getUid(),objectNo,objectType,status);
    }

    @PostMapping("/subscribe/star")
    @ApiOperation("收藏")
    public ResultBean star(@RequestParam("token")String token,
                           @RequestParam("objectNo")String objectNo,
                           @RequestParam("objectType")String objectType,
                           @RequestParam("status")Boolean status){
        User user = userInfoService.getUserByToken(token);
        if (user==null)
            return ResultBean.error("用户未登录");
        return subscribeService.star(user.getUid(),objectNo,objectType,status);
    }

    @PostMapping("/subscribe/coin")
    @ApiOperation("投币")
    public ResultBean coin(@RequestParam("token")String token,
                           @RequestParam("objectNo")String objectNo,
                           @RequestParam("autherId")Integer autherId){
        User user = userInfoService.getUserByToken(token);
        if (user==null)
            return ResultBean.error("用户未登录");
        return subscribeService.coin(user.getUid(),objectNo,autherId);
    }

    @GetMapping("/subscribe/getNum")
    @ApiOperation("获取点赞收藏数据")
    public ResultBean like(@RequestParam(value = "token",required = false)String token,
                           @RequestParam("objectNo")String objectNo,
                           @RequestParam("objectType")String objectType){
        User user = userInfoService.getUserByToken(token);
        return subscribeService.getNum(objectType,objectNo,user==null?null:user.getUid());
    }

    @PostMapping("/subscribe/addView")
    @ApiOperation("增加播放量/浏览量")
    public ResultBean addView(@RequestParam("objectNo")String objectNo,
                              @RequestParam("objectType")String objectType,
                              @RequestParam("ip")String ip){
        return subscribeService.addView(objectNo,objectType,ip);
    }

    @PostMapping("/subscribe/canDownloadFile")
    @ApiOperation("用户下载文件")
    public ResultBean canDownloadFile(@RequestParam("token") String token,
                                      @RequestParam("objectNo") String objectNo){
        return subscribeService.canDownloadFile(token,objectNo);
    }

}
