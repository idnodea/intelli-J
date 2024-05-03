package com.shop.service;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.CartItemDto;
import com.shop.entity.CartItem;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.repository.CartItemRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

//장바구니 담기 테스트
@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class CartServiceTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CartService cartService;

    @Autowired
    CartItemRepository cartItemRepository;

    public Item saveItem(){//테스트를 위한,
        //장바구니에 담을 상품
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

    public Member saveMember(){
        //장바구니에 담을 회원정보
        Member member = new Member();
        member.setEmail("test@test.com");
        return memberRepository.save(member);
    }

    @Test
    @DisplayName("장바구니 담기 테스트")
    public void addCart(){
        Item item = saveItem();
        Member member = saveMember();

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setCount(5);
        //장바구니에 담을 상품과 수량을  cartItemDto객체에 세팅
        
        cartItemDto.setItemId(item.getId());
        

        Long cartItemId = cartService.addCart(cartItemDto, member.getEmail());
        //상품을 장바구니에 담는 로직 호출 결과 
        // 생성된 장바구니 상품아이디를 CartItemId변수에 저장
        
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        //장바구니 상품 아이디를 이용하여 생성된 장바구니 상품정보를 조회

        assertEquals(item.getId(), cartItem.getItem().getId());
        //상품아이디와 장바구니에 저장된 상품아이디가 같다면 테스트통과
        assertEquals(cartItemDto.getCount(), cartItem.getCount());
        //장바구니에 담았던 수량과 실제로 장바구니에 저장된 수량이 같다면 테스트통과
    }

}