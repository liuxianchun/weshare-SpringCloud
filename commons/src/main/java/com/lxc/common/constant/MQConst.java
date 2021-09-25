package com.lxc.common.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxianchun
 * @date 2021/8/8
 */
public class MQConst {

    /**
     * MQ通用配置
     */
    public static final Map<String,Object> props = new HashMap<>(){{
        put("x-message-ttl",30000);  //消息存活30s
        put("x-max-length",10000);   //队列最大长度
    }};

    /**
     * 增加浏览数据队列
     */
    public static final String ADD_NUM_QUEUE = "add_num_queue";
    /**
     * 增加浏览数据交换机
     */
    public static final String ADD_NUM_EXCHANGE = "add_num_exchange";
    /**
     * 增加浏览数据routingKey
     */
    public static final String ADD_NUM_KEY = "addNum";

    /**
     * 删除文件队列
     */
    public static final String DELETE_FILE_QUEUE = "delete_file_queue";
    /**
     * 删除文件交换机
     */
    public static final String DELETE_FILE_EXCHANGE = "delete_file_exchange";
    /**
     * 删除文件routingKey
     */
    public static final String DELETE_FILE_KEY = "deleteFile";

    /**
     * 用户经验队列
     */
    public static final String USER_EXP_QUEUE = "user_exp_queue";
    /**
     * 用户经验交换机
     */
    public static final String USER_EXP_EXCHANGE = "user_exp_exchange";
    /**
     * 用户经验队列routingKey
     */
    public static final String USER_EXP_KEY = "userExp";

    /**
     * 发送邮件队列
     */
    public static final String SEND_EMAIL_QUEUE = "send_email_queue";
    /**
     * 发送邮件交换机
     */
    public static final String SEND_EMAIL_EXCHANGE = "send_email_exchange";
    /**
     * 发送邮件队列routingKey
     */
    public static final String SEND_EMAIL_KEY = "sendEmail";

}
