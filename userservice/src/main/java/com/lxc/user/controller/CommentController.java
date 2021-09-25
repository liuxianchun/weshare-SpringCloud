package com.lxc.user.controller;

import com.lxc.common.entity.PageBean;
import com.lxc.common.entity.ResultBean;
import com.lxc.user.service.api.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author liuxianchun
 * @date 2021/7/25
 */
@Api(tags = "评论功能")
@RequestMapping("/comment")
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/addComment")
    @ApiOperation("添加评论")
    public ResultBean addComment(@RequestBody Map paramMap){
        return commentService.addComment(paramMap);
    }

    @PostMapping("/getComment")
    @ApiOperation("获取评论")
    public PageBean getComment(@RequestBody Map paramMap){
        return commentService.getComment(paramMap);
    }

}
