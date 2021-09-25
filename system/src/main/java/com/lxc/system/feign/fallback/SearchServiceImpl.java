package com.lxc.system.feign.fallback;

import com.lxc.common.entity.PageBean;
import com.lxc.common.entity.ResultBean;
import com.lxc.system.feign.api.SearchService;
import org.springframework.stereotype.Service;

/**
 * @author liuxianchun
 * @date 2021/7/21
 */
@Service
public class SearchServiceImpl implements SearchService {
    @Override
    public ResultBean getHotWord(String keyword) {
        return null;
    }

    @Override
    public ResultBean getTop10() {
        return null;
    }

    @Override
    public PageBean searchVideo(String keyword, Integer currentPage, Integer size) {
        return null;
    }

    @Override
    public PageBean searchArticle(String keyword, Integer currentPage, Integer size) {
        return null;
    }

    @Override
    public PageBean searchFile(String keyword, Integer currentPage, Integer size) {
        return null;
    }
}
