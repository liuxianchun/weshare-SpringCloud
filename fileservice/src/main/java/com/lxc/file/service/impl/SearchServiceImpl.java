package com.lxc.file.service.impl;

import com.lxc.common.constant.NumConst;
import com.lxc.common.constant.TimeConst;
import com.lxc.common.entity.PageBean;
import com.lxc.common.entity.ResultBean;
import com.lxc.common.utils.RedisUtil;
import com.lxc.file.handler.HighlightResultHelper;
import com.lxc.file.pojo.Article;
import com.lxc.file.pojo.FileResource;
import com.lxc.file.pojo.HotWord;
import com.lxc.file.pojo.Video;
import com.lxc.file.service.api.SearchService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Author: liuxianchun
 * @Date: 2021/07/19
 * @Description:
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private RedisUtil redis;

    @Autowired
    private ElasticsearchRestTemplate esTemplate;

    private static final String hotWordRex = "[a-zA-Z0-9\\u4e00-\\u9fa5]{2,20}";

    //根据输入词联想获取前10热门搜索
    @Override
    public ResultBean getHotWord(String keyword) {
        PrefixQueryBuilder prefixQueryBuilder = QueryBuilders.prefixQuery("value", keyword);
        QueryStringQueryBuilder stringQueryBuilder = QueryBuilders.queryStringQuery(keyword).defaultField("value");
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.boolQuery().should(prefixQueryBuilder).should(stringQueryBuilder))
                .withSort(SortBuilders.fieldSort("searchNum").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0,10))
                .build();
        List<HotWord> hotWords = esTemplate.queryForList(query, HotWord.class);
        return ResultBean.success("获取成功",hotWords);
    }

    /**
     * 获取点赞数top10的投稿
     * @return
     */
    @Override
    public ResultBean getTop10() {
        String key = "resources:top10";
        Map<String,Object> map = (Map<String,Object>) redis.get(key);
        if (ObjectUtils.isEmpty(map)) {
            map = new HashMap<>();
            List videos = searchTop10List(Video.class);
            List articles = searchTop10List(Article.class);
            List files = searchTop10List(FileResource.class);
            map.put("video",videos);
            map.put("article",articles);
            map.put("file",files);
            redis.set(key,map,TimeConst.DAY);
        }
        return ResultBean.success("请求成功",map);
    }

    public List searchTop10List(Class<?> clazz){
        SearchQuery query = new NativeSearchQueryBuilder()
                .withPageable(PageRequest.of(NumConst.ZERO, NumConst.TEN))
                .withSort(SortBuilders.fieldSort("likeNum").order(SortOrder.DESC))
                .build();
        AggregatedPage aggregatedPage = esTemplate.queryForPage(query, clazz);
        return aggregatedPage.toList();
    }

    //根据标题搜索
    @Override
    public PageBean search(String keyword, Integer currentPage, Integer size,Class<?> clazz){
        if (size>100)
            size = 100;
        if (StringUtils.isNotBlank(keyword))
            searchHotWord(keyword);
        SearchQuery query;
        //搜索词为空，查询全部，不为空高亮查询
        if (StringUtils.isBlank(keyword)){
            query = new NativeSearchQueryBuilder()
                    .withPageable(PageRequest.of(currentPage-1,size))
                    .withSort(SortBuilders.fieldSort("timestamp").order(SortOrder.DESC))
                    .build();
        }else{
            //对搜索词进行转义，防止报错
            keyword = QueryParser.escape(keyword);
            PrefixQueryBuilder prefixQueryBuilder = QueryBuilders.prefixQuery("title", keyword);
            QueryStringQueryBuilder stringQueryBuilder = QueryBuilders.queryStringQuery(keyword).defaultField("title");
            HighlightBuilder.Field title = new HighlightBuilder.Field("title")
                    .preTags("<span style=\"color:red\">")
                    .postTags("</span>");
            query = new NativeSearchQueryBuilder()
                    .withQuery(QueryBuilders.boolQuery().should(prefixQueryBuilder).should(stringQueryBuilder))
                    .withHighlightFields(title)
                    .withPageable(PageRequest.of(currentPage-1,size))
                    .withSort(SortBuilders.fieldSort("timestamp").order(SortOrder.DESC))
                    .build();
        }
        AggregatedPage aggregatedPage = esTemplate.queryForPage(query, clazz,new HighlightResultHelper());
        PageBean pageBean = new PageBean();
        pageBean.setCurrentPage(currentPage);
        pageBean.setResult(true);
        pageBean.setSize(size);
        pageBean.setTotalPage(aggregatedPage.getTotalPages());
        pageBean.setTotalSize((int) aggregatedPage.getTotalElements());
        pageBean.setData(aggregatedPage.toList());
        return pageBean;
    }

    //搜索后增加热词搜索次数
    private void searchHotWord(String keyword){
        if (Pattern.matches(hotWordRex,keyword)){
            if (!redis.hasKey("hotWord:"+keyword)){
                //加redis锁
                boolean setSuccess = redis.setNX("lock:search:addHotWord:" + keyword, "OK", TimeConst.HOUR);
                if (setSuccess){
                    //es无该热词，创建
                    HotWord hotWord = new HotWord(keyword, 0L);
                    IndexQuery indexQuery = new IndexQueryBuilder().withObject(hotWord).build();
                    esTemplate.index(indexQuery);
                }
            }
            redis.incr("hotWord:" + keyword, 1);
        }
    }

}
