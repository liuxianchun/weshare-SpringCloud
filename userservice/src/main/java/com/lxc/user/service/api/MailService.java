package com.lxc.user.service.api;

/**
 * @Author: liuxianchun
 * @Date: 2021/05/26
 * @Description:
 */
public interface MailService {

    String sendMailCode(String receiver);

    void sendEmailMessage(String receiver,String title,String emailMsg);

}
