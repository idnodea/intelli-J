package com.sky._sb0409.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class MainController {


    @GetMapping("/")
    public String MainPage(Model model){
        model.addAttribute("value1","안녕");
        model.addAttribute("value2","안녕하세요");
        return "main";
    }
    @ModelAttribute
    public void case1(Model model){
        model.addAttribute("value1","hello?");
    }

    @ModelAttribute("value2")
    public String case2(){
        return "반가워요";
    }

}
