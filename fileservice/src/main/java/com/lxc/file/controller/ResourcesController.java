package com.lxc.file.controller;

import com.lxc.common.entity.ResultBean;
import com.lxc.file.service.api.ResourcesService;
import com.lxc.file.service.api.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: liuxianchun
 * @Date: 2021/06/18
 * @Description: 客户端只能使用获取资源接口，其余由管理服务调用
 */
@Api(tags = "静态资源处理")
@RestController
@RequestMapping("/resources")
public class ResourcesController {

    @Autowired
    private ResourcesService resourceService;

    @Autowired
    private SearchService searchService;

    @GetMapping("/{type}")
    @ApiOperation("根据资源类型获取静态资源")
    public ResultBean getResources(@PathVariable("type") String type){
        return resourceService.getResources(type);
    }

    @PostMapping("/add")
    @ApiOperation("添加资源")
    public ResultBean addResources(@RequestParam("file")MultipartFile file,@RequestParam("type") String type){
        return resourceService.addResources(file,type);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除静态资源")
    public ResultBean deleteResources(@RequestParam("id") Integer id){
        return resourceService.deleteResources(id);
    }

    @PostMapping("/update")
    @ApiOperation("更新静态资源")
    public ResultBean updateResources(@RequestParam("file") MultipartFile file,@RequestParam("id") Integer id){
        return resourceService.updateResources(file,id);
    }

    @GetMapping("/getTop10")
    @ApiOperation("获取点赞数top10内容")
    public ResultBean getTop10(){
        return searchService.getTop10();
    }

}
