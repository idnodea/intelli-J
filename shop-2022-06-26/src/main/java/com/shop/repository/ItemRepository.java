package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;

//page 80. 엔티티매니저 삭제 후, 리포지토리로
//엔티티 매니저를 이용해 item를 저장하는 예제코드, EntityManager
//스프링 데이터 jpa에서는 엔티티매니저작성필요없음. 어떻게 jpa가 엔티티를 관리하는지 확인하기 위함이었음
//jpa리포지토리를 상속받는 아이템리포지토리 작성.Jpa리포지토리는 2개의 제네릭 타입을 사용.
//jpa리포지토리는 2개의 제네릭 타입을 사용. 첫번째에는 엔티티타입클래스
//두번째에는 기본키 타입을 넣어준다.
public interface ItemRepository extends JpaRepository<Item, Long>,
        QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {

    //find+(엔티티이름)+By+변수이름
//    //itemNm(상품명)으로 데이터를 조회하기 위해서 By 뒤에 필드명인 ItemNm을 메소드의 이름에 붙여준다
//    //엔티티명은 생략이 가능하므로 findItemByItemNm대신에 findByItemNm으로 메소드명을 만들어준다.
//    //매개변수로는 검색할 때 사용할 상품명 변수를 넘겨줍니다
    List<Item> findByItemNm(String itemNm);


    //LessThan조건처리 상품명과 상품상세설명을 or조건을 이용하여 조회하는 쿼리 메소드
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);


    //파라미터로 넘어온 price변수보다 값이 작은 상품데이터를 조회하는 쿼리 메소드입니다.
    List<Item> findByPriceLessThan(Integer price);

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    //JPQL
    @Query("select i from Item i where i.itemDetail like " +
            "%:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);
    //@Query어노테이션 안에 JPQL로 작성한 쿼리문을 넣어준다.
    //from 뒤에는 엔티티 클래스로 작성한 Item을 지정해주었고,
    //Item으로부터 데이터를 select하겠다는 것을 의미


    @Query(value="select * from item i where i.item_detail like " +
            "%:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);////파라미터에 @Param어노테이션을 이용하여 파라미터로 넘어온 값을
//// JPQL에 들어갈 변수로 지정해줄 수 있습니다.
////현재는 itemDetail변수를 "like % %"사이에
//// ":itemDetail"로 값이 들어가도록 작성
//
////value 안에 네이티브 쿼리문을 작성하고 "nativeQuery = true"를 지정합니다
//
////Querydsl @Query어노테이션의 단점보완.
////Querydsl은 JPQL을 코드로 작성할 수 있도록 도와주는 빌더API
////문자열이 아닌 코드로 작성하므로 컴파일러의 도움을 받을 수 있음
////또한 오타발생여부를 쉽게 알 수 있으며 동적으로 쿼리생성까지 해줌

    ////    두 쿼리 모두 item 테이블에서 itemDetail 필드에
////    특정 문자열이 포함된 모든 상품을 찾고,
////    결과를 가격 내림차순으로 정렬하여 반환하는 기능을 수행합니다.
////    하지만 각각의 쿼리는 조금 다른 방식으로 이를 수행합니다.
////
////    첫 번째 메서드: findByItemDetail
////    이 메서드는 JPQL을 사용하여 쿼리를 정의합니다.
////    JPQL은 엔티티 객체를 대상으로 쿼리를 작성하는 방식으로,
////    SQL과 유사하지만 데이터베이스 테이블이 아닌 엔티티 클래스와
////    그 필드를 기반으로 쿼리를 작성합니다.
////
////    @Query 어노테이션 안의
////    "select i from Item i where i.itemDetail like
////    %:itemDetail% order by i.price desc"는
////    Item 엔티티에서 itemDetail 필드가 파라미터로 전달된
////    itemDetail 값을 포함하는 모든 엔티티를 선택하여,
////    가격(price)을 기준으로 내림차순으로 정렬하라는
////    JPQL 쿼리입니다.
////     :itemDetail은 메서드 파라미터에서 전달된 값을
////     JPQL 쿼리의 파라미터로 바인딩하는 부분입니다.
////    이 메서드는 JPQL을 사용하므로 데이터베이스에
////    독립적인 방식으로 쿼리를 작성할 수 있습니다.
////    두 번째 메서드: findByItemDetailByNative
////    이 메서드는 네이티브 SQL 쿼리를 사용하여 쿼리를 정의합니다.
////    네이티브 쿼리는 데이터베이스에 특화된 순수 SQL을 사용하여
////    쿼리를 작성하는 방식입니다.
////
////    value 속성에 작성된
////    "select * from item i where i.item_detail like
////    %:itemDetail% order by i.price desc"
////    쿼리는 데이터베이스의 item 테이블을 직접 조회하여
////    item_detail 필드가 파라미터로 전달된 itemDetail 값을
////    포함하는 모든 레코드를 선택하고, price 필드를 기준으로
////    내림차순으로 정렬합니다.
////    nativeQuery = true 속성은 이 쿼리가 네이티브 SQL
////    쿼리임을 나타냅니다.
////    이 메서드는 데이터베이스에 종속적인 SQL을 사용하므로,
////    사용하는 데이터베이스의 SQL 문법을 따라야 합니다.
////    두 메서드 모두 @Param("itemDetail") 어노테이션을
////    사용하여 메서드 파라미터와 쿼리의 파라미터를 연결합니다.
////    결과적으로, 이 메서드들은 상품 상세 설명에 특정 문자열이
////    포함된 상품 목록을 가격 내림차순으로 검색하여 반환하는 기능을
////    수행합니다.


}
