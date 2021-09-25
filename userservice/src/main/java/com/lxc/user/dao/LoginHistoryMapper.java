package com.lxc.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxc.user.po.LoginHistory;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author liuxianchun
 * @date 2021/5/29
 */
@Mapper
public interface LoginHistoryMapper extends BaseMapper<LoginHistory> {

    /**
     * 删除三个月前的登录记录
     * @return
     */
    int dailyDeleteLoginHistory();

}
