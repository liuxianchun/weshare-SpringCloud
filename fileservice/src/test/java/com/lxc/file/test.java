package com.lxc.file;
import com.lxc.common.entity.PageBean;
import com.lxc.common.utils.IdWorker;
import com.lxc.common.utils.RedisUtil;
import com.lxc.file.handler.HighlightResultHelper;
import com.lxc.file.dao.VideoMapper;
import com.lxc.file.pojo.HotWord;
import com.lxc.file.pojo.Video;
import com.lxc.file.service.api.VideoService;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * @Author: liuxianchun
 * @Date: 2021/06/24
 * @Description:
 */
@SpringBootTest
class test {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ElasticsearchRestTemplate esTemplate;

    @Autowired
    private RedisUtil redis;

    @Autowired
    private IdWorker idWorker;

    @Resource
    private VideoMapper videoMapper;

    @Autowired
    private VideoService videoService;


    @Test
    void test2(){
        boolean set = redis.set("feign",55);
        Long hhh = redis.incr("feign", 1);
        System.out.println(set+","+hhh);
        Integer service = (Integer) redis.get("feign");
        System.out.println(service);
    }

    @Test
    void delete(){
        esTemplate.deleteIndex("article_index");
        esTemplate.deleteIndex("video_index");
        esTemplate.deleteIndex("file_index");
    }

    @Test
    void test3(){
        Video video = videoMapper.selectById(1411255323055931392L);
        System.out.println(video);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //boolQueryBuilder.should(QueryBuilders.termQuery("uid", 3));
        FieldSortBuilder sortBuilder = SortBuilders.fieldSort("svid").order(SortOrder.DESC);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
        nativeSearchQueryBuilder.withSort(sortBuilder);
        NativeSearchQuery searchQuery = nativeSearchQueryBuilder.build();
        List<Video> videos = esTemplate.queryForList(searchQuery, Video.class);
        System.out.println(videos);
    }

    @Test
    void saveVideo2(){
        Video video = new Video();
        video.setSnowFlakeId(1652263450665L);
        video.setIntroduction("efdwefasdsd32wed");
        video.setPosterPath("/se3ewfwesdfs/asd");
        video.setVideoPath("/vifd3seo/sad.mp4");
        video.setSvid("SVhfwe232dj");
        video.setUid(6);
        video.setTitle("spring讲解");
        video.setIntroduction("王鹤wewf---spring");
        IndexQuery indexQuery = new IndexQueryBuilder().withObject(video).build();
        String id = esTemplate.index(indexQuery);
        System.out.println(id);
    }

