package com.lxc.system.feign.api;

import com.lxc.common.entity.PageBean;
import com.lxc.common.entity.ResultBean;
import com.lxc.system.feign.fallback.SearchServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author liuxianchun
 * @date 2021/7/21
 */
@Service
@FeignClient(value = "file-service",contextId = "search",fallback = SearchServiceImpl.class)
public interface SearchService {

    @GetMapping("/video/getHotWord")
    @ApiOperation("根据输入获取搜索量前10的热词")
    ResultBean getHotWord(@RequestParam("keyword")String keyword);

    @GetMapping("/resources/getTop10")
    @ApiOperation("获取点赞数top10内容")
    ResultBean getTop10();

    @GetMapping("/video/searchVideo")
    @ApiOperation("根据关键词搜索视频")
    PageBean searchVideo(@RequestParam(value = "keyword",required = false)String keyword,
                                @RequestParam("currentPage")Integer currentPage,
                                @RequestParam("size")Integer size);

    @GetMapping("/article/searchArticle")
    @ApiOperation("根据关键词搜索文章")
    PageBean searchArticle(@RequestParam(value = "keyword",required = false)String keyword,
                           @RequestParam("currentPage")Integer currentPage,
                           @RequestParam("size")Integer size);

    @GetMapping("/file/searchFile")
    @ApiOperation("根据关键词搜索文件")
    PageBean searchFile(@RequestParam(value = "keyword",required = false)String keyword,
                           @RequestParam("currentPage")Integer currentPage,
                           @RequestParam("size")Integer size);
}
