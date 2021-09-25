package com.lxc.system.feign.api;

import com.lxc.common.entity.PageBean;
import com.lxc.common.entity.ResultBean;
import com.lxc.system.feign.fallback.CommentServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author liuxianchun
 * @date 2021/7/25
 */
@Service
@FeignClient(value = "user-service",contextId = "comment",fallback = CommentServiceImpl.class)
public interface CommentService {

    @PostMapping("/comment/addComment")
    @ApiOperation("添加评论")
    public ResultBean addComment(@RequestBody Map paramMap);

    @PostMapping("/comment/getComment")
    @ApiOperation("获取评论")
    public PageBean getComment(@RequestBody Map paramMap);

}
