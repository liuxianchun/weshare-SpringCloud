package com.lxc.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxc.user.po.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author liuxianchun
 * @date 2021/7/25
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
