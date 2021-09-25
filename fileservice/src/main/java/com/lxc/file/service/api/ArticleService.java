package com.lxc.file.service.api;

import com.lxc.common.entity.ResultBean;
import com.lxc.common.entity.user.User;

/**
 * @author liuxianchun
 * @date 2021/7/3
 */
public interface ArticleService {

    ResultBean publishArticle(Integer uid,String username, String title, String content);

}
