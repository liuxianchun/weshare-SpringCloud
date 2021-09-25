package com.lxc.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author liuxianchun
 * @date 2021/5/23
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@ServletComponentScan
public class System {

    public static void main(String[] args) {
        SpringApplication.run(System.class,args);
    }

}
