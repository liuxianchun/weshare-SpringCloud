package com.lxc.user.mq;

import com.lxc.common.constant.MQConst;
import com.lxc.common.entity.user.CommentType;
import com.lxc.user.dao.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxianchun
 * @date 2021/8/8
 */
@Component
@Slf4j
public class MQMessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Resource
    private UserMapper userMapper;

    //消息发送成功
    private RabbitTemplate.ConfirmCallback confirmCallback = (correlationData, ack, cause) -> {
        if(ack){  //客户端确认
            //log.info("消息发送成功，消息id："+ (correlationData != null ? correlationData.getId() : null));
        }else{   //客户端未确认
            log.info("消息确认失败");
        }
    };

    //消息发送失败的回调
    private RabbitTemplate.ReturnCallback returnsCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(org.springframework.amqp.core.Message message, int replyCode, String replyText, String exchange, String routingKey) {
            log.info("消息发送失败{replyCode:"+replyCode+",exchange:"+exchange+
                    ",routingKey:"+routingKey+",replyText:"+replyText+"}");
        }

    };

    /**
     * 发送增加浏览数据消息
     * @param objectType
     * @param objectNo
     * @param nums
     */
    public void sendAddNum(String objectType, String objectNo, Map nums){
        if (!CommentType.list().contains(objectType)||StringUtils.isEmpty(objectNo)||nums==null)
            return;
        Map<String,Object> props = new HashMap<>();
        props.put("objectType",objectType);
        props.put("objectNo",objectNo);
        MessageHeaders headers = new MessageHeaders(props);
        Message message = MessageBuilder.createMessage(nums, headers);
        CorrelationData correlationData = new CorrelationData();
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnsCallback);
        rabbitTemplate.convertAndSend(MQConst.ADD_NUM_EXCHANGE,MQConst.ADD_NUM_KEY,message,correlationData);
    }

    /**
     * 发送删除文件消息
     * @param relativePath
     */
    public void sendDeleteFile(String relativePath){
        if (StringUtils.isEmpty(relativePath))
            return;
        MessageHeaders headers = new MessageHeaders(null);
        Message message = MessageBuilder.createMessage(relativePath, headers);
        CorrelationData correlationData = new CorrelationData();
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnsCallback);
        rabbitTemplate.convertAndSend(MQConst.DELETE_FILE_EXCHANGE,MQConst.DELETE_FILE_KEY,message,correlationData);
    }

    /**
     * 根据autherId增加经验和硬币
     * @param autherId
     * @param coin
     * @param scoin
     * @param growth
     */
    public void sendUserExp(Integer autherId,Integer coin,Integer scoin,Integer growth){
        Map<String, Object> props = new HashMap<>();
        props.put("uid",autherId);
        props.put("coin",coin);
        props.put("scoin",scoin);
        props.put("growth",growth);
        MessageHeaders headers = new MessageHeaders(null);
        Message message = MessageBuilder.createMessage(props, headers);
        CorrelationData correlationData = new CorrelationData();
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnsCallback);
        rabbitTemplate.convertAndSend(MQConst.USER_EXP_EXCHANGE,MQConst.USER_EXP_KEY,message,correlationData);
    }

    /**
     * 发送邮件消息
     * @param receiver,message
     */
    public void sendEmail(String receiver,String title,String content){
        if (StringUtils.isEmpty(receiver)||StringUtils.isEmpty(title)||StringUtils.isEmpty(content)){
            log.warn("参数缺失，发送邮件失败");
            return;
        }
        MessageHeaders headers = new MessageHeaders(null);
        Map<String, Object> body = new HashMap<>();
        body.put("receiver",receiver);
        body.put("title",title);
        body.put("content",content);
        Message message = MessageBuilder.createMessage(body, headers);
        CorrelationData correlationData = new CorrelationData();
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnsCallback);
        rabbitTemplate.convertAndSend(MQConst.SEND_EMAIL_EXCHANGE,MQConst.SEND_EMAIL_KEY,message,correlationData);
    }

}
