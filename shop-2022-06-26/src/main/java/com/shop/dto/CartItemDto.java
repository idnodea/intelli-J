package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

//page 332 상품상세페이지에서 장바구니에 담을 수량을 선택하고 
//장바구니 담기버튼을 클릭할 때 상품이 장바구니에 담기는 기능을 구현
@Getter @Setter
public class CartItemDto {

    @NotNull(message = "상품 아이디는 필수 입력 값 입니다.")
    private Long itemId;

    @Min(value = 1, message = "최소 1개 이상 담아주세요")
    private int count;

}