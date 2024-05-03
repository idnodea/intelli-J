package com.shop.dto;

import com.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

//page311 주문이력조회하기
//주문부터 현재상품의 배송상태. 상품 출발 도착 여부. 반품 교환 주문취소 등
//주문취소기능만 일단 구현

//조회한 주문 데이터를 화면에 보낼 때 사용할 DTO클래스를 생성. 주문 상품 정보를 담을 OrderItemDto클래스생성
@Getter @Setter
public class OrderItemDto {

    public OrderItemDto(OrderItem orderItem, String imgUrl){
        //OrderItemDto클래스의 생성자, orderItem객체와 이미지 경로를 파라미터로 받아, 멤버변수값을세팅

        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }

    private String itemNm; //상품명
    private int count; //주문 수량

    private int orderPrice; //주문 금액
    private String imgUrl; //상품 이미지 경로

}