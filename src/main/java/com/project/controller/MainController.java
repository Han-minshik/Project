package com.project.controller;

import com.project.dto.BookDTO;
import com.project.service.BookService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    /*************************************************/

    // 해당 카테고리의 모든 책 리스트 화면
    @GetMapping("/book")
    public String get_book_list(
            PageInfoDTO<BookDTO> bookPageInfo,
            Model model
    ){
        bookService.get_book_list(bookPageInfo);
        model.addAttribute("bookPageInfo", bookPageInfo);
        return "main/book-list";
    }

    @GetMapping("/book/{bookIsbn}")
    public String get_book(
            @PathVariable Integer bookIsbn,
            Model model
    ) {
        BookDTO book = bookService.get_book(bookIsbn);
        List<CategoryDTO> categoryMap =  bookService.get_category_map(book.getCategory().getNo());
        model.addAttribute("book", book);
        model.addAttribute("categoryMap", categoryMap);
        return "main/book";
    }

    /********************************************/
    @GetMapping("/book/{bookIsbn}/review")
    public String get_book_reviews(
            @PathVariable("bookIsbn") Integer bookIsbn,
            PageInfoDTO<ReviewDTO> reviewPageInfo,
            Model model
    ){
        Map<String, Map<String, Object>> rateMap = bookService.get_reviews(reviewPageInfo, bookIsbn);
        model.addAttribute("reviewPageInfo", reviewPageInfo);
        model.addAttribute("rateMap", rateMap);
        return "main/review-template";
    }
    /********************************************/


    @GetMapping("/discussion")
    public String get_discussion_list(){
        return "main/discussion-list";
    }

    @GetMapping("/discussion/{discussionId}")
    public String get_discussion(){
        return "main/discussion";
    }

    /*********************************************/

    // 토론 게시글 작성 페이지
    @GetMapping("/discussion/upload")
    public String get_discussion_upload(
            Authentication auth
    ){
        return "main/discussion-upload";
    }

    //토론 게시글 업로드
    @PostMapping("/discussion/upload")
    public String post_discussion_upload(
            Authentication auth
    ){
        return "main/discussion-upload";
    }

    // 토론 게시글 수정 페이지
    @GetMapping("/discussion/{discussionId}/update")
    public String get_discussion_update(
            @PathVariable Integer discussionId,
            Authentication auth
    ){
        return "main/discussion-upload";
    }

    // 토론 게시글 수정(업데이트)
    @PatchMapping("/discussion/{discussionId}/update")
    public String patch_discussion_update(
            @PathVariable Integer discussionId,
            Authentication auth
    ){
        return "main/discussion-update";
    }

    // 토론 게시글 삭제
    @DeleteMapping("/discussion/{discussionId}/delete")
    public String delete_discussion(
            @PathVariable Integer discussionId,
            Authentication auth
    ){
        return "main/discussion-delete";
    }



}
