package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;

//page191 일대일 단방향 매핑하기
//page219 연관관계매핑-엔티티지연로딩 FetchType.LAZY
@Entity
@Table(name = "cart")
@Getter @Setter
@ToString
public class Cart extends BaseEntity {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //@OneToOne어노테이션을 이용해 회원 엔티티와 일대일로 매핑을 합니다.
    @OneToOne(fetch = FetchType.LAZY) //일부러 느리게
    //@JoinColumn 어노테이션을 이요해 매핑할 외래키를 지정합니다.
    //name속성에는 매핑할 외래키의 이름을 설정합니다. @JoinColumn의 name을
    //명시하지 않으면 JPA가 알아서 ID를 찾지만, 컬럼명이 원하는대로
    //생성되지 않을 수 있기 때문에 직접 지정

    //따로옵션을 주지않을경우
    //@OneToOne(fetch = FetchType.EAGER)와 같이 즉시로딩이 기본임.
    //(일대일 다대일  OneToOne, ManyToOne)일 경우.

    @JoinColumn(name="member_id")
    private Member member;

    //회원 1명당 1개의 장바구니. 처음 장바구니에 상품을 담을 때는
    //해당 회원의 장바구니를 생성해주어야함
    //회원엔티티를 파라미터로 받아서 장바구니 엔티티를 생성하는 로직 추가
    public static Cart createCart(Member member){
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }

}