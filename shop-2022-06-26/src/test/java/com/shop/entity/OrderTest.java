package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.repository.ItemRepository;
import com.shop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderItemRepository;

//page 208 영속성
@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
@Transactional
class OrderTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    public Item createItem(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());

        item.setUpdateTime(LocalDateTime.now());
        return item;
    }

    //page208
    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest() {

        Order order = new Order();

        for(int i=0;i<3;i++){
            Item item = this.createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
            //아직 영속성 컨텍스트에 저장되지 않은 orderItem 엔티티를
            //order엔티티에 담아줍니다.
        }

        orderRepository.saveAndFlush(order);
        //order엔티티를 저장하면서 강제로 flush를 호출하여 영속성컨텍스트에
        //있는 객체들을 데이터베이스에 반영합니다.
        //코드 실행시 flush를 호출하면서 콘솔창에 insert쿼리문이 출력되는 것을 확인할 수 있습니다.
        //주문 데이터가 먼저 데이터베이스에 반영됩니다.
        //그후 영속성이 전이되면서 order에 담아두었던 orderItem이 insert되는 것을 확인가능
        //총 3개의 orderItem을 담아두었으므로 3번의 insert쿼리문이 실행됩니다.

        em.clear();
        //영속성켄텍스트의 상태를 초기화합니다.

        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        //영속성컨텍스트를 초기화했기 때문에 데이터베이스에서 주문 엔티티를 조회합니다
        //select쿼리문이 실행되는 것을 콘솔창에서 확인할 수 있습니다.

        assertEquals(3, savedOrder.getOrderItems().size());
        //itemOrder엔티티 3개가 실제로 데이터베이스에 저장되었는지 검사합니다.
    }

    public Order createOrder(){ //주문데이터생성하여 저장하는 메소드
        Order order = new Order();
        for(int i=0;i<3;i++){
            Item item = createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }
        Member member = new Member();
        memberRepository.save(member);
        order.setMember(member);
        orderRepository.save(order);
        return order;
    }

    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest(){
        Order order = this.createOrder();
        order.getOrderItems().remove(0); 
        //order엔티티에서 관리하고있는 orderItem리스트의 0번째 인덱스요소를 제거
        em.flush(); //flush를 호출하면 콘솔에서, orderItem을 삭제하는 쿼리문이 
        //출력되는 것을 확인할 수 있음. 부모엔티티-자식 관계끊어지고, 고아객체삭제쿼리실행
    }

    //page214 
    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest(){
        Order order = this.createOrder();//기존에 만들었던 주문생성 메소드를 이용해 주문데이터를 저장
        Long orderItemId = order.getOrderItems().get(0).getId();
        em.flush();
        em.clear();
        //영속성 컨텍스트의 상태 초기화 후 order엔티티에 저장했던 주문 상품 아이디를 이용
        //하여 orderItem을 데이터베이스에서 다시 조회합니다.
        //코드에서 OrderItem데이터조회시, order_item,item,oders,member테이블조인해서 한번에 가져온다
        //4개,5개정도지만, 실제비즈니스라면?  즉시로딩 대신 지연로딩을 해야한다
        //FetchType.LAZY방식으로 설정,,OderItem.java로 ,,설정이후 재실행하면?오더아이템 엔티티만 조회
        //지연로딩으로 하면, 실제 엔티티 대신 프록시객체.
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);
        //orderItem엔티티에 있는 order객체의 클래스를 출력합니다. Order클래스가 출력되는 것을 확인가능
        //출력:Order class : class com.shop.entity.Order
        System.out.println("Order class : " + orderItem.getOrder().getClass());
        System.out.println("===========================");
        orderItem.getOrder().getOrderDate();
        System.out.println("===========================");
    }

}