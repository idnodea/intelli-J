package com.shop.service;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//page156 멤버서비스 테스트
@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember(){//회원정보를 입력한 엔티티 Member를 만드는 메소드
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest(){
        //Junit의 Assertions클래스의 assertEquals메소드를 이용하여 저장하려고 요청했던 값과
        //실제 저장된 데이터를 비교. 첫번째 파라미터에는 기대값, 두번째 파라미터에는 실제저장된값.
        //기대값,저장된값
        Member member = createMember();
        Member savedMember = memberService.saveMember(member);
        assertEquals(member.getEmail(), savedMember.getEmail()); //저장된 객체를 뒤의 변수에 할당
        assertEquals(member.getName(), savedMember.getName());   //검증
        assertEquals(member.getAddress(), savedMember.getAddress());
        assertEquals(member.getPassword(), savedMember.getPassword());
        assertEquals(member.getRole(), savedMember.getRole());
    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void saveDuplicateMemberTest(){  

        Member member1 = createMember();
        Member member2 = createMember();
        memberService.saveMember(member1);

        //예외처리테스트 Junit의 Assertions클래스의 assertEquals메소드를 이용해 예외처리테스트
        //첫번째 파라미터에는 발생할 예외타입을 넣어준다.
        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(member2);});
        //발생한 예외메시지가 예상결과와 맞는지 검증
        assertEquals("이미 가입된 회원입니다.", e.getMessage());
    }
}