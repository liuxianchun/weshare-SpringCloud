package com.lxc.file.schedule;

import com.lxc.common.constant.TimeConst;
import com.lxc.common.entity.ResultBean;
import com.lxc.common.utils.RedisUtil;
import com.lxc.file.config.IdWorkerConfig;
import com.lxc.file.pojo.Article;
import com.lxc.file.pojo.FileResource;
import com.lxc.file.pojo.HotWord;
import com.lxc.file.pojo.Video;
import com.lxc.file.service.api.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQueryBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author liuxianchun
 * @date 2021/7/17
 */
@Component
@Slf4j
public class ESSchedule {

    @Autowired
    private RedisUtil redis;

    @Autowired
    private ElasticsearchRestTemplate esTemplate;

    @Autowired
    private SearchService searchService;

    //redis删除key的阈值
    private static final Long deleteRedisCount = 10L;

    @Scheduled(cron = "0 0 6 * * ?")   //每天6点执行一次
    public void setHotWord(){
        Set<String> keys = redis.keys("hotWord:*");
        log.info("【定时任务】【同步搜索次数到es】开始执行,key数量:"+keys.size());
        for (String key:keys){
            NativeSearchQuery idQuery = new NativeSearchQuery(QueryBuilders.idsQuery().addIds(key.substring(8)));
            List<HotWord> hotWords = esTemplate.queryForList(idQuery, HotWord.class);
            //es中已有值，更新es
            if (hotWords.size()>0){
                UpdateRequest updateRequest = new UpdateRequest();
                HashMap<String, Object> params = new HashMap<>();
                Integer searchNum = (Integer) redis.get(key);
                params.put("searchNum",hotWords.get(0).getSearchNum()+searchNum);
                updateRequest.doc(params);
                UpdateQuery updateQuery = new UpdateQueryBuilder()
                        .withId(key.substring(key.indexOf(":") + 1))
                        .withClass(HotWord.class)
                        .withUpdateRequest(updateRequest)
                        .build();
                try {
                    esTemplate.update(updateQuery);
                    redis.decr(key,searchNum);
                    if (searchNum<deleteRedisCount)
                        redis.del(key);
                } catch (Exception e) {
                    log.error("es更新搜索次数失败,原因:{}"+e.getMessage());
                }
            }
        }
    }

    @Scheduled(cron = "0 0 * * * ?") //每小时更新top10热门内容
    public void updateTop10(){
        //获取分布式锁
        boolean success = redis.setNX("lock:updateTop10", IdWorkerConfig.IDCard, 30 * TimeConst.MINUTE);
        if (success){
            HashMap<String, Object> map = new HashMap<>();
            String key = "resources:top10";
            List videos = searchService.searchTop10List(Video.class);
            List articles = searchService.searchTop10List(Article.class);
            List files = searchService.searchTop10List(FileResource.class);
            map.put("video",videos);
            map.put("article",articles);
            map.put("file",files);
            redis.set(key,map, TimeConst.DAY);
            log.info("定时任务【更新top10内容】完成");
        }
    }

}
