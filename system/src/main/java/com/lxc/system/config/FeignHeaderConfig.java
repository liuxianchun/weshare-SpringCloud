package com.lxc.system.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @Author: liuxianchun
 * @Date: 2021/05/28
 * @Description: 解决feign调用时header丢失的问题
 */
@Configuration
public class FeignHeaderConfig implements RequestInterceptor {

    @Bean
    public RequestInterceptor requestInterceptor(){
        return requestTemplate -> {
            apply(requestTemplate);
        };
    }

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String values = request.getHeader(name);
                // 跳过 content-length
                // 拦截器复制请求头，复制的时候是所有头都复制的,可能导致Content-length长度跟body不一致
                if (name.equals("content-length")){
                    continue;
                }
                template.header(name, values);
            }
        }
       /* Enumeration<String> bodyNames = request.getParameterNames();
        StringBuffer body =new StringBuffer();
        if (bodyNames != null) {
            while (bodyNames.hasMoreElements()) {
                String name = bodyNames.nextElement();
                String values = request.getParameter(name);
                body.append(name).append("=").append(values).append("&");
            }
        }
        if(body.length()!=0) {
            body.deleteCharAt(body.length()-1);
            template.body(body.toString());
        }*/
    }
}