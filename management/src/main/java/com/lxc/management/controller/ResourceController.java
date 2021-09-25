package com.lxc.management.controller;

import com.lxc.common.entity.ResultBean;
import com.lxc.management.feign.api.FeignResourcesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: liuxianchun
 * @Date: 2021/06/18
 * @Description:
 */
@Api(tags = "静态资源处理")
@RestController
@RequestMapping("/resources")
public class ResourceController {

    @Autowired
    private FeignResourcesService feignResourcesService;

    @GetMapping("/{type}")
    @ApiOperation("根据资源类型获取静态资源")
    public ResultBean getResources(@PathVariable("type") String type){
        return feignResourcesService.getResources(type);
    }

    @PostMapping("/add")
    @ApiOperation("添加静态资源")
    public ResultBean addResources(@RequestPart("file") MultipartFile file, @RequestParam("type") String type){
        System.out.println(file.getResource().getDescription());
        return feignResourcesService.addResources(file,type);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除静态资源")
    public ResultBean deleteResources(@RequestParam("id") Integer id){
        return feignResourcesService.deleteResources(id);
    }

    @PostMapping("/update")
    @ApiOperation("更新静态资源")
    public ResultBean updateResources(@RequestPart("file") MultipartFile file,@RequestParam("id") Integer id){
        return feignResourcesService.updateResources(file,id);
    }

}
