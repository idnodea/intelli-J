package com.shop.repository;

import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.shop.dto.MainItemDto;

//page 269 사용자 정의 인터페이스
public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    //상품조회조건을 담고 있는 itemSearchDto객체와 페이징 정보를 담고있는
    //pageable객체를 파라미터로 받는 getAdminItemPage메소드를 정의
    //반환데이터로 Page<Item>객체를 반환
    
    //page 284 메인페이지에 보여줄 상품리스트를 가져오는 메소드
    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

}