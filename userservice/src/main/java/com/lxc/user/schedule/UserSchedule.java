package com.lxc.user.schedule;

import cn.hutool.core.date.DateUtil;
import com.lxc.common.constant.NumConst;
import com.lxc.common.constant.TimeConst;
import com.lxc.common.entity.user.User;
import com.lxc.common.utils.RedisUtil;
import com.lxc.user.config.IdWorkerConfig;
import com.lxc.user.dao.LoginHistoryMapper;
import com.lxc.user.dao.UserMapper;
import com.lxc.user.mq.MQMessageSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Time;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liuxianchun
 * @date 2021/9/8
 */
@Slf4j
@Component
public class UserSchedule {

    @Resource
    private UserMapper userMapper;
    @Resource
    private LoginHistoryMapper loginHistoryMapper;

    @Autowired
    private RedisUtil redis;
    @Autowired
    private MQMessageSender mqMessageSender;

    private static Integer count = NumConst.ZERO;
    private static Integer redisNum = NumConst.ZERO;

    //每天0:00执行一次,增加用户硬币数
    @Scheduled(cron = "0 0 0 * * ?")
    public void dailyAddCoin(){
        int update = userMapper.dailyAddCoin();
        log.info("定时任务【每天增加用户硬币】执行结束,用户数:{}",update);
    }

    //每天2:00执行一次,删除超过3个月的登录记录
    @Scheduled(cron = "0 0 2 * * ?")
    public void dailyDeleteLoginHistory(){
        int delete = loginHistoryMapper.dailyDeleteLoginHistory();
        log.info("定时任务【删除三个月前的登录记录】执行结束,删除记录数:{}",delete);
    }

    //每天9:00执行一次，发送生日祝福邮件
    @Scheduled(cron = "0 0 9 * * ?")
    @Transactional
    public void sendBirthdayEmail(){
        Date today = new Date();
        String todayStr = DateUtil.format(today, "yyyyMMdd");
        //从数据库读取获取用户数据的锁
        String lock = "lock:user:birthdayEmail:"+todayStr;
        //已发送的生日祝福的用户数的key
        String key = "count:birthdayEmail:"+todayStr;
        Long time1 = System.currentTimeMillis();
        Long time2 = System.currentTimeMillis();
        //初始化及设置key的有效期
        redis.setNX(key,NumConst.ZERO,TimeConst.DAY);
        while((time2-time1)/1000<3*TimeConst.HOUR){
            boolean success = redis.setNX(lock, IdWorkerConfig.IDCard, 5 * TimeConst.MINUTE);
            //获取锁成功，读取数据发送邮件
            if (success){
                log.info("定时任务【发送生日祝福邮件】启动,执行服务:{}",IdWorkerConfig.IDCard);
                Cursor<User> cursor = userMapper.selectBirthdayUser(today);
                redisNum = (Integer) redis.get(key);
                count = NumConst.ZERO;
                //流式查询遍历
                cursor.forEach(user -> {
                    //未发送邮件的用户
                    if (redisNum==null||count<=redisNum){
                        //发送生日祝福
                        String title = "【We Share】生日祝福";
                        String receiver = user.getAccount();
                        String content = "Hi,"+user.getUsername()+ "!有些事情可能你已经忘记，但我们依然记得。" +
                                "今天是你的生日,祝你生日快乐,事业成功,爱情甜蜜,生活幸福,天天快乐,事事开心！";
                        mqMessageSender.sendEmail(receiver,title,content);
                        redis.incr(key,NumConst.ONE);
                        redis.setNX(lock, IdWorkerConfig.IDCard, 5*TimeConst.MINUTE);
                    }
                    count++;
                });
                log.info("定时任务【发送生日祝福邮件】结束，共发送邮件{}封",count);
                redis.setNX(lock, IdWorkerConfig.IDCard, TimeConst.DAY);
                break;
            }else{
                try {
                    TimeUnit.MINUTES.sleep(5);
                } catch (InterruptedException e) {
                    log.error("线程休眠失败");
                }
            }
            time2 = System.currentTimeMillis();
        }
    }

}
