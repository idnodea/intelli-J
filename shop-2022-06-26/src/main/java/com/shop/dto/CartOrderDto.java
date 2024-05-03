package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

//page363 장바구니 목록 중 체크박스가 선택된 상품을 주문하는 로직을 작성
//장바구니에서 주문을 하면 기준 주문 로직과의 차이점은 여러 개의 상품을 하나의 주문에
//담을 수 있다는 점과 주문한 상품은 장바구니에서 삭제해야한다는 점

//장바구니 페이지에서 주문할 상품 데이터를 전달할 DTO를 생성
@Getter
@Setter
public class CartOrderDto {

    private Long cartItemId;

    private List<CartOrderDto> cartOrderDtoList;
    //장바구니에서 여러 개의 상품을 주문하므로 CartOrderDto클래스가 자기자신을 
    //List로 가지고 있도록 만든다

}