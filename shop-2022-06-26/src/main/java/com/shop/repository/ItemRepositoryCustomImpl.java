package com.shop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.dto.QMainItemDto;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import com.shop.entity.QItemImg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

//page 270 아이템리포지토리커스텀인터페이스를 구현
//클래스명 끝에 Impl을 붙여준다
//쿼리dsl에서는 BooleanExpression이라는 where절에서 사용할 수 있는 값 지원
//BooleanExpression을 반환하는 메소드를 만들고 해당 조건들을 다른 쿼리를
//생성할 때 사용할 수 있기 때문에 중복코드를 줄일 수 있다
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{
//ItemRepositoryCustom상속

    private JPAQueryFactory queryFactory;
    //동적으로 쿼리를 생성하기 위해서 JPAQueryFactory 클래스를 사용

    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }
    //JPAQueryFactory의 생성자로 EntityManager객체를 넣어줍니다.

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
    //상품판매상태조건이 전체(null)일 경우는 nul을 리턴.
    //결과값이 null이면 where절에서 해당조건은 무시된다.
    //상품판매상태조건이 null이 아니라 판매중or품절상태라면 해당조건의 상품만 조회

        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);

    }

    private BooleanExpression regDtsAfter(String searchDateType){
    //searchDateType의 값에 따라서 dateTime의 값을 이전 시간의 값으로 세팅 후
    //해당 시간 이후로 등록된 상품만 조회. 예를 들어 searchDateType의 값
    //"1m"인 경우 dateTime의 시간을 한 달 전으로 세팅 후 최근 한 달 동안
    //등록된 상품만 조회하도록 조건값을 반환

        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null){
            return null;
        } else if(StringUtils.equals("1d", searchDateType)){
            dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w", searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        } else if(StringUtils.equals("1m", searchDateType)){
            dateTime = dateTime.minusMonths(1);
        } else if(StringUtils.equals("6m", searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }

        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        //searchBy의 값에 따라서 상품명에 검색어를 포함하고 있는 상품
        // 또는 상품생성자의 아이디에 검색어를 포함하고 있는 상품을
        // 조회하도록 조건값을 반환

        if(StringUtils.equals("itemNm", searchBy)){
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)){
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        return null;

    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        //이제 QueryFactory를 이용해서 쿼리를 생성. 쿼리문을 직접작성할 때의
        //형태와 문법이 비슷한 것을 볼 수 있음.

        //selectFrom(QItem,item):상품 데이터를 조회하기 위해 QItem의 item을 지정
        //where조건절:BooleanExprssion 반환하는 조건문들을 넣어줌
        //','단위로 넣어줄 경우 and 조건으로 인식
        //offset:데이터를 가지고 올 시작 인덱스를 지정
        //limit:한번에 가지고 올 최대 개수지정
        //fetchResult():조회한 리스트 및 전체 개수를 포함하는 QueryResults를 반환
        //상품데이터리스트 조회 및 상품데이터 전체 개수를 조회하는 2번의 쿼리문실행
        List<Item> content = queryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(),
                                itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        


        long total = queryFactory.select(Wildcard.count).from(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
        //조회한 데이터를 page클래스의 구현체인 PageImpl 객체로 변환
    }

    
    private BooleanExpression itemNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%" + searchQuery + "%");
        //검색어가 null이 아니면 상품명에 해당 검색어가 포함되는 상품을 조회하는 조건을 반환함
    }


    //page284 getMainItemPage()메소드를 ItemRepositoryCustomImpl클래스에 구현
    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainItemDto> content = queryFactory
                .select(
                        new QMainItemDto(//QMainItemDto의 생성자에 반환할 값들을 넣어줌
                                //@QueryProjection을 사용하면 DTO로 바로 조회가능
                                //엔티티조회 후 DTO로 변환하는 과정을 줄일 수 있음
                                item.id,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price)
                )
                .from(itemImg)
                .join(itemImg.item, item) //itemImg와 item을 내부 조인
                .where(itemImg.repimgYn.eq("Y"))//상품이미지의 경우 대표상품이미지만 불러옴
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);

    }

}
//Querydsl조회결과 메소드
//QueryResults<T> fetchResults() 조회대상 리스트 및 전체개수포함하는 QueryResults반환
//List<T> fetch()조회대상리스트반환
//T fetchOne() 조회 대상이 1건이면 해당 타입 반환. 
// 조회대상이 1건이상이면 에러
//T fetchFirst() 조회 대상이 1건 또는 1건 이상이면 1건만 반환
//long fetchCount() 해당 데이터 전체 개수 반환. count 쿼리 실행
