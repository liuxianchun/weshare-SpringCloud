package com.lxc.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxc.common.entity.PageBean;
import com.lxc.common.entity.ResultBean;
import com.lxc.common.entity.user.User;
import com.lxc.common.enums.UserLevelEnum;
import com.lxc.common.utils.IdWorker;
import com.lxc.common.utils.RedisUtil;
import com.lxc.user.dao.CommentMapper;
import com.lxc.user.po.Comment;
import com.lxc.common.entity.user.CommentType;
import com.lxc.user.service.api.CommentService;
import com.lxc.user.service.api.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author liuxianchun
 * @date 2021/7/25
 */
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private RedisUtil redis;

    @Override
    public ResultBean addComment(Map paramMap) {
        log.info("接收参数:"+paramMap);
        if (paramMap.isEmpty())
            return ResultBean.error("参数为空");
        String token = (String) paramMap.get("token");
        User user = userInfoService.getUserByToken(token);
        if (user==null)
            return ResultBean.error("用户未登录");
        String objectNo = (String) paramMap.get("objectNo");
        String objectType = (String) paramMap.get("objectType");
        String content = (String) paramMap.get("content");
        if (objectNo==null)
            return ResultBean.error("评论类型主键为空");
        if (!CommentType.list().contains(objectType))
            return ResultBean.error("评论类型错误");
        if (StringUtils.isBlank(content))
            return ResultBean.error("评论不能为空");
        if (content.length()>800)
            return ResultBean.error("评论长度超限");
        Comment comment = new Comment(objectType, Long.parseLong(objectNo), user.getUid(), content);
        //设置主键
        comment.setSnowFlakeId(idWorker.snowId());
        int insert = commentMapper.insert(comment);
        if (insert>0){
            redis.hincr(objectType+":num:"+objectNo,"commentNum",1);
            return ResultBean.success("评论发表成功");
        }
        return ResultBean.error("评论发表失败");
    }

    @Override
    public PageBean getComment(Map paramMap) {
        log.info("接收参数:"+paramMap);
        if (paramMap==null||paramMap.isEmpty())
            return PageBean.error();
        Integer currentPage = (Integer) paramMap.get("currentPage");
        Integer size = (Integer) paramMap.get("size");
        String objectType = (String) paramMap.get("objectType");
        String objectNo = (String) paramMap.get("objectNo");
        if (currentPage==null||!CommentType.list().contains(objectType)||objectNo==null)
            return PageBean.error();
        size = (size==null||size>50||size<1)?50:size;
        IPage<Comment> iPage = new Page<>(currentPage, size);
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<Comment>()
                .eq("object_type", objectType)
                .eq("object_no", objectNo)
                .orderByDesc("create_time");
        IPage<Comment> commentIPage = commentMapper.selectPage(iPage, queryWrapper);
        List<Comment> comments = commentIPage.getRecords();
        //遍历评论列表，获取发表评论者的用户名和头像
        for (Comment comment:comments){
            User user = userInfoService.getUserByUid(comment.getUid());
            if (user!=null){
                comment.setUsername(user.getUsername());
                comment.setAvatar(user.getAvatar());
                comment.setUserLevel(UserLevelEnum.getLevel(user.getGrowth()));
            }
        }
        return new PageBean(commentIPage);
    }
}
