package com.lxc.system.feign.api;

import com.lxc.common.entity.ResultBean;
import com.lxc.common.entity.user.User;
import com.lxc.system.feign.fallback.LoginServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author liuxianchun
 * @date 2021/7/7
 */
@Service
@FeignClient(value = "user-service",contextId = "userInfo",fallback = LoginServiceImpl.class)
public interface UserService {

    @ApiOperation("判断用户角色")
    @GetMapping("/userInfo/isRoleCheck")
    ResultBean isRoleCheck(@RequestParam("token") String token);

    @ApiOperation("根据token获取用户信息")
    @GetMapping(value = "/userInfo/getUserByToken")
    User getUserByToken(@RequestParam("token") String token);

    @ApiOperation("根据uid获取用户信息")
    @GetMapping(value = "/userInfo/getUserByUid")
    User getUserByUid(@RequestParam("uid") Integer uid);

    @ApiOperation("根据id数组获取用户信息")
    @PostMapping(value = "/userInfo/getUserInfoByIds")
    ResultBean getUserInfoByIds(@RequestBody List<Integer> ids);

    @ApiOperation("根据id获取用户信息")
    @GetMapping(value = "/userInfo/getUserInfoById")
    ResultBean getUserInfoById(@RequestParam("uid") Integer uid);

    @GetMapping("/subscribe/getSubscribeList")
    @ApiOperation("获取用户的关注列表")
    public ResultBean getSubscribeList(@RequestParam("token")String token);

    @GetMapping("/subscribe/getSubscribeDetail")
    @ApiOperation("获取用户的关注列表")
    public ResultBean getSubscribeDetail(@RequestParam("token")String token);

    @PostMapping("/subscribe")
    @ApiOperation("关注用户/取消关注")
    public ResultBean subscribe(@RequestParam("token")String token,
                                @RequestParam("flowerId")Integer flowerId,
                                @RequestParam("status")Boolean status);

    @PostMapping("/subscribe/like")
    @ApiOperation("点赞")
    public ResultBean like(@RequestParam("token")String token,
                           @RequestParam("objectNo")String objectNo,
                           @RequestParam("objectType")String objectType,
                           @RequestParam("status")Boolean status);

    @PostMapping("/subscribe/star")
    @ApiOperation("收藏")
    public ResultBean star(@RequestParam("token")String token,
                           @RequestParam("objectNo")String objectNo,
                           @RequestParam("objectType")String objectType,
                           @RequestParam("status")Boolean status);

    @PostMapping("/subscribe/coin")
    @ApiOperation("投币")
    public ResultBean coin(@RequestParam("token")String token,
                           @RequestParam("objectNo")String objectNo,
                           @RequestParam("autherId")Integer autherId);

    @GetMapping("/subscribe/getNum")
    @ApiOperation("获取点赞收藏数据")
    public ResultBean getNum(@RequestParam(value = "token",required = false)String token,
                           @RequestParam("objectNo")String objectNo,
                           @RequestParam("objectType")String objectType);

    @PostMapping("/subscribe/addView")
    @ApiOperation("增加播放量/浏览量")
    public ResultBean addView(@RequestParam("objectNo")String objectNo,
                              @RequestParam("objectType")String objectType,
                              @RequestParam("ip")String ip);

    @PostMapping("/subscribe/canDownloadFile")
    @ApiOperation("用户是否可以下载文件")
    public ResultBean canDownloadFile(@RequestParam("token") String token,
                                      @RequestParam("objectNo") String objectNo);

    @ApiOperation("更换头像")
    @PostMapping("/userInfo/updateAvatar")
    public ResultBean updateAvatar(@RequestParam("uid") Integer uid,@RequestParam("path") String path);

    @PostMapping("/userInfo/updateUser")
    @ApiOperation("用户更新信息")
    public ResultBean updateUser(@RequestParam("token") String token,@RequestBody User user);

}
