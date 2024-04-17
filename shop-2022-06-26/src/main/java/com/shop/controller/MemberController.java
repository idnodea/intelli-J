package com.shop.controller;

import com.shop.dto.MemberFormDto;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shop.entity.Member;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.validation.BindingResult;
import javax.validation.Valid;

//page162 회원가입컨트롤러 소스코드 , //page166가입성공하면 메인, 가입실패하면 가입페이지,실패이유출력
//page174.  작성 이후, 스프링시큐리티 테스트,로그인테스트, 멤버콘트롤러테스트 작성
@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String memberForm(Model model){   //회원가입페이지로 이동
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new") //page166가입성공하면 메인,
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){
    //검증하려는 객체 앞에 @Valid어노테이션 선언, 파라미터로 bindingResult객체추가
    //검사 후 결과는 bindingResult에 담아준다. bindingResult.hasErrors()를 호출하여 에러가 있으면 회원가입페이지로    
        
        if(bindingResult.hasErrors()){ //가입실패하면 가입페이지,실패이유출력
            return "member/memberForm";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());//실패이유출력,에러메시지 뷰로 전달
            return "member/memberForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginMember(){
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/member/memberLoginForm";
    }

}