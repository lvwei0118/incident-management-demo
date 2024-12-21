package com.incident.management.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfg implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //设置允许跨域的路径
        registry.addMapping("/**")
            //设置允许跨域请求的域名
            .allowedOrigins("http://localhost:8080", "http://127.0.0.1:8080","http://127.0.0.1:8000","http://localhost:8000","http://localhost:3000", "http://127.0.0.1:3000")
            //是否允许证书 不再默认开启
            .allowCredentials(true)
            //设置允许的方法
            .allowedMethods("*")
            //跨域允许时间
            .maxAge(3600);
    }
}
