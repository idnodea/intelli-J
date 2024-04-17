package com.shop.repository;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.shop.entity.QItem;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
class ItemRepositoryTest {


    @Autowired
    ItemRepository itemRepository;

    // p104영속성컨텍스트를 사용하기 위해  @PersistenceContext사용
    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());
    }
    //    @Test
//    @DisplayName("상품저장테스트")
//    public void createItemTest(){
//        Item item = new Item();
//        item.setItemNm("테스트상품");
//        item.setPrice(10000);
//        item.setItemDetail("테스트상품상세설명");
//        item.setItemSellStatus(ItemSellStatus.SELL);
//        item.setStockNumber(100);
//        item.setRegTime(LocalDateTime.now());
//        Item savedItem = itemRepository.save(item);
//        System.out.println(savedItem.toString());
//    }

    public void createItemList(){
        for(int i=1;i<=10;i++){
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100); item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }
    }
    //    public void createItemList(){
//        for(int i =1; i<=10; i++){
//            Item item = new Item();
//            item.setItemNm("테스트상품"+i);
//            item.setPrice(10000+i);
//            item.setItemDetail("테스트상품상세설명"+i);
//            item.setItemSellStatus(ItemSellStatus.SELL);
//            item.setStockNumber(100);item.setRegTime(LocalDateTime.now());
//            item.setUpdateTime(LocalDateTime.now());
//            Item savedItem = itemRepository.save(item);
//        }
//    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }
    //    @Test
//    @DisplayName("상품명 조회 테스트")
//    public void findByItemNmTest(){
//        this.createItemList(); //기존에 만들었던 테스트 상품을 만드는 메소드를 실행하여, 조회할 대상을 만듦
////        List<Item> itemList = itemRepository.findByItemNm("테스트상품1");
//        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트상품1","테스트상품상세설명5");
//        //상품명이 테스트상품1 또는 상품상세설명이 테스트상품상세설명5이면 itemList에 할당합니다.
//        for (Item item : itemList){
//            System.out.println(item.toString());
//        }
//    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByItemNmOrItemDetailTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }


    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }
    //    @Test
//    @DisplayName("가격 LessThan 테스트")
//    public  void findByPriceLessThanTest(){
//        this.createItemList();
//        List<Item> itemList = itemRepository.findByPriceLessThan(10005);
//        for (Item item : itemList){
//            System.out.println(item.toString());
//        }
//    }


    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDesc(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }
//    @Test
//    @DisplayName("가격 내림차순 조회 테스트")
//    public void findByPriceLessThanOrderByPriceDesc(){
//        this.createItemList();
//        List<Item> itemList=
//                itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
//        for(Item item : itemList){
//            System.out.println(item.toString());
//        }
//    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByItemDetailTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }
    //    @Test
//    @DisplayName("@Query를 이용한 상품 조회 테스트")
//    public void findByItemDetailTest(){
//        this.createItemList();
//        List<Item> itemList = itemRepository.findByItemDetail("테스트상품 상세설명");
//        for(Item item : itemList){
//            System.out.println(item.toString());
//        }
//    }


//    @Test
//    @DisplayName("nativeQuery 속성을 이용한 상품 조회 테스트")
//    public void findByItemDetailByNative(){
//        this.createItemList();
//        List<Item> itemList=
//                itemRepository.findByItemDetailByNative("테스트상품상세설명");
//        for (Item item : itemList){
//            System.out.println(item.toString());
//        }
//    }

    //p104영속성컨텐스트를 사용하기 위해 @위에 어노테이션-주입
    //JPAQueryFacotry를 이용하여 쿼리를 동적생성
    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDslTest(){
        //생성자의 파라미터로 EntityManager객체를 넣어준다
        this.createItemList();
      //Querydsl를 통해 쿼리를 생성하기 위해 플러그인을 통해 자동으로 생성된 QItem객체를 이용
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;
        JPAQuery<Item> query  = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))
                .orderBy(qItem.price.desc());

        //jpaQuery 메소드 중 하나인 fetch를 이용해 쿼리 결과를 리스트로 반환.
        //fetch()메소드실행시점에 쿼리문 실행되는 걸 알 수 있음.
        List<Item> itemList = query.fetch();
        //List<T>조회결과리스트반환
        //T fetchOne 1건인 경우 제네릭지정타입으로반환
        //T fetchFirst()조회대상중 1건만 반환
        //Long fetchCount(조회대상 개수반환)
        //QueryResultMt< fetchResults()조회한 리스트와 전체 개수를 포함한
        //QueryResults반환

        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    public void createItemList2(){
        for(int i=1;i<=5;i++){
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL); //판매상태
            item.setStockNumber(100);   //재고수량
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }

        for(int i=6;i<=10;i++){
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(0);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
    }


    @Test
    @DisplayName("상품 Querydsl 조회 테스트 2")
    public void queryDslTest2(){

        this.createItemList2();
        ////'쿼리에 들어갈 조건'을 만들어주는 빌더
        BooleanBuilder booleanBuilder = new BooleanBuilder();//Predicate구현,메소드체인형식상태로사용가능

        QItem item = QItem.item;
        String itemDetail = "테스트 상품 상세 설명";
        int price = 10003;
        String itemSellStat = "SELL";

        //필요한 상품을 조회하는데 필요한 "and"조건추가.아래 소스에서 상품판매가 SELL일 때만
        //booleanBuilder에 판매상태 조건을 동적으로 추가
        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
        
        booleanBuilder.and(item.price.gt(price));
        System.out.println(ItemSellStatus.SELL);  
        if(StringUtils.equals(itemSellStat, ItemSellStatus.SELL)){ 
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        //데이터페이징 조회   PageRequest.of()메소드를 이용해 PageBle객체를 생성
        //첫번째 인자는 조회할 페이지의 번호, 두번째 인자는 한 페이지당 조회할 데이터의 개수를 삽입
        Pageable pageable = PageRequest.of(0, 5); 

        //QueryDslPredicateExecutor인터페이스에서 정의할 findAll()메소드를 이용해
        //조건에 맞는 데이터를 Page객체로 받아옵니다.
        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);
        System.out.println("total elements : " + itemPagingResult. getTotalElements ());

        List<Item> resultItemList = itemPagingResult.getContent();
        for(Item resultItem: resultItemList){
            System.out.println(resultItem.toString());
        }
    }

}