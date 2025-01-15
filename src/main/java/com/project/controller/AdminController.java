package com.project.controller;

import com.project.dto.BookDTO;
import com.project.mapper.BookMapper;
import com.project.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired private BookMapper bookMapper;
    @Autowired private UserMapper userMapper;

//    @GetMapping("/add-book")
//    public void addBook(){}

//    @PostMapping("/add-book")
//    public String addBook(BookDTO book){
//        bookMapper.createBook(book);
//        return "redirect:/admin/books";
//    }

    /*******************************************/

//    @GetMapping("/update-book")
//    public void updateBook(){}

//    @PatchMapping("/update-book")
//    public String updateBook(BookDTO book) {
//        bookMapper.updateBook(book);
//        return "redirect:/admin/books";
//    }

    /********************************************/

//    @GetMapping("/delete-book")
//    public void deleteBook(){}
//    @DeleteMapping("/delete-book")
//    public String deleteBook(
//            @RequestParam("bookIsbn") int bookIsbn
//    ) {
//        bookMapper.deleteBook(bookIsbn);
//        return "redirect:/admin/books";
//    }

    /******************* 강제 퇴출 ********************/

//    @GetMapping("/drop-user")
//    public String dropUser() {}
//
//    @PostMapping("/drop-user")
//    public String dropUser(@RequestParam String id) {
//        userMapper.deleteUser(username);
//        return "redirect:/admin/drop-user";
//    }

    /****************** 규칙 위반 게시글 삭제 *******************/

//    @DeleteMapping("/delete-discussion")
//    public String deleteDiscussion(@RequestParam("discussionId") int discussionId) {
//        return "redirect:/admin/deleted-discussion-page";
//        "규정 위반으로 삭제된 게시글입니다"
//    }

    /****************** 규칙 위반 리뷰 삭제 ******************/

//    @PatchMapping("/delete-review")
//    public String deleteReview(@RequestParam("id") Integer id) {
//        return "redirect:/admin/reviews";
//        "규정 위반으로 삭제된 리뷰입니다"
//    }

    /****************** 규칙 위반 댓글 삭제 ********************/

//    @PatchMapping("/delete-comment")
//    public String deleteComment(@RequestParam("id") Integer id) {
//        return "redirect:/admin/books/" + id;
//        "규정 위반으로 삭제된 댓글입니다"
//    }


}
