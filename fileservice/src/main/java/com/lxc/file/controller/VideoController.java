package com.lxc.file.controller;

import com.lxc.common.entity.PageBean;
import com.lxc.common.entity.ResultBean;
import com.lxc.common.entity.user.User;
import com.lxc.file.pojo.Video;
import com.lxc.file.service.api.SearchService;
import com.lxc.file.service.api.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: liuxianchun
 * @Date: 2021/06/29
 * @Description:
 */
@Api(tags = "视频处理")
@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private SearchService searchService;

    @GetMapping("/getCheckVideo")
    @ApiOperation("management模块调用,获取未审核的视频")
    public PageBean getCheckVideo(@RequestParam("current") Integer current, @RequestParam("size") Integer size){
        return videoService.getCheckVideo(current,size);
    }

    @PostMapping("/checkVideo")
    @ApiOperation("审核视频")
    public ResultBean checkVideo(@RequestParam("snowFlakeId") Long snowFlakeId,
                                 @RequestParam("status") Integer status,
                                 @RequestParam("reason") String reason){
        return videoService.checkVideo(snowFlakeId,status,reason);
    }

    @PostMapping("/add")
    @ApiOperation("发布视频")
    public ResultBean addVideo(@RequestPart("video")MultipartFile video,
                               @RequestParam(value = "posterUrl",required = false)String posterUrl,
                               @RequestParam("uid") Integer uid,
                               @RequestParam("username")String username,
                               @RequestParam("title")String title,
                               @RequestParam("introduction") String introduction){
        return videoService.addVideo(video,posterUrl,uid,username,title,introduction);
    }

    @PostMapping("/addPoster")
    @ApiOperation("发布视频上传封面")
    public ResultBean addPoster(@RequestPart("poster") MultipartFile poster){
        return videoService.addPoster(poster);
    }

    @GetMapping("/get")
    @ApiOperation("根据svid获取视频")
    public ResultBean getVideo(@RequestParam("svid") String svid){
        return videoService.getVideo(svid);
    }

    @GetMapping("/searchVideo")
    @ApiOperation("根据关键词搜索视频")
    public PageBean searchVideo(@RequestParam(value = "keyword",required = false)String keyword,
                                @RequestParam("currentPage")Integer currentPage,
                                @RequestParam("size")Integer size){
        return searchService.search(keyword,currentPage,size, Video.class);
    }

    @GetMapping("/getHotWord")
    @ApiOperation("根据输入获取搜索量前10的热词")
    public ResultBean getHotWord(@RequestParam("keyword")String keyword){
        return searchService.getHotWord(keyword);
    }

}
