package com.lxc.user.config;

import cn.hutool.core.date.DateUtil;
import com.lxc.common.constant.TimeConst;
import com.lxc.common.utils.IdWorker;
import com.lxc.common.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuxianchun
 * @date 2021/6/24
 */
@Configuration
public class IdWorkerConfig {
    //工作机器id
    @Value("${weshare.workerId}")
    private int workerId;

    //序列号
    @Value("${weshare.datacenterId}")
    private int datacenterId;

    @Autowired
    private RedisUtil redis;

    public static String IDCard = "none";

    @Bean
    public IdWorker IdWorker(){
        IDCard = "("+datacenterId+","+workerId+")";
        boolean success = redis.setNX( "cluster:file-service:"+IDCard, DateUtil.now(), 5 * TimeConst.SECOND);
        if (!success){
            throw new RuntimeException("启动失败,服务id冲突,IDCard="+IDCard);
        }
        return new IdWorker(workerId,datacenterId);
    }

}
