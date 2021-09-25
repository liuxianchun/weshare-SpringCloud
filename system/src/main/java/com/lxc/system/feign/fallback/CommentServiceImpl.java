package com.lxc.system.feign.fallback;

import com.lxc.common.entity.PageBean;
import com.lxc.common.entity.ResultBean;
import com.lxc.system.feign.api.CommentService;

import java.util.Map;

/**
 * @author liuxianchun
 * @date 2021/7/25
 */
public class CommentServiceImpl implements CommentService {
    @Override
    public ResultBean addComment(Map paramMap) {
        return null;
    }

    @Override
    public PageBean getComment(Map paramMap) {
        return null;
    }
}
