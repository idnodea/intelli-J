package com.shop.repository;

import com.shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

//page 207 영속성전이
//특정 엔티티와 연관된 엔티티의 상태를 함꼐 변화시키는 옵션

//cascade 흐르다.  영속성전이.
//엔티티의 상태를 변경할 때 해당 엔티티와 연관된 엔티티의 상태 변화를 전파시키는 옵션
//이때 부모는 one에 해당하고 자식은 many에 해당.
//예시로 Order엔티티가 삭제되었을 때, 해당 엔티티와 연관되어 있는 
// OrderItem엔티티가 함께 삭제되거나,
//Order엔티티를 저장할 때 Order엔티티에 담겨있던 
// OrderItem엔티티를 한꺼번에 저장할 수 있음
//상태가 전파되는 모습~~~~~~폭포

//CASCADE
//PERSIST 부모 엔티티가 영속화 될 때 자식 엔티티도 영속화
//MERGE   부모 엔티티가 병합될 때 자식 엔티티도 병합
//REMOVE  부모 엔티티가 삭제될 때 연관된 자식 엔티티도 삭제
//REFRESH 부모 엔티티가 refresh되면 연관된 자식 엔티티도 refresh
//DETACH  부모 엔티티가 detach되면 연관된 자식 엔티티도 detach상태로 변경
//ALL     부모 엔티티의 영속성상태변화를 자식 엔티티에 모두 전이

//영속성 전이 옵션을 무분별하게 사용할 때, 삭제되지 말아야할 데이터까지 삭제될 수 있으므로 주의 
//영속성 전이옵셥은 단일 엔티티에 완전히 종속적이고 
// 부모엔티티와 자식 엔티티의 라이프 사이클이 유사할 때  cascade옵션활용하자
//오더리포지토리로

//page311 OrderHisDto생성 이후, OrderRepository인터페이스에 @Query어노테이션을 이용해 주문이력을 
//조회하는 쿼리작성


public interface OrderRepository extends JpaRepository<Order, Long> {

    //쿼리문법은 JPQL
    @Query("select o from Order o " +
            "where o.member.email = :email " +
            "order by o.orderDate desc"
    )
    List<Order> findOrders(@Param("email") String email, Pageable pageable);
    //현재 로그인한 사용자의 주문 데이터를 페이징 조건에 맞춰서 조회

    @Query("select count(o) from Order o " +
            "where o.member.email = :email"
    )
    Long countOrder(@Param("email") String email);
    //현재 로그인한 회원의 주문 개수가 몇 개인지 조회합니다
}