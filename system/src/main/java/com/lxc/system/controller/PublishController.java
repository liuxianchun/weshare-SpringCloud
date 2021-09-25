package com.lxc.system.controller;

import com.lxc.common.entity.ResultBean;
import com.lxc.common.entity.user.User;
import com.lxc.common.utils.CookieUtil;
import com.lxc.system.feign.api.LoginService;
import com.lxc.system.feign.api.PublishService;
import com.lxc.system.feign.api.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: liuxianchun
 * @Date: 2021/06/30
 * @Description:
 */
@RestController
@Api(tags = "投稿")
@RequestMapping("/publish")
public class PublishController {

    @Autowired
    private PublishService publishService;

    @Autowired
    private UserService userService;

    /*@RequestParam的value必须和openfeign中value一致*/
    @PostMapping("/video")
    @ApiOperation("发布视频投稿")
    public ResultBean addVideo(HttpServletRequest request,
                               @RequestParam("video") MultipartFile video,
                               @RequestParam(value = "poster",required = false)MultipartFile poster,
                               @RequestParam("title")String title,
                               @RequestParam("introduction") String introduction){
        String token = CookieUtil.getCookieValue(request, "token", false);
        if (StringUtils.isEmpty(token))
            return ResultBean.error("请先登录账号");
        User user = userService.getUserByToken(token);
        if(user==null)
            return ResultBean.error("请先登录账号");
        if(poster!=null){
            ResultBean resultBean = publishService.addPoster(poster);
            if (resultBean.isResult()){
                return publishService.addVideo(video,(String)resultBean.getData(),user.getUid(),user.getUsername(),title,introduction);
            }else
                return ResultBean.error("封面保存失败");
        }
        return publishService.addVideo(video,null,user.getUid(),user.getUsername(),title,introduction);
    }

    @PostMapping("/file")
    @ApiOperation("发布文件投稿")
    public ResultBean publishFile(HttpServletRequest request,
                                  @RequestPart("file") MultipartFile file,
                                  @RequestParam("title") String title,
                                  @RequestParam("introduction") String introduction){
        String token = CookieUtil.getCookieValue(request, "token", false);
        if (StringUtils.isEmpty(token))
            return ResultBean.error("请先登录账号");
        User user = userService.getUserByToken(token);
        if(user==null)
            return ResultBean.error("请先登录账号");
        return publishService.publishFile(file,user.getUid(),user.getUsername(),title,introduction);
    }

    @PostMapping("/article")
    @ApiOperation("发布文章")
    public ResultBean publishArticle(HttpServletRequest request,
                                     @RequestBody Map params){
        String token = CookieUtil.getCookieValue(request, "token", false);
        if (StringUtils.isEmpty(token))
            return ResultBean.error("请先登录账号");
        User user = userService.getUserByToken(token);
        if(user==null)
            return ResultBean.error("请先登录账号");
        return publishService.publishArticle(user.getUid(),user.getUsername(),(String)params.get("title"),(String)params.get("content"));
    }

}
