package com.lxc.file.mq;

import cn.hutool.core.io.FileUtil;
import com.lxc.common.constant.MQConst;
import com.lxc.common.entity.user.CommentType;
import com.lxc.file.pojo.Article;
import com.lxc.file.pojo.FileResource;
import com.lxc.file.pojo.Video;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQueryBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

/**
 * @author liuxianchun
 * @date 2021/8/8
 */
@Component
@Slf4j
public class MessageReceiver {

    @Autowired
    private ElasticsearchRestTemplate esTemplate;

    @Value("${weshare.upload-dir}")
    private String uploadDir;

    /*增加浏览数据队列*/
    @RabbitListener(queues = {MQConst.ADD_NUM_QUEUE})
    @RabbitHandler
    public void addNum(Message msg, Channel channel){
        try {
            channel.basicQos(0,1000,true);
            Map nums = (Map)msg.getPayload();
            log.info("接收到【增加浏览数据】消息:"+msg.getHeaders()+","+nums);
            long deliveryTag = (long) msg.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
            String objectType = (String) msg.getHeaders().get("objectType");
            String objectNo = (String) msg.getHeaders().get("objectNo");
            if (StringUtils.isEmpty(objectType)||StringUtils.isEmpty(objectNo))
                return;
            if (!nums.isEmpty())
                addNumsUpdateES(objectType, objectNo, nums);
            //确认消费消息
            channel.basicAck(deliveryTag,false);
        } catch (IOException e) {
            log.error("消费【增加浏览数据】消息异常:"+e);
        }
    }

    /*删除文件队列*/
    @RabbitListener(queues = {MQConst.DELETE_FILE_QUEUE})
    @RabbitHandler
    public void deleteFile(Message msg, Channel channel){
        try {
            //消息逐条消费
            channel.basicQos(0,1,true);
            String relativePath = (String)msg.getPayload();
            log.info("接收到【删除文件】消息,文件相对路径:"+relativePath);
            FileUtil.del(Path.of(uploadDir+relativePath));
            long deliveryTag = (long) msg.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
            //确认消费消息
            channel.basicAck(deliveryTag,false);
        } catch (IOException e) {
            log.error("消费【删除文件】消息异常:"+e);
        }
    }

    //更新es数据
    private void addNumsUpdateES(String objectType,String snowFlakeId,Map nums){
        Class clazz = null;
        if (CommentType.VIDEO.equals(objectType)){
            clazz = Video.class;
        }else
        if (CommentType.ARTICLE.equals(objectType)){
            clazz = Article.class;
        }else
        if (CommentType.FILE.equals(objectType)){
            clazz = FileResource.class;
        }else
            return;
        //更新es
        UpdateQuery updateQuery = new UpdateQueryBuilder()
                .withId(snowFlakeId)
                .withClass(clazz)
                .withUpdateRequest(new UpdateRequest().doc(nums))
                .build();
        UpdateResponse update = esTemplate.update(updateQuery);
        if (update==null)
            log.warn("elasticsearch增加浏览数据更新失败,objectType={},objectNo={}",objectType,snowFlakeId);
    }

}
