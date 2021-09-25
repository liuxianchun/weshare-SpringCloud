package com.lxc.user.util;

import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: liuxianchun
 * @Date: 2021/05/08
 * @Description:
 */
public class HttpUtils {
    public static Object post(String url, MultiValueMap<String, Object> map){
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        //将请求头部和参数合成一个请求
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        //执行HTTP请求，将返回
        return client.postForObject(url,requestEntity, String.class);
    }
}
