package com.lxc.management.config;

import com.lxc.management.interceptor.IPInterceptor;
import com.lxc.management.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author liuxianchun
 * @date 2021/6/19
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    //将拦截器注册为一个 Bean ,从而可以在拦截器内使用Autowired
    @Bean
    public IPInterceptor ipInterceptor(){
        return new IPInterceptor();
    }

    @Bean
    public LoginInterceptor loginInterceptor(){
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ipInterceptor())
                .addPathPatterns("/*");
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/resources/*")
                .addPathPatterns("/user/*")
                .excludePathPatterns("/user/login","/user/all");
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
