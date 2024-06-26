package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

//page233 상품데이터정보를 전달하는 DTO
@Getter @Setter
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "상품 상세는 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();
    //상품 저장 후 수정할 때 상품이미지 정보를 저장하는 리스트
    
    private List<Long> itemImgIds = new ArrayList<>();
    //상품의 이미지 아이디를 저장하는 리스트
    //상품 등록시엔 아직 상품의 이미지를 저장하지 않았으므로 아무 값도 안 들어가있을 것임
    //수정 시에 이미지 아이디를 담아둘 용도로 사용
    
    private static ModelMapper modelMapper = new ModelMapper();

    //아래의 두 코드는, modelMapper를 이용하여 엔티티 객체와 DTO객체 간의 데이터를 복사
    //복사한 객체를 반환해주는 메소드
    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }
    public static ItemFormDto of(Item item){
        return modelMapper.map(item,ItemFormDto.class);
    }

}