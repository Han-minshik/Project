package com.project.controller;

import com.project.dto.DiscussionDTO;
import com.project.dto.ReviewDTO;
import com.project.service.BookService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Log4j2
@Controller
public class MainController {
    @Autowired private BookService bookService;

    @GetMapping("/")
    public String get_home(){
        return "main/home";
    }

    @GetMapping("/test-page")
    public String get_test_page(){
        return "main/test-page";
    }

    /**********************************************/
    // 책 페이지 불러오기
    @GetMapping("/book/{bookIsbn}")
    public String get_book(@PathVariable Integer bookIsbn){
        return "main/book";
    }

    // 책 리뷰 불러오기
    // js로 태그 작성 같은걸 붙인다
    @GetMapping("/book/{bookIsbn}/review")
    public String get_book_review(
            @PathVariable Integer bookIsbn,
            PageInfoDTO<ReviewDTO> pageInfo,
            Model model
    ){
        Map<String, Map<String, Object>> rateMap = bookService.findReviewTitlesByBookTitle() // ??
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("rateMap", rateMap);
        return "main/review-template";
    }
    /********************************************/
    // 토론 페이지 불러오기
    @GetMapping("/discussion/{discussion_id}")
    public String get_discussion(
            @PathVariable Integer discussion_id,
            Model model
    ){
//        DiscussionDTO discussion = discussionMapper.get
        model.addAttribute("discussion", discussion);
        return "main/discussion";
    }

    // 토론 페이지 댓글 불러오기 (어쩌면 fetch로 해야할지도)
    @GetMapping("/discussion/{discussion_id}/comment")
    public String get_discussion_comment(
            @PathVariable Integer discussion_id,
            Model model
    ){
//        model.addAttribute("discussion-comment", discussion_comment);
        return "main/discussion-comment";
    }
    /*********************************************/
}
