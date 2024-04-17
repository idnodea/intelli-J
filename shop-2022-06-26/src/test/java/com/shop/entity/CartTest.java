package com.shop.entity;

import com.shop.dto.MemberFormDto;
import com.shop.repository.CartRepository;
import com.shop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

//p195  엔티티 cart, cart리포지토리 작성 후
@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")

class CartTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    //@PersistenceContext는 Java Persistence API (JPA)에서 사용되는 애노테이션
    //엔티티 매니저 (EntityManager)를 해당 컴포넌트에 주입하기 위해 사용됩니다.
    //엔티티 매니저는 JPA에서 제공하는 메인 인터페이스로, 데이터베이스 엔티티와의
    //상호작용을 관리합니다. 이 애노테이션은 보통 서비스 또는 DAO/리포지토리 계층의
    //클래스에 적용되어 JPA의 영속성 컨텍스트와의 연결을 제공합니다.

    //@PersistenceContext의 기능
    //엔티티 매니저 주입: 스프링 프레임워크는 @PersistenceContext 애노테이션을 사용
    //하여 선언된 필드나 메소드에 EntityManager 인스턴스를 자동으로 주입합니다.
    //이는 개발자가 수동으로 EntityManager를 생성하고 관리할 필요가 없게 해 줍니다.

    //트랜잭션 관리: 주입된 EntityManager는 현재 트랜잭션과 연관된 영속성 컨텍스트에
    //바인딩됩니다. 이를 통해 엔티티에 대한 모든 변경사항은 트랜잭션이 커밋될 때
    //데이터베이스에 자동으로 반영됩니다.

    //스레드 안전성: EntityManager는 자체적으로 스레드에 안전하지 않지만,
    // @PersistenceContext를 사용하면 스프링이 관리하는 스레드-안전한 방식으로
    //엔티티 매니저를 제공합니다. 각 트랜잭션마다 새로운 EntityManager가 제공될 수 있어,
    //병렬 처리에서도 안전하게 사용할 수 있습니다.
    EntityManager em;

    public Member createMember(){  //회원엔티티를 생성하는 메서드
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    public void findCartAndMemberTest(){
        Member member = createMember();
        memberRepository.save(member);
        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);

        em.flush();
        //JPA는 영속성 컨텍스트에 데이터를 저장 후 트랜잭션이 끝날 때 flush()를
        //호출하여 데이터베이스에 반영. 회원 엔티티와 장바구니 엔티티를
        //영속성 컨텍스트에 저장 후 엔티티 매니저로부터 강제로 flush()를 호출하여
        //데이터베이스에 반영

        em.clear();
        //JPA는 영속성 컨텍스트로부터 엔티티를 조회 후 영속성 컨텍스트에 엔티티가 없을 경우
        //데이터베이스를 조회합니다. 실제 데이터베이스에서 장바구니 엔티티를 가지고 올 때
        //회원 엔티티도 같이 가지고오는지 보기 위해 영속성 컨텍스트를 비워서 테스트

        Cart savedCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityNotFoundException::new);
        //저장된 장바구니 엔티티를 조회

        assertEquals(savedCart.getMember().getId(), member.getId());
        //처음에 저장한 member엔티티의 id와 savedCart에 매핑된 member엔티티의 id를 비교
    }

}