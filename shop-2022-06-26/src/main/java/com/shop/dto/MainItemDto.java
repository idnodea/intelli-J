package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

//page 282 메인화면
//등록한 상품을 메인페이지에서 고객이 볼 수 있도록 구현
//Querydsl을 사용하여 페이징 처리 및 네비게이션바에 있는 Search버튼을 이용하여 상품명으로 검색가능하도록 구현

//메인페이지구현도 상품관리메뉴구현과 비슷하게 Querydsl을 사용하여 페이징 처리 및 네비게이션바에 있는
//Search버튼을 이용하여 상품명으로 검색가능하도록 구현

//Querydsl을 이용하여 상품조회 시 결과값을 받을 때 Item객체로 반환값을 받은 적이 있는데,
//이번에는 @QueryProjection을 이용하여 상품조회 시 DTO객체로 결과값을 받도록
//@QueryProjection을 이용하면 Item객체로 값을 받은 후, DTO클래스로 변환하는 과정없이 바로 DTO객체를 받을 수 있음
//메인페이지에서 상품을 보여줄 때 사용할 MainItemDto클래스를 생성
@Getter @Setter
public class MainItemDto {

    private Long id;

    private String itemNm;

    private String itemDetail;

    private String imgUrl;

    private Integer price;

    @QueryProjection
//생성자에 @QueryProjection어노테이션을 선언하여 Querydsl로 결과 조회 시, MainItemDto객체로 바로 받을 수 있도록
//이후, 메이븐컴파일-QDto파일 생성으로 QMainItemDto생성
    public MainItemDto(Long id, String itemNm, String itemDetail, String imgUrl,Integer price){
        this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
    }

}