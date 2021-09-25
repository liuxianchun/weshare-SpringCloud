package com.lxc.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author liuxianchun
 * @date 2021/6/19
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class Management {
    public static void main(String[] args) {
        SpringApplication.run(Management.class,args);
    }
}