    @Test
    void test4(){
        // 创建一个查询条件对象
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        /*// 拼接查询条件
        queryBuilder.should(QueryBuilders.termQuery("creator", "1"));*/
        // 创建聚合查询条件
        TermsAggregationBuilder agg = AggregationBuilders.terms("收wef到货3浮wef士德").field("title.keyword");//keyword表示不使用分词进行聚合
        // 创建查询对象
        SearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder) //添加查询条件
                .addAggregation(agg) // 添加聚合条件
                .withPageable(PageRequest.of(0, 10)) //符合查询条件的文档分页（不是聚合的分页）
                .build();
        List<Video> videos = esTemplate.queryForList(build, Video.class);
        for (Video video : videos) {
            System.out.println(video);
        }
    }

    //关键字查询
    @Test
    void test5(){
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(queryStringQuery("收wef到货3浮wef士德").defaultField("title"))
                .withPageable(PageRequest.of(0,10))
                .build();
        List<Video> videos = esTemplate.queryForList(query, Video.class);
        for (Video video : videos) {
            System.out.println(video);
        }
    }

    //精确查询,使用termQuery无法查询出结果
    @Test
    void test6(){
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchPhraseQuery("title", "讲解spring"))
                .build();
        List<Video> videos = esTemplate.queryForList(query, Video.class);
        for (Video video : videos) {
            System.out.println(video);
        }
    }

    @Test
    void test7(){
        System.out.println(videoService.getVideo("SVhfwe232dj"));
        System.out.println("-------");
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("title.keyword","收到"))
                .withPageable(PageRequest.of(0,50))
                .build();
        AggregatedPage<Video> videos = esTemplate.queryForPage(query, Video.class);
        System.out.println(videos.getTotalPages());
        System.out.println(videos.getContent());
    }

    @Test
    void testPage() {
        String from = "now-90d/d";
        String to = "now";
        HighlightBuilder.Field title = new HighlightBuilder.Field("title")
                .preTags("<em style=\"color:red\">")
                .postTags("</em>");
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.boolQuery()
                        .should(queryStringQuery("*pr*").defaultField( "title"))
                        .should(prefixQuery("title","S"))
                        .should(regexpQuery("title","*是*"))
                )
                .withSort(SortBuilders.scoreSort())
                .withPageable(PageRequest.of(0, 20))
                .withHighlightFields(title)
                .build();
        AggregatedPage<Video> videos = esTemplate.queryForPage(nativeSearchQuery, Video.class);
        AggregatedPage page = esTemplate.queryForPage(nativeSearchQuery, Video.class,new HighlightResultHelper());
        List<Video> articleEntities = page.getContent();
        System.out.println(page.getPageable());
        System.out.println(page.getTotalPages());
        System.out.println(page.toList());
        articleEntities.forEach(item -> System.out.println(item.toString()));
        Aggregations aggregations = page.getAggregations();
        NativeSearchQuery nativeSearchQuery2 = new NativeSearchQueryBuilder()
                .withSort(SortBuilders.scoreSort())
                .withPageable(PageRequest.of(0, 20))
                .build();
        AggregatedPage<Video> videos1 = esTemplate.queryForPage(nativeSearchQuery2, Video.class);
        System.out.println("-----------");
        for (Video video : videos1.toList()) {
            System.out.println(video);
        }
    }

    @Test
    void saveHotWord(){
        HotWord hotWord = new HotWord("xiaomi", 532L);
        IndexQuery indexQuery = new IndexQueryBuilder().withObject(hotWord).build();
        String index = esTemplate.index(indexQuery);
        System.out.println(index);
        HotWord hotWord2 = new HotWord("小米", 644L);
        IndexQuery indexQuery2 = new IndexQueryBuilder().withObject(hotWord2).build();
        String index2 = esTemplate.index(indexQuery2);
        System.out.println(index2);
        HotWord hotWord3 = new HotWord("SpringCloud", 684L);
        IndexQuery indexQuery3 = new IndexQueryBuilder().withObject(hotWord3).build();
        String index3 = esTemplate.index(indexQuery3);
        System.out.println(index3);
    }

    @Test
    void searchHotWord(){
        PrefixQueryBuilder prefixQueryBuilder = QueryBuilders.prefixQuery("title", "讲解");
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", "cloud");
        QueryStringQueryBuilder queryStringQueryBuilder = queryStringQuery("cloud").defaultField("title");
        QueryStringQueryBuilder queryStringQueryBuilder1 = queryStringQuery("讲解spring").defaultField("title");
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.boolQuery().should(queryStringQueryBuilder1).should(prefixQueryBuilder))
                .build();
        List<Video> videos = esTemplate.queryForList(query, Video.class);
        System.out.println(videos);
    }

    @Test
    void searchHotWord2(){
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.idsQuery().addIds("SpringCloudAlibaba"))
                .build();
        List<HotWord> hotWords = esTemplate.queryForList(query, HotWord.class);
        System.out.println(hotWords);
    }

    @Test
    void searchHotWord3(){
        PrefixQueryBuilder prefixQueryBuilder = QueryBuilders.prefixQuery("value", "");
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("value", "spring");
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.boolQuery().should(prefixQueryBuilder))
                .withSort(SortBuilders.fieldSort("searchNum").order(SortOrder.DESC))
                .build();
        List<HotWord> hotWords = esTemplate.queryForList(query, HotWord.class);
        System.out.println(hotWords);
        System.out.println(hotWords.size());
    }

    @Test
    void test8(){
        String keyword = "讲解spring";
        Integer currentPage = 1;
        Integer size = 20;
        Class cls = Video.class;
        PrefixQueryBuilder prefixQueryBuilder = QueryBuilders.prefixQuery("title", keyword);
        QueryStringQueryBuilder stringQueryBuilder = queryStringQuery(keyword).defaultField("title");
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.boolQuery().should(prefixQueryBuilder).should(stringQueryBuilder))
                .withPageable(PageRequest.of(currentPage-1,size))
                .withSort(SortBuilders.fieldSort("viewNum").order(SortOrder.DESC))
                .build();
        AggregatedPage aggregatedPage = esTemplate.queryForPage(query, cls);
        PageBean pageBean = new PageBean();
        pageBean.setCurrentPage(currentPage);
        pageBean.setResult(true);
        pageBean.setSize(size);
        pageBean.setTotalPage(aggregatedPage.getTotalPages());
        pageBean.setTotalSize((int) aggregatedPage.getTotalElements());
        pageBean.setData(aggregatedPage.toList());
    }

    //更新数据
    @Test
    void test10(){
        HashMap<String, Object> params = new HashMap<>();
        params.put("playNum",75L);
        params.put("starNum",12L);
        params.put("danmuNum",2L);
        params.put("username","超级用户root");
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.doc(params);
        UpdateQuery updateQuery5 = new UpdateQueryBuilder()
                .withUpdateRequest(updateRequest)
                .withClass(Video.class)
                .withId("1419613894969704448")
                .build();
        UpdateResponse update = esTemplate.update(updateQuery5);
        System.out.println(update);
    }

    @Test
    void test9(){
        Long aaaaa = redis.decr("aaaaa", 1);
        System.out.println(aaaaa);
    }

}
