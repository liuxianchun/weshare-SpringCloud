package com.lxc.file.service.api;

import com.lxc.common.entity.PageBean;
import com.lxc.common.entity.ResultBean;

import java.util.List;

/**
 * @Author: liuxianchun
 * @Date: 2021/07/19
 * @Description:
 */
public interface SearchService {

    PageBean search(String keyword, Integer currentPage, Integer size,Class<?> cls);

    /**
     * 获取点赞数top10的投稿
     * @return
     */
    ResultBean getTop10();

    List searchTop10List(Class<?> clazz);

    ResultBean getHotWord(String keyword);

}
