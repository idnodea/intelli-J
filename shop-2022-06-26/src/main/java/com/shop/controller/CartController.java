package com.shop.controller;
import com.shop.dto.CartItemDto;
import com.shop.service.CartService;
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

import com.shop.dto.CartDetailDto;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.DeleteMapping;

import com.shop.dto.CartOrderDto;

//page337장바구니와 관련된 요청들을 처리하기 위해 CartController생성
@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping(value = "/cart")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid CartItemDto cartItemDto, BindingResult bindingResult, Principal principal){

        if(bindingResult.hasErrors()){
        //장바구니에 담을 상품정보를 받는 cartItemDto객체에
        // 데이터바인딩 시 에러가 있는지 검사
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName();
        //현재 로그인한 회원의 이메일 정보를 변수에 저장
        Long cartItemId;

        try {
            cartItemId = cartService.addCart(cartItemDto, email);
            //화면으로부터 넘어온 장바구니에 담을 상품정보와 현재 로그인한 회원의
            //이메일정보를 이용하여 장바구니에 상품을 담는 로직을 호출
        } catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
        //결과값으로 생성된 장바구니 상품아이디와 요청이 성공하였다는
        // HTTP응답상태코드를 반환
    }

    //page346장바구니페이로 이동할 수 있도록 함
    @GetMapping(value = "/cart")
    public String orderHist(Principal principal, Model model){
        List<CartDetailDto> cartDetailList = cartService.getCartList(principal.getName());
        //현재 로그인한 사용자의 이메일 정보를 이용하여 장바구니에 담겨있는 상품정보를 조회
        model.addAttribute("cartItems", cartDetailList);
        //조회한 장바구니 상품 정보를 뷰로 전달
        return "cart/cartList";
    }

    //page 354 장바구니 상품의 수량을 업데이트하는 요청을 처리할 수 있는 로직
    @PatchMapping(value = "/cartItem/{cartItemId}")
    //HTTP메소드에서 PATCH는 요청된 자원의 일부를 업데이트할 때 PATCH를 사용
    //장바구니 상품의 수량만 업데이트하기 때문에 @PatchMapping을 사용
    public @ResponseBody ResponseEntity updateCartItem(@PathVariable("cartItemId") Long cartItemId, int count, Principal principal){

        if(count <= 0){
    //장바구니에 담겨있는 상품개수를 0개 이하로 업데이트요청을 할 때 에러메세지와 함께 반환
            return new ResponseEntity<String>("최소 1개 이상 담아주세요", HttpStatus.BAD_REQUEST);
        } else if(!cartService.validateCartItem(cartItemId, principal.getName())){
    //수정 권한을 체크(확인)
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.updateCartItemCount(cartItemId, count);
        //장바구니 상품의 개수를 업데이트
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    //page359 장바구니상품삭제요청 처리하는 로직 추가
    @DeleteMapping(value = "/cartItem/{cartItemId}")
    //HTTP 메소드에서 DELETE의 경우, 요청된 자원을 삭제할 때 사용. 
    //장바구니 상품을 삭제하기 때문에 @DeleteMapping을 사용
    public @ResponseBody ResponseEntity deleteCartItem(@PathVariable("cartItemId") Long cartItemId, Principal principal){

        if(!cartService.validateCartItem(cartItemId, principal.getName())){
            //수정 권한을 체크
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.deleteCartItem(cartItemId);
        //해당 장바구니 상품을 삭제
        
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

//page365 CartController클래스에 장바구니 상품의 수량을 업데이트하는 요청을 처리하는 로직추가
    @PostMapping(value = "/cart/orders")
    public @ResponseBody ResponseEntity orderCartItem(@RequestBody CartOrderDto cartOrderDto, Principal principal){

        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();

        if(cartOrderDtoList == null || cartOrderDtoList.size() == 0){
            //page365 주문할 상품을 선택하지 않았는지 체크
            return new ResponseEntity<String>("주문할 상품을 선택해주세요", HttpStatus.FORBIDDEN);
        }

        for (CartOrderDto cartOrder : cartOrderDtoList) {
            //page365 주문권한을 체크
            if(!cartService.validateCartItem(cartOrder.getCartItemId(), principal.getName())){
                return new ResponseEntity<String>("주문 권한이 없습니다.", HttpStatus.FORBIDDEN);
            }
        }

        Long orderId = cartService.orderCartItem(cartOrderDtoList, principal.getName());
        //주문로직호출결과 생성된 주문번호를 반환받음
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
        //생성된 주문번호와 요청이 성공했다는 HTTP응답상태코드를 반환함
    }

}