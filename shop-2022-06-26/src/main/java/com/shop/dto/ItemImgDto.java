package com.shop.dto;

import com.shop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

//page232 상품저장 후 상품이미지에 대한 데이터를 전달할 DTO클래스
@Getter @Setter
public class ItemImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();
    //멤버 변수로 ModelMapper객체를 추가

    public static ItemImgDto of(ItemImg itemImg) {
        return modelMapper.map(itemImg,ItemImgDto.class);
    }
    //ItemImg엔티티객체를 파라미터로 받아서 ItemImg객체의 자료형과 멤버변수의
    //이름이 같을 때 ItemImgDto로 값을 복사해서 반환합니다.
    //static메소드로 선언해 ItemImgDto객체를 생성하지 않아도 호출할 수 있도록
}