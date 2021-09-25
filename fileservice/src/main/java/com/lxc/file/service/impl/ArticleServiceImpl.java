package com.lxc.file.service.impl;

import com.lxc.common.entity.ResultBean;
import com.lxc.common.entity.user.User;
import com.lxc.common.utils.IdWorker;
import com.lxc.file.dao.ArticleMapper;
import com.lxc.file.pojo.Article;
import com.lxc.file.service.api.ArticleService;
import com.lxc.file.utils.PublishUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author liuxianchun
 * @date 2021/7/3
 */
@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private IdWorker idWorker;

    @Resource
    private ArticleMapper articleMapper;

    @Autowired
    private ElasticsearchRestTemplate esTemplate;

    @Autowired
    private PublishUtil publishUtil;

    @Transactional
    @Override
    public ResultBean publishArticle(Integer uid,String username, String title, String content) {
        if (uid==null||StringUtils.isEmpty(username))
            return ResultBean.error("用户信息缺失");
        if (StringUtils.isBlank(title)||StringUtils.isBlank(content))
            return ResultBean.error("标题或内容不能为空");
        if (title.length()>30)
            return ResultBean.error("文章标题长度过长");
        if (content.length()<200)
            return ResultBean.error("文章内容长度不能少于200");
        if (content.length()>10000)
            return ResultBean.error("文章内容长度不能超过10000");
        ResultBean resultBean = publishUtil.canPublish(uid);
        if (!resultBean.isResult())
            return resultBean;
        long snowFlakeId = idWorker.snowId();
        Article article = new Article();
        article.init();
        article.setSnowFlakeId(snowFlakeId);
        article.setUid(uid);
        article.setUsername(username);
        article.setTitle(title);
        article.setContent(content);
        IndexQuery indexQuery = new IndexQueryBuilder().withObject(article).build();
        String index = esTemplate.index(indexQuery);
        if (!StringUtils.isEmpty(index)){
            int insert = articleMapper.insert(article);
            if (insert>0){
                return ResultBean.success("文章发布成功");
            }else
                log.error("文章插入数据库失败,snowFlakeId="+snowFlakeId);
        }
        log.error("文章插入elasticsearch失败,snowFlakeId="+snowFlakeId);
        return ResultBean.error("文章发布失败");
    }

}
