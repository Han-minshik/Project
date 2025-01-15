package com.project.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@Controller
public class MainController {

    @GetMapping("/")
    public String get_home(){
        return "main/home";
    }

    @GetMapping("/test-page")
    public String get_test_page(){
        return "main/test-page";
    }


}
