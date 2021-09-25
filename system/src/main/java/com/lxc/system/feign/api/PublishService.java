package com.lxc.system.feign.api;

import com.lxc.common.entity.ResultBean;
import com.lxc.common.entity.user.User;
import com.lxc.system.config.OpenFeignConfig;
import com.lxc.system.feign.fallback.PublishServiceImpl;
import feign.Headers;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @Author: liuxianchun
 * @Date: 2021/06/30
 * @Description: contextId防止bean名称冲突导致无法启动
 */
@Service
@FeignClient(value = "file-service",contextId = "publish",fallback = PublishServiceImpl.class)
public interface PublishService {

    @PostMapping(value = "/video/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResultBean addVideo(@RequestPart("video") MultipartFile video,
                        @RequestParam("posterUrl") String posterUrl,
                        @RequestParam("uid") Integer uid,
                        @RequestParam("username")String username,
                        @RequestParam("title") String title,
                        @RequestParam("introduction") String introduction);

    @PostMapping(value = "/video/addPoster",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResultBean addPoster(@RequestPart("poster")MultipartFile poster);


    @PostMapping(value = "/file/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResultBean publishFile(@RequestPart("file") MultipartFile file,
                           @RequestParam("uid") Integer uid,
                           @RequestParam("username")String username,
                           @RequestParam("title") String title,
                           @RequestParam("introduction") String introduction);

    @PostMapping(value = "/article/add")
    ResultBean publishArticle(@RequestParam("uid") Integer uid,
                              @RequestParam("username")String username,
                              @RequestParam("title") String title,
                              @RequestParam("content") String content);

}
