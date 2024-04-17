package com.shop.service;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

//page155
@Service    //늘 꼭 확인하자. 서비스
@Transactional  //에러가 발생하더라도, 로직수행 이전상태로 콜백시켜줌
@RequiredArgsConstructor  
public class MemberService implements UserDetailsService {
//빈 주입 1.오토와이어드 어노테이션, 필드(세터)주입, 생성자 주입\
//@RequiredArgConstructor 어노테이션은 final이나 @NonNull이 붙은 필드에 생성자를 생성해줌

//빈에 생성자가 1개이고,   -----스프링부트의 놀라운 기능...
// 생성자의 파라미터 타입이 빈으로 등록가능하다면 @Autowired어노테이션없이 의존성주입이 가능함
    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member){ //이미 가입된 회원이라면 예외발생
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    //p170  유저디테일스서비스 인터페이스의 로드유저바이유저네임() 메소드를 오버라이딩.로그인할 유저의 이메일을 파라미터로 전달받음
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }

        return User.builder() //유저디테일을 구현하고 있는 유저 객체를 반환. 유저 객체를 생성하기 위해 생성자로 회원의 이메일, 비밀번호, role을 파라미터 넘겨준다
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

    //p169
    //UserDetailsService
    //UserDetailService 인터페이스는 데이터베이스에서 회원정보를 가져오는 역할 담당
    //loadUserByUsername()메소드가 존재하며, 회원정보를 조회하여 사용자의 정보와 권한을 갖는
    //UserDetails인터페이스를 반환한다
    //스프링 시큐리티에서 UserDetailService를 구현하고 있는 클래스를 통해 로그인기능을 구현
    
    //UserDetail
    //스프링 시큐리티에서 회원의 정보를 담기 위해서 사용하는 인터페이스는 UserDetails
    //이 인터페이스를 직접 구현하거나 스프링시큐리티에서 제공하는 User클래스를 사용
    //User클래스는 UserDetails인터페이스를 구현하고 있는 클래스

    //로그인/로그아웃 구현
    //로그인 기능 구현을 위해 기존에 만들었던 MemberService가 UserDetailsService를 구현
}