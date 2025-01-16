package com.project.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@Controller
public class MainController {
//    @GetMapping("/")
//    public String get_home(){
//        return "main/home";
//    }
    @GetMapping("/book")
    public String get_book() {
        return "main/book";
    }

    @GetMapping("/category")
    public String get_category() {
        return "main/category";
    }

    @GetMapping("/discussion")
    public String get_discussion() {
        return "main/discussion";
    }

    @GetMapping("/participation")
    public String get_participate() {
        return "main/participation";
    }
}
