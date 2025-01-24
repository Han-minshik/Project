package com.project.controller;

import com.project.dto.AdminPostDTO;
import com.project.dto.BookDTO;
import com.project.dto.PageInfoDTO;
import com.project.dto.UserDTO;
import com.project.mapper.AdminMapper;
import com.project.mapper.BookMapper;
import com.project.mapper.UserMapper;
import com.project.service.AdminService;
import com.project.service.BookService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger log = LogManager.getLogger(AdminController.class);
    @Autowired private BookMapper bookMapper;
    @Autowired private UserMapper userMapper;
    @Autowired private AdminService adminService;
    @Autowired private AdminMapper adminMapper;
    @Autowired private BookService bookService;

    /*********************문의사항 조회*************************/



    /*********************************************/
    @GetMapping("/book/add")
    public void insertBook(){}

    @PostMapping("/book/add")
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

//    @GetMapping("/book/update")
//    public String updateBook(@AuthenticationPrincipal UserDTO user,
//                             Model model){
//
//    }

    @PatchMapping("/book/update")
    public String updateBook(
            @AuthenticationPrincipal UserDTO user,
            BookDTO book
    ) {
        if(user.getRole().equals("관리자")){
            adminService.updateBook(book);
            return "redirect:/admin/books";

        }
        return "redirect:/";
    }

    /********************************************/

    @GetMapping("/book/delete")
    public String deleteBook(@AuthenticationPrincipal UserDTO user,
                             Model model){
        if(user.getRole().equals("관리자")) {
            List<BookDTO> books = bookService.getAllBooks();
            model.addAttribute("books", books);
            return "redirect:/admin/books";
        }
        return "redirect:/";
    }

    @DeleteMapping("/book/delete")
    public String deleteBook(
            @AuthenticationPrincipal UserDTO user,
            @RequestParam("bookIsbn") String bookIsbn
    ) {
        if(user.getRole().equals("관리자")){
            adminService.deleteBook(bookIsbn);
            return "redirect:/admin/books";
        }
        return "redirect:/";
    }

    /******************* 유저 관련 ********************/
    @GetMapping("/admin/user")
    public String getUserList(@AuthenticationPrincipal UserDTO user, Model model, PageInfoDTO<UserDTO> pageInfo) {
        if(user.getRole().equals("관리자")) {
            List<UserDTO> users = adminService.getAllUser();
            List<UserDTO> updatedUsers = adminService.getRecentlyUpdatedUsers();
            model.addAttribute("updatedUsers", updatedUsers);
            model.addAttribute("users", users);
            model.addAttribute("pageInfo", pageInfo);
            log.error(updatedUsers);
            log.error(users);
            return "redirect:/admin/user";
        }
        return "redirect:/";
    }

    @PostMapping("/update-user")
    public String updateUser(@AuthenticationPrincipal UserDTO user, @RequestParam String userId) {
        if(user.getRole().equals("관리자")) {
            adminService.promoteToAdmin(userId);
            log.error(userId);
            return "redirect:/admin/user";
        }
        return "redirect:/";
    }

    @PostMapping("/drop-user")
    public String dropUser(@AuthenticationPrincipal UserDTO user, @RequestParam String userId) {
        if(user.getRole().equals("관리자")) {
            adminService.deleteUser(userId);
            log.error(userId);
            return "redirect:/admin/user";
        }
        return "redirect:/";
    }

    // 아마도 RestController로 가야할 듯
//    @PostMapping("/drop-user")
//    public String dropUser(@RequestParam String id) {
//        userMapper.deleteUser(username);
//        return "redirect:/admin/drop-user";
//    }

//    /****************** 규칙 위반 게시글 삭제 *******************/
//
//    @DeleteMapping("/discussion/delete")
//    public String deleteDiscussion(@RequestParam("discussionId") Integer discussionId) {
//        return "redirect:/admin/deleted-discussion-page";
//        // "규정 위반으로 삭제된 게시글입니다"
//    }
//
//    /****************** 규칙 위반 리뷰 삭제 ******************/
//
//    @PatchMapping("/review/delete") // 완전 삭제는 아니고, 삭제된 걸로 바꾸는 트릭
//    public String deleteReview(@RequestParam("id") Integer id) {
//        return "redirect:/admin/reviews";
//        // "규정 위반으로 삭제된 리뷰입니다"
//    }
//
//    /****************** 규칙 위반 댓글 삭제 ********************/
//
//    @PatchMapping("/comment/delete")
//    public String deleteComment(@RequestParam("id") Integer id) {
//        return "redirect:/admin/books/" + id;
//        // "규정 위반으로 삭제된 댓글입니다"
//    }
//
//
//    /********************* 공지 사항 **********************/
//    @GetMapping("/adminPost")
//    public String get_adminPost(
//            @AuthenticationPrincipal UserDTO user,
//            Model model
//    ){
//        if(user.getRole().equals("admin")){
//            List<AdminPostDTO> posts = adminService.getAllAdminPosts();
//            model.addAttribute("posts", posts);
//            return "redirect:/admin/posts";
//
//        }
//        return "redirect:/user/login";
//    }
//
//    //특정 공지사항 조회
//    @GetMapping("/adminPost/{adminPostId}")
//    public String get_adminPost_by_id(
//            @AuthenticationPrincipal UserDTO user,
//            @PathVariable Integer adminPostId,
//            Model model
//    ){
//        if(user.getRole().equals("admin")){
//            AdminPostDTO adminPost = adminService.getAdminPostById(adminPostId);
//            model.addAttribute("adminPost", adminPost);
//            return "redirect:/admin/posts";
//        }
//        return "redirect:/user/login";
//    }
//
//    @PostMapping("/adminPost/add")
//    public String post_addAdminPost(
//            @AuthenticationPrincipal UserDTO user,
//            AdminPostDTO adminPost
//    ){
//        if(user.getRole().equals("admin")){
//            adminService.addAdminPost(adminPost);
//            return "redirect:/admin/admins";
//        }
//        return "redirect:/user/login";
//    }
//
//    @PatchMapping("/adminPost/update")
//    public String updateAdminPost(
//            @AuthenticationPrincipal UserDTO user,
//            AdminPostDTO adminPost
//    ){
//        if(user.getRole().equals("admin")){
//            adminService.updateAdminPost(adminPost);
//            return "redirect:/admin/admins";
//        }
//        return "redirect:/user/login";
//    }
//
//    @DeleteMapping("/adminPost/delete")
//    public String deleteAdminPost(
//            @AuthenticationPrincipal UserDTO user,
//            Integer adminPostId
//    ){
//        if(user.getRole().equals("admin")){
//            adminService.deleteAdminPost(adminPostId);
//            return "redirect:/admin/admins";
//        }
//        return "redirect:/user/login";
//    }


}