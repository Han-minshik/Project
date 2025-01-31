package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/header")
    public String getHeader() {
        return "main/home_header";  // resources/templates/main/home_header.html 뷰 반환
    }

    @GetMapping("/footer")
    public String getFooter() {
        return "main/home_footer";  // resources/templates/main/home_footer.html 뷰 반환
    }
}
