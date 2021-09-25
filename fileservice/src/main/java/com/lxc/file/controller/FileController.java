package com.lxc.file.controller;

import com.lxc.common.entity.PageBean;
import com.lxc.common.entity.ResultBean;
import com.lxc.common.entity.user.User;
import com.lxc.file.FileService;
import com.lxc.file.pojo.FileResource;
import com.lxc.file.service.api.FileResourceService;
import com.lxc.file.service.api.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;

/**
 * @Author: liuxianchun
 * @Date: 2021/07/01
 * @Description:
 */
@RestController
@RequestMapping("/file")
@Api(tags = "分享文件操作")
public class FileController {

    @Autowired
    private FileResourceService fileResourceService;

    @Autowired
    private SearchService searchService;

    @PostMapping("/add")
    @ApiOperation("发布文件资源")
    ResultBean publishFile(@RequestParam("file") MultipartFile file,
                           @RequestParam("uid") Integer uid,
                           @RequestParam("username")String username,
                           @RequestParam("title") String title,
                           @RequestParam("introduction") String introduction){
        return fileResourceService.publishFile(uid,username,file,title,introduction);
    }

    @GetMapping("/searchFile")
    @ApiOperation("根据关键词搜索文件")
    public PageBean searchFile(@RequestParam(value = "keyword",required = false)String keyword,
                               @RequestParam("currentPage")Integer currentPage,
                               @RequestParam("size")Integer size){
        return searchService.search(keyword,currentPage,size,FileResource.class);
    }

    @PostMapping(value = "/downloadFile")
    @ApiOperation(("下载文件"))
    public void downloadFile(@RequestParam("objectNo")String objectNo,HttpServletResponse response){
        fileResourceService.downloadFile(objectNo,response);
    }

    @PostMapping("/uploadPicture")
    @ApiOperation("上传图片")
    public ResultBean uploadPicture(@RequestParam("picture") MultipartFile file){
        return fileResourceService.uploadPicture(file);
    }

}
