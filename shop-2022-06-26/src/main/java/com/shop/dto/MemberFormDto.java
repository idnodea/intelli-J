package com.shop.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

//가입정보를 담을 Dto,  p165에서 추가
@Getter @Setter
public class MemberFormDto {

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min=8, max=16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요")
    private String password;

    @NotEmpty(message = "주소는 필수 입력 값입니다.")
    private String address;


    //pom.xml에서 스프링부트스타터-발리데이션
    //@NotEmpty Null체크 및 문자열의 경우 길이0인지 검사
    //@NotBlank Null체크 및 문자열의 경우 길이0 및 빈 문자열("") 검사
    //@Length(min=,max=) 최소,최대 길이 검사
    //@Email 이메일 형식인지 검사
    //@Max @Min  지정한 값보다 큰지, 작은지 검사  
    //@Null널인지 @NotNull 널이 아닌지 검사
}