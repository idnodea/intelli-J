package com.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shop.dto.ItemDto;    //p120  아이템디티오객체 생성이후 모델에 데이터를 넣어 뷰로전달
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

//page111 타임리프 예제용컨트롤러 클래스만들기
@Controller
@RequestMapping(value="/thymeleaf")   //타임리프경로로 오는 요청을 컨트롤러가 처리
public class ThymeleafExController {

    @GetMapping(value = "/ex01")  
    public String thymeleafExample01(Model model){
        //모델객체->뷰 key,value
        model.addAttribute("data", "타임리프 예제 입니다.");
        return "thymeleafEx/thymeleafEx01";
    }

    @GetMapping(value = "/ex02")     //p120 전달받은 아이템dto를 출력
    public String thymeleafExample02(Model model){
        ItemDto itemDto = new ItemDto();
        itemDto.setItemDetail("상품 상세 설명");
        itemDto.setItemNm("테스트 상품1");
        itemDto.setPrice(10000);
        itemDto.setRegTime(LocalDateTime.now());

        model.addAttribute("itemDto", itemDto);
        return "thymeleafEx/thymeleafEx02";
    }

    @GetMapping(value = "/ex03")
    public String thymeleafExample03(Model model){

        List<ItemDto> itemDtoList = new ArrayList<>();

        for(int i=1;i<=10;i++){

            ItemDto itemDto = new ItemDto();
            itemDto.setItemDetail("상품 상세 설명"+i);
            itemDto.setItemNm("테스트 상품" + i);
            itemDto.setPrice(1000*i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);
        }

        //화면에서 출력할 itemDtoList를 model에 담아서 View에 전달합니다.
        model.addAttribute("itemDtoList", itemDtoList);

        return "thymeleafEx/thymeleafEx03";
    }

    //p124
    @GetMapping(value = "/ex04")
    public String thymeleafExample04(Model model){

        List<ItemDto> itemDtoList = new ArrayList<>();

        for(int i=1;i<=10;i++){

            ItemDto itemDto = new ItemDto();
            itemDto.setItemDetail("상품 상세 설명"+i);
            itemDto.setItemNm("테스트 상품" + i);
            itemDto.setPrice(1000*i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);
        }

        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafEx/thymeleafEx04";
    }

    //p129
    @GetMapping(value = "/ex05")
    public String thymeleafExample05(){
        return "thymeleafEx/thymeleafEx05";
    }

    //page131  ex06에서 작성한 key=value 파람1 파람2, 파라미터데이터1,파라미터데이터2
    @GetMapping(value = "/ex06")
    public String thymeleafExample06(String param1, String param2, Model model){
        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);
        return "thymeleafEx/thymeleafEx06";
    }

    //e07은 헤드본문푸터 출력  layout1.html<-헤더,푸터.  입력된 layout1를 출력
    @GetMapping(value = "/ex07")
    public String thymeleafExample07(){

        //스프링 시큐리티 아이디:user, 비밀번호:  콘솔창에 출력되어야하는데 ??
        //인증x-상품상세페이지조회
        //인증o-상품주문
        //관리자권한-상품등록
        System.out.println("spring security password:");
        return "thymeleafEx/thymeleafEx07";
    }

}

//콘솔창에선 정상출력
// 그런데 url로 이동이??.
// 그리고 시큐리티이후 로그인페이지로 이동되어야하는데 그것도 ???