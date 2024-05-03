package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

//page196 다대일 단방향 매핑하기
//page220 지연로딩
@Entity
@Getter @Setter
@Table(name="cart_item")
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    //하나의 장바구니에 여러상품이 들어갈 수 있도록
    //ManyToOne 어노테이션으로 다대일로 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private Cart cart;

    //장바구니와 상품 간 매핑이 필요함.
    //ManyToOne 어노테이션으로 다대일로 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int count; //그래서 담을 수량이 몇 개인가.

    //장바구니에 담을 상품엔티티를 생성하는 메소드와 장바구니에 담을 수량을 
    //증가시켜 주는 메소드를 카트아이템클래스에 추가
    public static CartItem createCartItem(Cart cart, Item item, int count) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        return cartItem;
    }

    
    public void addCount(int count){
        this.count += count;
    }
    //장바구니에 담겨있는 기존상품인데, 해당상품을 추가로 장바구니에 담을 때
    //기존 수량에 현재 담을 수량을 더해줄 때 사용할 메소드

    //page353
//장바구니에서 상품의 수량을 변경할 경우, 실시간으로 해당회원의 장바구니상품의 수량도 변경
    public void updateCount(int count){
        this.count = count;
    }

}