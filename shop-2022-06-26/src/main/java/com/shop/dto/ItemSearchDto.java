package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

//page 266 상품관리메뉴 구현
//Querydsl을 Spring Data Jpa와 함께 사용하기 위해서는 사용자 정의 리포지토리를 정의해야함
//1단계 사용자 저으이 인터페이스 작성
//2단계 상ㅇ자 정의 인터페이스 구현
//3단계 Spring Data Jap 리포지토리에서 사용자 정의 인터페이스 상속
@Getter @Setter
public class ItemSearchDto {

    private String searchDateType;
    //현재 시간과 상품 등록일을 비교해서 상품 데이터를 조회
    //조회시간 기준은 
    //all:상품 등록일 전체
    //1d:최근 하루 동안 등록된 상품
    //1w:최근 일주일 동안 등록된 상품
    //1m:최근 한달 동안 등록된 상품
    //6m:최근 6개월 동안 등록된 상품

    private ItemSellStatus searchSellStatus;
    //상품의 판매상태를 기준으로 상품데이터조회
    
    private String searchBy;
    //상품 조회시 어떤 유형으로 조회할지 선택
    //itemNm:상품명,  createdBy:상품등록자아이디
    
    private String searchQuery = "";
    //조회할 검색어를 저장하는 변수
    //searchBy가 itemNm일 경우 상품명, createdBy면 상품등록자아이디기준
}