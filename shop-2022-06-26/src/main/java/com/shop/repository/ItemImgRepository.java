package com.shop.repository;

import com.shop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

//page 246 상품의 이미지 정보를 저장하기 위해서 repository 패키지 아래에
//JPARepository를 상속받는 ItemImgRepository인터페이스를 만든다

//page314 상품의 대표이미지를 찾는 쿼리 메소드 추가 
//구매이력페이지에서 주문상품의 대표 이미지를 보여주기 위해 추가,-> 이후 오더서비스클래스수정
public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    //page250 매개변수로 넘겨준 상품 아이디를 가지며,
    //상품 이미지 아이디의 오름차순으로 가져오는 쿼리 메소드
    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

    ItemImg findByItemIdAndRepimgYn(Long itemId, String repimgYn);

}