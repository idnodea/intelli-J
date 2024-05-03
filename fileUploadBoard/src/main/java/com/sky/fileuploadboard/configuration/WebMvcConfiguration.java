package com.sky.fileuploadboard.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.swing.*;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer{
	
	@Bean
	public CommonsMultipartResolver multipartResolver(){
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		commonsMultipartResolver.setDefaultEncoding("UTF-8");
		commonsMultipartResolver.setMaxUploadSizePerFile(5 * 1024 * 1024); //5 * 1024 * 1024 (5mb)
		return commonsMultipartResolver;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
		.addResourceLocations("file:src/main/resources/static/");

	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("main");
	}
//
//	위 코드는 Spring MVC 웹 애플리케이션의 설정을 담당하는 클래스입니다. 주요 기능은 다음과 같습니다:
//
//	파일 업로드 설정: CommonsMultipartResolver를 빈으로 등록하여 파일 업로드를 처리합니다. 이때, 업로드된 파일의 인코딩을 UTF-8로 설정하고, 파일당 최대 업로드 크기를 5MB로 제한합니다.
//	정적 리소스 핸들링: addResourceHandlers 메서드를 사용하여 정적 리소스에 대한 핸들러를 등록합니다. 이를 통해 /src/main/resources/static/ 경로에 있는 정적 리소스들에 접근할 수 있도록 설정합니다.
//	뷰 컨트롤러 등록: addViewControllers 메서드를 사용하여 특정 URL에 대한 뷰 컨트롤러를 등록합니다. 이 코드에서는 "/" 경로에 대한 요청이 들어왔을 때 "main" 뷰를 반환하도록 설정합니다.
//	이 코드는 Spring MVC 애플리케이션에서 파일 업로드, 정적 리소스 처리 및 뷰 관련 설정을 담당하고 있습니다.
}
