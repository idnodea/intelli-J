package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

//page232상품등록하기 
//상품의 정보를 가지고 있는 상품엔티티
//상품의 이미지를 저장하는 상품 이미지 엔티티
//임지 파일명, 원본 이미지 파일명, 이미지 조회 경로, 대표 이미지 여부

//page230상품등록구현
@Entity
@Table(name="item_img")
@Getter @Setter
public class ItemImg extends BaseEntity{

    @Id
    @Column(name="item_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imgName; //이미지 파일명

    private String oriImgName; //원본 이미지 파일명

    private String imgUrl; //이미지 조회 경로

    private String repimgYn; //대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY) 
    //상품엔티티와 다대일 단방향관계로 매핑
    //지연 로딩을 설정하여 매핑된 상품 엔티티 정보가 필요할 경우, 데이터를 조회
    @JoinColumn(name = "item_id")
    private Item item;

    public void updateItemImg(String oriImgName, String imgName, String imgUrl){
       //원본 이미지 파일명, 업데이트할 파일명, 이미지 경로
        //파라미터로 입력받아 임지 정보를 업데이트하는 메소드
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
   //이후, 상품등록 및 수정에 사용할 데이터전달용DTO클래스를 작성
// 엔티티 자체를 화면으로 반환할 수 있지만, 그럴 때 엔티티클래스에 화면에서만 사용하는 값이 추가되버린다
    //상품등록할 때는 화면으로부터 전달받은 DTO객체를 엔티티 객체로 변환하는 작업
    //상품조회 시엔 엔티티 객체를 DTO객체로 바꿔주는 작업

}