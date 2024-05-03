package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface ItemRepository extends JpaRepository<Item, Long>
    , QuerydslPredicateExecutor<Item>, ItemRepositoryCustom{

    //파라미터 조회
    List<Item> findByItemNm(String itemNm);

    //or 조건처리
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    //lessThan 조건처리
    List<Item> findByPriceLessThan(Integer price);

    //orderBy로 정렬 처리하기
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    //@Query 이용한 검색처리
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    //native 쿼리 처리
    @Query(value="select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery=true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);
}