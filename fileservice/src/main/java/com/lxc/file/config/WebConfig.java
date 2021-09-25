package com.lxc.file.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: liuxianchun
 * @Date: 2021/05/10
 * @Description:
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    //磁盘文件存储路径
    @Value("${weshare.upload-dir}")
    private String uploadDir;

    //访问文件路径
    @Value("${weshare.file-dir}")
    private String fileDir;

    // 配置server虚拟路径，handler为前台访问的目录，locations为files相对应的本地路径
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(fileDir).addResourceLocations("file:"+uploadDir);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET","HEAD","POST","PUT","DELETE","OPTIONS")
                .allowCredentials(true)    //允许跨域
                .maxAge(3600)
                .allowedHeaders("*");
    }

}
