package com.lxc.user.service.api;

import com.lxc.common.entity.PageBean;
import com.lxc.common.entity.ResultBean;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author liuxianchun
 * @date 2021/7/25
 */
public interface CommentService {

    ResultBean addComment(Map paramMap);

    PageBean getComment(Map paramMap);
}
