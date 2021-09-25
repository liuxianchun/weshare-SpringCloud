package com.lxc.user.service.impl;

import com.lxc.user.service.api.MailService;
import com.lxc.user.util.VerifyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @Author: liuxianchun
 * @Date: 2021/05/26
 * @Description:
 */
@Service
@Slf4j
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    private static final String register_title = "【验证码】We Share注册邮箱验证码";
    private static final String register_content = "欢迎注册We Share账号，本验证码有效时间为30分钟，您本次的验证码为：";

    /**
     * 发送注册验证码
     * @param receiver
     */
    @Override
    public String sendMailCode(String receiver) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);   // 邮件发送者
        message.setTo(receiver);   // 邮件接收者
        message.setSubject(register_title);  // 主题
        String mailCode = VerifyUtil.createMailCode();
        message.setText(register_content + mailCode);  // 内容
        try{
            mailSender.send(message);
        }catch (Exception e){
            log.error("邮件发送失败:"+e.getCause().toString());
            return "error";
        }
        return mailCode;
    }

    /**
     * 发送系统邮件消息
     * @param receiver
     */
    @Override
    public void sendEmailMessage(String receiver,String title,String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);   // 邮件发送者
        message.setTo(receiver);   // 邮件接收者
        message.setSubject(title);  // 主题
        message.setText(content);  // 内容
        try{
            mailSender.send(message);
        }catch (Exception e){
            log.error("邮件发送失败:"+e.getCause().toString());
        }
    }

}
