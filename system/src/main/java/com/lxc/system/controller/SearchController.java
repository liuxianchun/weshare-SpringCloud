package com.lxc.system.controller;

import com.lxc.common.entity.PageBean;
import com.lxc.common.entity.ResultBean;
import com.lxc.system.feign.api.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuxianchun
 * @date 2021/7/21
 */
@RestController
@Api(tags = "搜索获取资源")
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/getHotWord")
    @ApiOperation("获取热门搜索词")
    public ResultBean getHotWord(@RequestParam("keyword")String keyword){
        return searchService.getHotWord(keyword);
    }

    @GetMapping("/getTop10")
    @ApiOperation("获取点赞数top10内容")
    public ResultBean getTop10(){
        return searchService.getTop10();
    }

    @GetMapping("/searchVideo")
    @ApiOperation("根据关键词搜索视频")
    public PageBean searchVideo(@RequestParam(value = "keyword",required = false)String keyword,
                         @RequestParam("currentPage")Integer currentPage,
                         @RequestParam("size")Integer size){
        return searchService.searchVideo(keyword,currentPage,size);
    }

    @GetMapping("/searchArticle")
    @ApiOperation("根据关键词搜索文章")
    public PageBean searchArticle(@RequestParam(value = "keyword",required = false)String keyword,
                           @RequestParam("currentPage")Integer currentPage,
                           @RequestParam("size")Integer size){
        return searchService.searchArticle(keyword,currentPage,size);
    }

    @GetMapping("/searchFile")
    @ApiOperation("根据关键词搜索文件")
    public PageBean searchFile(@RequestParam(value = "keyword",required = false)String keyword,
                        @RequestParam("currentPage")Integer currentPage,
                        @RequestParam("size")Integer size){
        return searchService.searchFile(keyword,currentPage,size);
    }
}
