package com.shop.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;

//page222 AUditing를 이용한 엔티티 공통속성공통화
//스프링데이터JPA에서 어디팅기능을 제공. 엔티티저장수정될 때 자동으로
//등록일,수정일,등록자,수정자 입력해줌 . 이런 공통멤버변수들을 추상클래스로 만들고,
//해당 추상 클래스를 상속받는 형태로 인티티를 리팩토링
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "";
        if(authentication != null){
            userId = authentication.getName();
            //현재 로그인한 사용자의 정보를 조회하여 사용자의 이름을 등록자와 수정자로 지정
        }
        return Optional.of(userId);
    }

}