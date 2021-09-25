package com.lxc.system.controller;

import com.lxc.common.entity.PageBean;
import com.lxc.common.entity.ResultBean;
import com.lxc.common.utils.CookieUtil;
import com.lxc.system.feign.api.CheckService;
import com.lxc.system.feign.api.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liuxianchun
 * @date 2021/7/7
 */
@RestController
@Api(tags = "审核")
public class CheckController {

    @Autowired
    private CheckService checkService;

    @Autowired
    private UserService userService;

    @GetMapping("/check/getCheckVideo")
    @ApiOperation("获取未审核视频")
    public PageBean getCheckVideo(HttpServletRequest request,
                                  @RequestParam("current")Integer current,
                                  @RequestParam("size") Integer size){
        String token = CookieUtil.getCookieValue(request, "token", false);
        ResultBean result = userService.isRoleCheck(token);
        if (result.isResult())
            return checkService.getCheckVideo(current,size);
        return PageBean.error();
    }

    @PostMapping("/check/checkVideo")
    @ApiOperation("审核视频")
    public ResultBean checkVideo(HttpServletRequest request,
                                 @RequestParam("snowFlakeId") Long snowFlakeId,
                                 @RequestParam("status") Integer status,
                                 @RequestParam("reason") String reason){
        String token = CookieUtil.getCookieValue(request, "token", false);
        ResultBean result = userService.isRoleCheck(token);
        if (result.isResult())
            return checkService.checkVideo(snowFlakeId,status,reason);
        return ResultBean.error("权限不足");
    }

}
