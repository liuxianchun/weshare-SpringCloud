package com.lxc.user.schedule;

import com.lxc.common.constant.TimeConst;
import com.lxc.common.utils.RedisUtil;
import com.lxc.user.mq.MQMessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * @author liuxianchun
 * @date 2021/8/8
 * 定时同步浏览量数据
 */
@Component
@Slf4j
public class NumSchedule {

    @Autowired
    private RedisUtil redis;

    @Autowired
    private MQMessageSender mqMessageSender;

    @Scheduled(cron = "0 0/10 * * * ?")   //每10分钟执行一次,同步浏览量数据
    public void syncNums(){
        if (!redis.hasKey("lock:syncNums:video")) {
            long time1 = System.currentTimeMillis();
            //加锁,防止多个服务同时遍历redis
            boolean setSuccess = redis.setNX("lock:syncNums:video", "lock", 8 * TimeConst.MINUTE);
            if (setSuccess){
                Set<String> keys = redis.keys("temp:count:video:*");
                for (String key:keys){
                    String objectNo = key.substring(17);
                    Map nums = redis.hmget("video:num:" + objectNo);
                    mqMessageSender.sendAddNum("video",objectNo, nums);
                }
                long time2 = System.currentTimeMillis();
                log.info("【同步视频浏览量数据】消息发送{}条,用时{}s",keys.size(),(time2-time1)/1000.0);
            }
        }
        if (!redis.hasKey("lock:syncNums:article")) {
            long time1 = System.currentTimeMillis();
            //加锁,防止多个服务同时遍历redis
            boolean setSuccess = redis.setNX("lock:syncNums:article", "lock", 8 * TimeConst.MINUTE);
            if (setSuccess){
                Set<String> keys = redis.keys("temp:count:article:*");
                for (String key:keys){
                    String objectNo = key.substring(19);
                    Map nums = redis.hmget("article:num:" + objectNo);
                    mqMessageSender.sendAddNum("article",objectNo, nums);
                }
                long time2 = System.currentTimeMillis();
                log.info("【同步文章浏览量数据】消息发送{}条,用时{}s",keys.size(),(time2-time1)/1000.0);
            }
        }
        if (!redis.hasKey("lock:syncNums:file")) {
            long time1 = System.currentTimeMillis();
            //加锁,防止多个服务同时遍历redis
            boolean setSuccess = redis.setNX("lock:syncNums:file", "lock", 8 * TimeConst.MINUTE);
            if (setSuccess){
                Set<String> keys = redis.keys("temp:count:file:*");
                for (String key:keys){
                    String objectNo = key.substring(16);
                    Map nums = redis.hmget("file:num:" + objectNo);
                    mqMessageSender.sendAddNum("file",objectNo, nums);
                }
                long time2 = System.currentTimeMillis();
                log.info("【同步文件浏览量数据】消息发送{}条,用时{}s",keys.size(),(time2-time1)/1000.0);
            }
        }
    }

}
