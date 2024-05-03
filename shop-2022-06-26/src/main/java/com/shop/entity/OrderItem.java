package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

//page 202 다대일/일대다 매핑
@Entity
@Getter @Setter
public class OrderItem extends BaseEntity {
    //page228리그타임,업데이트타임변수 삭제, BaseEntity를 상속받도록 소스코드수정

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    //page216 지연로딩
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
 //하나의 상품은 여러 주문 상품으로 들어갈 수 있으므로 주문상품기준으로 다대일 단방향매핑

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
//한 번의 주문에 여러 개의 상품을 주문할 수 있으므로 주문 상품 엔티티와 주문 엔티티를
//다대일 단방향 매핑을 먼저 설정
//다대일과 일대다는 반대관계. 주문상품 엔티티 기준에서 다대일 매핑이었으므로
//주문 엔티티 기준에서는 주문 상품 엔티티와 일대다 관계로 매핑하면 됩니다.
// 또한 양방향 매핑에서는 '연관 관계주인'을 설정해야 한다는 점이 중요
    
//노트로 확인
//엔티티는 테이블과 다르다. 엔티티를 양방향 연관관계로 설정하면 객체의 참조는 둘인데
//외래키는 하나이므로 둘 중 누가 외래키를 관리할지를 정해야함.
//연관관계의 주인은 외래키가 있는 곳으로 설정
//연관관계의 주인이 외래키를 관리(등록,수정,삭제)
//주인이 아닌 쪽은 연관 관계 매핑 시 mappedBy속성의 값으로 연관관계의 주인설정
//주인이 아닌 쪽은 읽기만 가능    
    
    private int orderPrice; //주문가격

    private int count; //수량

    //page297 아이템엔티티수정 후, 주문할 상품과 주문수량을 통해 OrderItem객체를 만드는 메소드작성
    public static OrderItem createOrderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item); //주문할 상품 주문수량
        orderItem.setCount(count);
        
        orderItem.setOrderPrice(item.getPrice()); 
        //현재 시간 기준으로 상품가격을 주문가격으로 세팅
        //쿠폰, 할인 적용은 일단 제외
        
        item.removeStock(count);//주문수량만큼 상품의 재고 수량을 감소시킴
        return orderItem;

    }

    //주문 가격과 주문수량을 곱해서 해당 상품을 주문한 총 가격을 계산하는 메소드
    public int getTotalPrice(){
        return orderPrice*count;
    }

    //주문을 취소할 경우, 주문수량만큼 상품의 재고를 증가시키는 메소드
    public void cancel() {
        this.getItem().addStock(count);
    }

}