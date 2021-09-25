package com.lxc.user.mq;

import com.lxc.common.constant.MQConst;
import com.lxc.user.service.api.MailService;
import com.lxc.user.service.api.UserInfoService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author liuxianchun
 * @date 2021/9/8
 */
@Slf4j
@Component
public class MQMessageReceiver {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private MailService mailService;

    /*增加浏览数据队列*/
    @RabbitListener(queues = {MQConst.USER_EXP_QUEUE})
    @RabbitHandler
    public void addCoinExp(Message msg, Channel channel){
        try {
            channel.basicQos(0,1000,true);
            Map<String,Object> props = (Map<String, Object>) msg.getPayload();
            log.info("接收到【增加用户硬币经验】消息:{}",msg);
            Integer uid = (Integer) props.get("uid");
            Integer coin = (Integer) props.get("coin");
            Integer scoin = (Integer) props.get("scoin");
            Integer growth = (Integer) props.get("growth");
            userInfoService.addCoinExp(uid,coin,scoin,growth);
            long deliveryTag = (long) msg.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
            //确认消费消息
            channel.basicAck(deliveryTag,false);
        } catch (IOException e) {
            log.error("消费【增加用户硬币经验】消息异常:"+e.getMessage());
        }
    }

    /*发送邮件队列*/
    @RabbitListener(queues = {MQConst.SEND_EMAIL_QUEUE})
    @RabbitHandler
    public void sendEmail(Message msg, Channel channel){
        try {
            channel.basicQos(0,1000,true);
            Map body = (Map) msg.getPayload();
            String receiver = (String) body.get("receiver");
            String title = (String) body.get("title");
            String content = (String) body.get("content");
            log.info("接收到【发送邮件】消息,接收人:{},标题:{}",receiver,title);
            mailService.sendEmailMessage(receiver,title,content);
            long deliveryTag = (long) msg.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
            //确认消费消息
            channel.basicAck(deliveryTag,false);
        } catch (IOException e) {
            log.error("消费【发送邮件】消息异常:"+e.getMessage());
        }
    }

}
