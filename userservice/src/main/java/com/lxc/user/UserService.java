package com.lxc.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.lxc.user.dao")
@EnableDiscoveryClient
@EnableScheduling
@EnableTransactionManagement
public class UserService {

    public static void main(String[] args) {
        SpringApplication.run(UserService.class, args);
    }

}
