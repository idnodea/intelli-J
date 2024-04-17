package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import com.shop.dto.ItemFormDto;
import com.shop.exception.OutOfStockException;

//page 75  엔티티매핑 관련 어노테이션,
// page203의 다대다매핑이 가능하지만 추가x
//다대다 매핑을 사용하지 않는 건, 엔티티가 너무 복잡해지는 걸 막기위함 뿐만 아니라
//연결테이블에 컬럼을 추가할 수 없기 때문
//그 연결 테이블에는 조인 컬럼뿐 아니라 추가 컬럼들이 필요한 경우가 많기 때문.
//또한 엔티티조회 시에, member엔티티에서 item을 조회하면 중간 테이블이 있기 때문에
//어떤 쿼리문이 실행될지 예측하기도 쉽지 않습니다. 따라서  연결 테이블용 엔티티를 하나
//생성한 후 일대다 다대일 매핑을 하면 된다(
@Entity
@Table(name="item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity {

    @Id   //item클래스의 id변수와 item테이블의 item_id컬럼이 매핑되도록
    @Column(name="item_id") //p76 @Column어노테이션추가속성, 테이블에 매핑될 칼럼이름
    @GeneratedValue(strategy = GenerationType.AUTO) //p77 기본키생성-jpa구현체가 자동으로 생성전략(기본키생성방법)결정
    private Long id;       //상품 코드

    @Column(nullable = false, length = 50)
    private String itemNm; //상품명

    @Column(name="price", nullable = false)
    private int price; //가격

    @Column(nullable = false)
    private int stockNumber; //재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail; //상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; //상품 판매 상태

    public void updateItem(ItemFormDto itemFormDto){
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int stockNumber){
        int restStock = this.stockNumber - stockNumber;
        if(restStock<0){
            throw new OutOfStockException("상품의 재고가 부족 합니다. (현재 재고 수량: " + this.stockNumber + ")");
        }
        this.stockNumber = restStock;
    }

    public void addStock(int stockNumber){
        this.stockNumber += stockNumber;
    }

}