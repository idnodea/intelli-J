package com.sky._sb0402.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyController {

    @GetMapping("/")
    public String Main(){
        return  "main";
    }
}
