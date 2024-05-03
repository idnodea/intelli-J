package com.shop.entity;

import com.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

//p203 다대일/일대다 매핑하기
//p206 영속성설정
//page220 지연로딩
@Entity
//정렬할 때 사용하는 "order"키워드가 있기 때문에, Order엔티티에 매핑되는 테이블로
//"orders"를 지정  's'
@Table(name = "orders")
@Getter @Setter
public class Order extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; 
    //한 명의 회원은 여러번 주문을 할 수 있으므로 주문엔티티기준에서 다대일 단방향매핑

    private LocalDateTime orderDate; //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문상태

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL
            , orphanRemoval = true, fetch = FetchType.LAZY)
    //주문상품 엔티티와 일대다 매핑을 합니다. 외래키(order_id)가 order_item테이블
    //에 있으므로 연관관계의 주인은 OrderItem 엔티티. Order엔티티가 주인이 아니므로,
    //"mappedBy"속성으로 연관관계의 주인을 설정. 속성의 값으로 "order"를 적어준 건
    //오더아이템에 있는 오더에 의해 관리된다는 의미. 연관관계의 주인의 필드인 order를
    //mapppedBy의 값으로 세팅
    
    //cascade추가 page208. 부모엔티티의 영속성 상태변화를 자식 엔티티에 모두 전이
    //cascadeTypeAll 옵션설정
    private List<OrderItem> orderItems = new ArrayList<>();
    //하나의 주문이 여러 개의 주문 상품을 갖으므로 List자료형을 사용해서 매핑함
    //무조건 양방향으로 연관 관계를 매핑하면 해당 엔티티는 엄청나게 많은 테이블과
    //연관관계를 맺게되고 엔티티클래스가 복잡해지므로, 연관관계 단방향매핑으로 설계후
    //나중에 필요할 때 양방향 매핑을 추가하자

    //page210 고아객체제거테스트
    
    
    //page300, 생성한 주문상품객체를 이용하여 주문객체를 만드는메소드작성
    public void addOrderItem(OrderItem orderItem) { 
        //orderItems에는 주문상품정보들을 담아줌. orderItem객체를 order객체의 oderItems에 추가
        orderItems.add(orderItem);
        orderItem.setOrder(this);
        //Order엔티티와 OrderItem엔티티가 양방향참조관계이므로, orderItem객체에도 order객체를 세팅
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        Order order = new Order();
        order.setMember(member); //상품을 주문한 회원의 정보를 세팅

        for(OrderItem orderItem : orderItemList) {
            //상품페이지에서는 1개의 상품을 주문하지만, 장바구니페이지에서는 한 번에 여러 개의 상품주문가능
            //따라서 여러 개의 주문 상품을 담을 수 있도록
            //리스트형태로 파라미터 값을 받으며 주문 객체에 orderItem객체를 추가
            order.addOrderItem(orderItem);
        }

        order.setOrderStatus(OrderStatus.ORDER); //주문상태를 "ORDER"로 세팅
        order.setOrderDate(LocalDateTime.now()); //현재시간을 주문시간으로 세팅
        return order;
    }

    public int getTotalPrice() { //총 주문 금액을 구하는 메소드
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    //주문 취소 시 주문 수량을
    // 상품의 재고에 더해주는 로직과 주문상태를 취소상태로 바꿔주는 메소드
    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL;
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

}