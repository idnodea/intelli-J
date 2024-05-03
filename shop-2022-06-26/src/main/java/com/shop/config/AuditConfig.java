package com.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//page 223 어디팅기능을 사용하기 위해, Config파일 생성
@Configuration
@EnableJpaAuditing //JPA의 Auditing기능을 활성화
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        //등록자와 수정자를 처리해주는  AuditingAware를 빈으로 등록
        return new AuditorAwareImpl();
    }

}