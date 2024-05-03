package com.shop.repository;

import com.shop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

//엔티티 cart를 작성 후,
public interface CartRepository extends JpaRepository<Cart, Long> {

    //page334 현재 로그인한 회원의  Cart엔티티를 찾기 위해 
    //CartRepository에 쿼리 메소드를 추가
    Cart findByMemberId(Long memberId);

}