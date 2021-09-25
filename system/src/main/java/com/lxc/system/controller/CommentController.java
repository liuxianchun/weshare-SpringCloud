package com.lxc.system.controller;

import com.lxc.common.entity.PageBean;
import com.lxc.common.entity.ResultBean;
import com.lxc.common.utils.CookieUtil;
import com.lxc.system.feign.api.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author liuxianchun
 * @date 2021/7/25
 */
@Api(tags = "评论相关")
@RequestMapping("/comment")
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/addComment")
    @ApiOperation("添加评论")
    public ResultBean addComment(HttpServletRequest request, @RequestBody Map paramMap){
        String token = CookieUtil.getCookieValue(request, "token", false);
        paramMap.put("token",token);
        return commentService.addComment(paramMap);
    }

    @PostMapping("/getComment")
    @ApiOperation("获取评论")
    public PageBean getComment(HttpServletRequest request,@RequestBody Map paramMap){
        String token = CookieUtil.getCookieValue(request, "token", false);
        paramMap.put("token",token);
        return commentService.getComment(paramMap);
    }

}
