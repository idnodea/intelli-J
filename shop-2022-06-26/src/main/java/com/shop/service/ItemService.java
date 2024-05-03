package com.shop.service;

import com.shop.dto.ItemFormDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import com.shop.dto.ItemImgDto;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;

import com.shop.dto.ItemSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shop.dto.MainItemDto;

//page 256 상품등록기능구현 이후, 등록한 상품정보를 볼 수 있는 상품상세페이지진입 및
//상품데이터를 수정할 수 있도록 기능구현

//등록된 상품을 불러오는 메소드를 ItemService클래스에 추가
@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final ItemImgService itemImgService;

    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        //상품 등록
        Item item = itemFormDto.createItem();
        System.out.println("등록된 상품: "+item.toString());
        //상품 등록 폼으로부터 입력받은 데이터를 이용하여 item객체를 생성
        
        itemRepository.save(item);
        System.out.println("상품 데이터 저장 완료");
        //상품 데이터를 저장

        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            if(i == 0) 
                //첫번째 이미지일 경우 대표상품이미지 여부값을 y로세팅 나머지는 n
                itemImg.setRepimgYn("Y");
            else
                itemImg.setRepimgYn("N");

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i)); //상품이미지정보저장
            System.out.println("이미지 저장 완료");
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    //page257, 상품데이터를 읽어노는 트랜잭션을 읽기 전용을 설정.
    //이럴 경우 JPA가 더티체킹(변경감지)를 수행하지 않아서 성능을 향상시킬 수 있음
    public ItemFormDto getItemDtl(Long itemId){
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        //해당 상품의 이미지를 조회함.
        //등록순으로 가지고 오기 위해 상품 이미지 아이디 오름차순으로 가지고 온다
        
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        //조회한 ItemImg엔티티를 ItemImgDto객체로 만들어서 리스트에 추가
        for (ItemImg itemImg : itemImgList) {
                       ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        //상품의 아이디를 통해 상품엔티티를 조회. 존재하지 않을 때는.엔티티파운드이셉션
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        //상품 수정
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        //상품등록화면으로부터 전달받은 상품_아이디를 이용하여 상품엔티티를조회

        item.updateItem(itemFormDto);
        //상품등록화면으로부터 전달받은 ItemFormDto를 통해 상품엔티티를 업데이트

        List<Long> itemImgIds = itemFormDto.getItemImgIds();
        //상품이미지 아이디 리스트를 조회

        
        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            itemImgService.updateItemImg(itemImgIds.get(i),
                    itemImgFileList.get(i)); 
            //상품 임지를 업데이트하기 위해 updateItemImg()메소드에 상품이미지 아이디와,
            //상품이미지 파일정보를 파라미터로 전달
        }

        return item.getId();
    }

    //page 271 ItemService클래스에 상품조회조건과 페이지 정보를 파라미터로 받아서 상품데이터를
    //조회하는 getAdminItemPage()메소드를 추가. 데이터의 수정이 일어나지 않으므로 최적화를 위해
    //@Transactional(readOnly=true)어노테이션을 설정
    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    //p284 메인페이지를 보여주는 상품데이터를 조회하는 메소드를 ItemService클래스에 추가
    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }

}