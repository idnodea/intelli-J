package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

//장바구니 조회페이지에 전달할 DTO클래스생성.
//JPQL로 쿼리 작성 시 생성자를 이용해서 DTO로 바로 반환
@Getter @Setter
public class CartDetailDto {

    private Long cartItemId; //장바구니 상품 아이디

    private String itemNm; //상품명

    private int price; //상품 금액

    private int count; //수량

    private String imgUrl; //상품 이미지 경로

    //장바구니 페이지에 전달할 테이터를 생성자의 파라미터로 만들어준다
    public CartDetailDto(Long cartItemId, String itemNm, int price, int count, String imgUrl){
        this.cartItemId = cartItemId;
        this.itemNm = itemNm;
        this.price = price;
        this.count = count;
        this.imgUrl = imgUrl;
    }

}