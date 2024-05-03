package com.shop.controller;

import com.shop.dto.OrderDto;
import com.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.shop.dto.OrderHistDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import java.util.Optional;

//page304

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping(value = "/order")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDto orderDto
            , BindingResult bindingResult, Principal principal){
        //스프링에서 비동기 처리를 할 때, @RequestBody와 @ResponseBody어노테이션을 사용
        // @RequestBody:HTTP요청의 본문 body에 담긴 내용을 자바 객체로 전달
        // @ResponseBody: 자바 객체를 HTTP요청의 body로 전달

        if(bindingResult.hasErrors()){
            //주문정보를 받는 orderDto객체에 데이터 바인딩 시 에러가 있는지 검사

            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
            //에러정보를 ResponseEntity객체에 담아 반환
        }

        String email = principal.getName(); 
        //현재 로그인유저의 정보를 얻기 위해 @Controller어노테이션이 선언된 클래스에서
        //메소드 인자로 principal객체를 넘겨 줄 경우, 해당 객체에 직접 접근이 가능함
        //principal객체에서 현재 로그인한 회원의 이메일 정보를 조회
        Long orderId;

        try {
            orderId = orderService.order(orderDto, email);
            //화면으로부터 넘어오는 주문 정보와 회원의 이메일 정보를 이용해서 주문로직을 호출
        } catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
        //결과값으로 생성된 주문 번호와 요청이 성공했다는 HTTP응답상태코드를 반환
    }


    //page316 구매이력을 조회할 수 있도록, 구현한 로직을 호출하는 메소드
    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);
        //한번에 가지고 올 주문의 개수를 일단 4개로

        Page<OrderHistDto> ordersHistDtoList = orderService.getOrderList(principal.getName(), pageable);
        //현재 로그인한 회원은 이메일과 페이징 객체를 파라미터로 전달하여 화면에 전달한 주문목록데이터를
        //리턴값으로 받음

        model.addAttribute("orders", ordersHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        return "order/orderHist";
    }

    @PostMapping("/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId , Principal principal){

        if(!orderService.validateOrder(orderId, principal.getName())){
           //자바스크립트에서 취소할 주문 번호는 조작이 가능하므로
            //다른사람의 주문을 취소하지 못하도록 주문취소권한검사를 넣음
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        orderService.cancelOrder(orderId);
        //주문취소로직을 호출
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

}