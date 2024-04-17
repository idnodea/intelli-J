package com.shop.entity;

import com.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

//p203
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

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        Order order = new Order();
        order.setMember(member);

        for(OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }

        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL;
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

}