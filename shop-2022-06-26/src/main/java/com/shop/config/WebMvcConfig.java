package com.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("file:///D://_dev//inteli//shop-2022-06-26//src//main//resources//static//images//item")
    String uploadPath;
    //어플로케이션프로퍼티에 설정한 uploadPath프로퍼티값을 읽어온다
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/images/**")
                //웹 브라우저에 입력하는 url에 /images로 시작하는 경우 uploadPath에
                //설정한 폴더를 기준으로 파일을 읽어오도록 설정
//                .addResourceLocations(uploadPath);
                //로컬컴퓨터에 저장된 파일을 읽어올 root경로를 설정

        registry.addResourceHandler("/**")
                .addResourceLocations("file:src/main/resources/static/")
                .addResourceLocations(uploadPath);
    }



}