package com.lxc.file.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxc.file.pojo.Article;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author liuxianchun
 * @date 2021/7/3
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}
