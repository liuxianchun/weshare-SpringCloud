package com.lxc.user.config;

import com.lxc.common.constant.MQConst;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuxianchun
 * @date 2021/3/7
 */
@Configuration
public class RabbitMQConfig {

    //创建初始化RabbitAdmin对象
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        // 只有设置为 true，spring 才会加载 RabbitAdmin 这个类
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    //增加数据队列
    @Bean
    public Queue addNumQueue(){
        return new Queue(MQConst.ADD_NUM_QUEUE,true,false,false,MQConst.props);
    }
    //增加数据交换机
    @Bean
    public Exchange addNumExchange(){
        return new DirectExchange(MQConst.ADD_NUM_EXCHANGE, true, false, null);
    }
    //绑定增加数据队列
    @Bean
    public Binding bindingAddNum(){
        return BindingBuilder
                .bind(addNumQueue())
                .to(addNumExchange())
                .with(MQConst.ADD_NUM_KEY)
                .and(null);
    }

    //删除文件队列
    @Bean
    public Queue deleteFileQueue(){
        return new Queue(MQConst.DELETE_FILE_QUEUE,true,false,false,MQConst.props);
    }

    //删除文件交换机
    @Bean
    public Exchange deleteFileExchange(){
        return new DirectExchange(MQConst.DELETE_FILE_EXCHANGE, true, false, null);
    }
    //绑定删除文件队列
    @Bean
    public Binding bindingDeleteFile(){
        return BindingBuilder
                .bind(deleteFileQueue())
                .to(deleteFileExchange())
                .with(MQConst.DELETE_FILE_KEY)
                .and(null);
    }

    //用户经验队列
    @Bean
    public Queue userExpQueue(){
        return new Queue(MQConst.USER_EXP_QUEUE,true,false,false,MQConst.props);
    }
    //用户经验交换机
    @Bean
    public Exchange userExpExchange(){
        return new DirectExchange(MQConst.USER_EXP_EXCHANGE, true, false, null);
    }
    //绑定用户经验队列
    @Bean
    public Binding bindingUserExp(){
        return BindingBuilder
                .bind(userExpQueue())
                .to(userExpExchange())
                .with(MQConst.USER_EXP_KEY)
                .and(null);
    }

    //发送邮件队列
    @Bean
    public Queue sendEmailQueue(){
        return new Queue(MQConst.SEND_EMAIL_QUEUE,true,false,false,MQConst.props);
    }
    //发送邮件交换机
    @Bean
    public Exchange sendEmailExchange(){
        return new DirectExchange(MQConst.SEND_EMAIL_EXCHANGE, true, false, null);
    }
    //绑定发送邮件队列
    @Bean
    public Binding bindingSendEmail(){
        return BindingBuilder
                .bind(sendEmailQueue())
                .to(sendEmailExchange())
                .with(MQConst.SEND_EMAIL_KEY)
                .and(null);
    }

}
