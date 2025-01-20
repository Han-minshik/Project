package com.project.controller;

import com.project.dto.AdminPostDTO;
import com.project.dto.BookDTO;
import com.project.dto.UserDTO;
import com.project.mapper.AdminMapper;
import com.project.mapper.BookMapper;
import com.project.mapper.UserMapper;
import com.project.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired private BookMapper bookMapper;
    @Autowired private UserMapper userMapper;
    @Autowired private AdminService adminService;
    @Autowired private AdminMapper adminMapper;

    /**********************************************/



    /*********************************************/
    @GetMapping("/add-book")
    public void insertBook(){}

    @PostMapping("/add-book")
    public String insertBook(
            @AuthenticationPrincipal UserDTO user,
            BookDTO book
    ){
        if(user.getRole().equals("관리자")){
            adminService.insertBook(book);
            return "redirect:/admin/books";
        }
        return "redirect:/";
    }

    /*******************************************/

    @GetMapping("/update-book")
    public void updateBook(){}

//    @PatchMapping("/update-book")
//    public String updateBook(BookDTO book) {
//        bookMapper.updateBook(book);
//        return "redirect:/admin/books";
//    }

    /********************************************/

    @GetMapping("/delete-book")
    public void deleteBook(){}

//    @DeleteMapping("/delete-book")
//    public String deleteBook(
//            @RequestParam("bookIsbn") int bookIsbn
//    ) {
//        bookMapper.deleteBook(bookIsbn);
//        return "redirect:/admin/books";
//    }

    /******************* 강제 퇴출 ********************/

    @GetMapping("/drop-user")
    public void dropUser() {}

    // 아마도 RestController로 가야할 듯
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

//    @PatchMapping("/delete-review") // 완전 삭제는 아니고, 삭제된 걸로 바꾸는 트릭
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


    /********************* 공지 사항 **********************/
    @PostMapping("/addAdminPost")
    public String post_addAdminPost(
            @AuthenticationPrincipal UserDTO user,
            AdminPostDTO adminPost
    ){
        if(user.getRole().equals("admin")){
            adminMapper.createAdminPost(adminPost);
            return "redirect:/admin/admins";
        }
        return "redirect:/user/login";
    }

    @PatchMapping("/updateAdminPost")
    public String updateAdminPost(
            @AuthenticationPrincipal UserDTO user,
            AdminPostDTO adminPost
    ){
        if(user.getRole().equals("admin")){
            adminMapper.updateAdminPost(adminPost);
            return "redirect:/admin/admins";
        }
        return "redirect:/user/login";
    }

    @DeleteMapping("/deleteAdminPost")
    public String deleteAdminPost(
            @AuthenticationPrincipal UserDTO user,
            Integer adminPostId
    ){
        if(user.getRole().equals("admin")){
            adminMapper.deleteAdminPost(adminPostId);
            return "redirect:/admin/admins";
        }
        return "redirect:/user/login";
    }


}
