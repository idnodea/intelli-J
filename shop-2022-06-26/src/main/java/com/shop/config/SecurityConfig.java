package com.shop.config;

import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//page150
//스프링시큐리티 5.4부터  상속과 오버라이드=>빈을 직접등록하는 새로운 방법으로 변경
//확장성
@Configuration
@EnableWebSecurity      //시큐리티를 위한
public class SecurityConfig {   //책처럼 상속되어야할텐데???
    //public class SecurityConfig extends WebSecurityConfigurerAdapter

    @Autowired
    MemberService memberService;


    @Bean     //책에선 override로 되어있음. 완성코드에선 bean
    //Override
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //http요청에 대한 보안설정
        //페이지권한설정, 로그인페이지설정,로그아웃메소드
        http.formLogin()
                .loginPage("/members/login")  //로그인페이지url
                .defaultSuccessUrl("/")        //로그인성공시 이동할 url
                .usernameParameter("email")    //로그인시 사용할 파라미터의 이름
                .failureUrl("/members/login/error") //로그인 실패시 이동할 url
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) //로그아웃url설정
                .logoutSuccessUrl("/")//로그아웃성공시 이동할 url
        ;

        //시큐리티 처리에 HttpServletRequest를 이요한다는 것을 의미합니다.
        http.authorizeRequests()
                //permitAll()을 통해 모든 사용자가 인증(로그인)없이 해당 경로에 접근할 수 있도록 설정
                //메인 페이지, 회원관련URl, 뒤에서 만들 상품 상세 페이지, 상품 이미지를 불러오는 경로가 이에 해당
                .mvcMatchers("/css/**", "/js/**", "/img/**").permitAll()
                .mvcMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
               //admin으로 시작하는 경로는 해당 계정이 ADMIN Role일 경우에만 접근 가능
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
        ;

        http.exceptionHandling()
                //인증되지 않은 사용자가 리소스에 접근하였을 때 수행되는 핸들러를 등록합니다.
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        ;

        return http.build();
    }

    @Bean    //B크립트패스워드인코더의 해시함수를 이용해, 비밀번호를 암호화하여 저장
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    //p172 스프링시큐리티에서의 인증.  매니저,    빌더는 매니저를 생성.
    //userDetailService를 구현하고 있는 객체로 memberService를 지정해주며,
    // 비밀번호 암호화를 위해 passwordEncoder를 지정해줌
//    @Override
//    protected  void configure(AuthenticationManagerBuilder auth) throws  Exception{
//        auth.userDetailsService(memberService)
//                .passwordEncoder(passwordEncoder());

//        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**"); 
//        //스태틱 디렉터리의 하위파일은 인증무시하도록 설정
//    }

}