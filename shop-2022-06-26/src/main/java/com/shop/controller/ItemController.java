package com.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.ui.Model;
import com.shop.dto.ItemFormDto;

import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import javax.persistence.EntityNotFoundException;

import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
//p181아이템폼작성 이후 작성.
@Controller   
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

//p181작성 이후, EntryPoint인터페이스 구현
//page234 ItemFormDto를 model객체에 담아 뷰로 전달
    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList){

        if(bindingResult.hasErrors()){ //상품등록시 필수값없으면 다시 상품등록페이지로
            System.out.println("상품등록시 필수값없으면 다시 상품등록페이지로");
            return "item/itemForm";


        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            //상품 등록시 첫번째 이미지 없다면 에러메시지와 함께 상품등록페이지로
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try {
            itemService.saveItem(itemFormDto, itemImgFileList);
            //상품저장로직을 호출. 매개 변수로 상품정보와 상품 이미지정보를 담고있는
            //itemImgFileList를 넘겨준다
            
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/"; //정상등록후 메인으로
    }

    //page258, 257아이템서비스코드작성이후
    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model){

        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            //조회한 상품 데이터를 모델에 담아서 뷰로 전달
            
            model.addAttribute("itemFormDto", itemFormDto);
        } catch(EntityNotFoundException e){//상품엔티티없으면 상품등록페이지로
            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }

        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model){
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try {//상품수정로직 호출합니다.
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    //page272 ItemController클래스에 상품관리화면이동 및 조회한 상품데이터를 화면에 전달하는 로직구현
    //페이지번호는 0에서 시작, 1페이지당 3개의 상품
    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    //value에 상품관리 화면 진입 시에 URL에 페이지번호가 없는 경우와 페이지번호가 있는 경우 2가지를 매핑
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        //페이징을 위해 PageRequest, of메소드를 통해 Pageable객체를 생성
        //첫번째 파라미터로는 조회할 페이지 번호, 두번째 파라미터로는 한번에 가지고 올 테이터 수를 삽입
        //URL경로에 페이지 번호가 있으면 해당 페이지를 조회, 페이지번호가 없으면 0페이지를 조회

        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
        //조회조건과 페이징 정보를 파라미터로 넘겨서 Page<Item>객체를 반환받는다

        model.addAttribute("items", items);
        //조회한 상품 데이터 및 페이징 정보를 뷰에 전달

        model.addAttribute("itemSearchDto", itemSearchDto);
        //페이지 전환 시 기존 검색 조건을 유지한 채 이동할 수 있도록 뷰에 다시 전달

        model.addAttribute("maxPage", 5);
        //상품 관리 메뉴 하단에 보여줄 페이지 번호의 최대 개수. (5로 설정.).최대 5개의 이동할 페이지번호만

        return "item/itemMng";
    }

    //상품상세페이지
    //page291 메인페이지에서 상품이미지나 상품정보를 클릭시 상품의 상세정보를 보여주는 페이지구현
    //이후 itemDtl상품상세페이지 작성
    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId){
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDto);
        return "item/itemDtl";
    }

}