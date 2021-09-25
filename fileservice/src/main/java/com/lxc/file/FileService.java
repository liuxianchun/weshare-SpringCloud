package com.lxc.file;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author liuxianchun
 * @date 2021/5/23
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.lxc.file.dao")
@EnableScheduling
public class FileService {
    public static void main(String[] args) {
        SpringApplication.run(FileService.class,args);
    }
}
