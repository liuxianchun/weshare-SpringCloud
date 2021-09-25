package com.lxc.user.schedule;

import cn.hutool.core.date.DateUtil;
import com.lxc.common.constant.TimeConst;
import com.lxc.common.utils.RedisUtil;
import com.lxc.user.config.IdWorkerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author liuxianchun
 * @date 2021/9/17
 */
@Component
@Slf4j
public class ClusterSchedule {

    @Autowired
    private RedisUtil redis;

    //维持心跳
    @Scheduled(cron = "0/3 * * * * ?")   //每3秒执行一次
    public void keepHeart() {
        redis.set("cluster:user-service:" + IdWorkerConfig.IDCard, DateUtil.now(),5 * TimeConst.SECOND);
    }

}
