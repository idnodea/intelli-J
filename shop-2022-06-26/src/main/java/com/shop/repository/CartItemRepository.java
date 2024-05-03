package com.shop.repository;

import com.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.dto.CartDetailDto;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

//카트 아이디와 상품 아이디를 이용해서 상품이 장바구니에 들어있는지 조회
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    //page 344 장바구니 페이지에 전달할 CartDetailDto리시트를 쿼리 하나로
    //조회하는 JPQL문을 작성
    //연관관계매핑을 지연로딩으로 설정할 경우, 엔티티에 매핑된 다른 엔티티를
    //조회할 때 추가적으로 쿼리문이 실행됨
    //성능최적화를 위해 DTO의 생성자를 이용하여 반환값으로 DTO객체를 생성
    @Query("select new com.shop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
   //CartDetailDto의 생성자를 이용하여 DTO를 반환할 때는
   // "new com.shop.dto.CartDetailDto
   // (ci, id, i.itemNm, i.Price, ci.count, im.imgUrl)처럼
   //new 키워드와 해당DTO의 패키지, 클래스명을 적어줌.
   // 또한 생성자의 파라미터순서는  DTO클래스에 명시한 순으로

            "from CartItem ci, ItemImg im " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "and im.item.id = ci.item.id " +
            "and im.repimgYn = 'Y' " +
            "order by ci.regTime desc"
            )
    List<CartDetailDto> findCartDetailDtoList(Long cartId);

}