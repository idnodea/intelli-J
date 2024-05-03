package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

//MemberFormDto는 가입정보를, 엔티티 Member는 회원정보를 저장
@Entity
@Table(name="member")
@Getter @Setter
@ToString
public class Member extends BaseEntity { 
    //page225어디팅을적용,베이스엔티티상속 //이후, 다른 엔티티들도 BaseEntity상속

    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)    //회원은 이메일을 통해 유일하게 구분되어야하므로 유니크값
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)  //자바의 enum타입을 엔티티속성으로 지정가능함.
    private Role role;
    //다만, Enum을 사용시, 기본적으로 순서가 저장되는데 만약 enum의 순서를 바꾼다면??
    //EnumType.String으로 해서 스트링으로 저장되게끔.(최소한)  ->변수타입의 일치만큼은 지킬 수 있도록.
    //enum Role에 다시 설명적어둠.

    //엔티티 Member를 생성하는 메소드.
    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        //스프링시큐리티B크립트패스워드 엔코더 Bean을 파라미터로 넘긴다-이후 콘피그시큐리티에서 처리.(암호화)
        member.setPassword(password);
        
        //엔티티 Member 생성 시 USER Role로 생성하던 권한을 ADMIN Role로 생성하도록 수정 
        member.setRole(Role.ADMIN);  
        
        return member;
    }

}
