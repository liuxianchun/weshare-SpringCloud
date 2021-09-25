package com.lxc.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author liuxianchun
 * @date 2021/6/25
 */
@EnableDiscoveryClient
@SpringBootApplication
public class GateWay {
    public static void main(String[] args) {
        SpringApplication.run(GateWay.class,args);
    }
}
