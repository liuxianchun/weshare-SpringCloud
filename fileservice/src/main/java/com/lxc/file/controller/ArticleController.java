package com.lxc.file.controller;

import com.lxc.common.entity.PageBean;
import com.lxc.common.entity.ResultBean;
import com.lxc.common.entity.user.User;
import com.lxc.file.pojo.Article;
import com.lxc.file.service.api.ArticleService;
import com.lxc.file.service.api.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author liuxianchun
 * @date 2021/7/3
 */
@Api(tags = "文章操作")
@RequestMapping("/article")
@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private SearchService searchService;

    @PostMapping(value = "/add")
    @ApiOperation("发布文章")
    ResultBean publishArticle(@RequestParam("uid") Integer uid,
                              @RequestParam("username")String username,
                              @RequestParam("title") String title,
                              @RequestParam("content") String content){
        return articleService.publishArticle(uid,username,title,content);
    }

    @GetMapping("/searchArticle")
    @ApiOperation("根据关键词搜索文章")
    public PageBean searchArticle(@RequestParam(value = "keyword",required = false)String keyword,
                                  @RequestParam("currentPage")Integer currentPage,
                                  @RequestParam("size")Integer size){
        return searchService.search(keyword,currentPage,size, Article.class);
    }

}
