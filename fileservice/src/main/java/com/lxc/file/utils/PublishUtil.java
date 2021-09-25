package com.lxc.file.utils;

import cn.hutool.core.date.DateUtil;
import com.lxc.common.constant.TimeConst;
import com.lxc.common.entity.ResultBean;
import com.lxc.common.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author: liuxianchun
 * @Date: 2021/08/30
 * @Description: 投稿工具类
 */
@Service
public class PublishUtil {

    @Autowired
    private RedisUtil redis;

    /**
     * 服务器资源有限，一天只能发布5个投稿
     * @param uid
     * @return
     */
    public ResultBean canPublish(Integer uid){
        if (uid==null)
            return ResultBean.error("用户id为空");
        String today = DateUtil.format(new Date(), "yyyyMMdd");
        String key = "lock:publishLimit:"+today+":"+uid;
        Long times = redis.incr(key, 1, TimeConst.DAY);
        if (times>5)
            return ResultBean.error("资源有限,每天最多发布5个投稿");
        return ResultBean.success("允许投稿");
    }

}
