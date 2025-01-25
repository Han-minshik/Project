package com.project.controller;

import com.project.dto.*;
import com.project.mapper.AdminMapper;
import com.project.mapper.LoanMapper;
import com.project.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import com.project.mapper.UserMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Log4j2
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired private UserService userService;
    @Autowired private UserMapper userMapper;
    @Autowired private PortOneSerivce portOneSerivce;
    @Autowired private AdminMapper adminMapper;
    @Autowired private BookService bookService;
    @Autowired private LoanMapper loanMapper;
    @Autowired private LoanService loanService;
    @Autowired private DiscussionService discussionService;

    /***********************************************/

    @GetMapping("/join")
    public String get_join(Authentication auth) {
        if (auth != null) {
            System.out.println("회원가입 할 필요 없습니다");
            return "redirect:/";
        }
        return "user/join";
    }

    @PostMapping("/join")
    public String post_join(
            @ModelAttribute @Validated UserDTO joinUser,
            BindingResult bindingResult,
            HttpSession session
    ) {
        if (bindingResult.hasErrors()) { // 유효성 검사 실패 시
            log.error("에러 발생!");
            log.error(bindingResult.getAllErrors());
            return "user/join";
        }
        // 전화번호 인증 확인 여부
        String impUid = (String) session.getAttribute("impUid");
        if(impUid == null) {
            log.error("전화번호 인증 확인 실패");
            return "user/join";
        }
        // 포트원 인증 통과 여부
        String ci = portOneSerivce.tel_authentication(impUid, joinUser.getTel());
        if(ci == null) {
            log.error("포트원 인증 확인 실패");
            return "user/join";
        }
        joinUser.setCi(ci);

        // 이메일 인증 확인 여부
        String certCompleteEmail = (String) session.getAttribute("emailAuth");
        if(certCompleteEmail == null || !certCompleteEmail.equals(joinUser.getEmail())) {
            log.error("이메일 인증 확인 실패");
            return "user/join";
        }

        log.info("가입할 user" + joinUser);
        boolean signUpResult = userService.join_user(joinUser);
        if (signUpResult) {
            log.info("가입 완료");
            return "redirect:/user/login";
        }
        log.error("원인 모를 이유로 실패");
        return "user/join";
    }

    /***********************************************/
    @GetMapping("/login")
    public String get_login(Authentication auth) {
        if (auth != null) {
            System.out.println("로그인 할 필요 없습니다");
            System.out.println("이미 로그인된 유저 : " + auth.getName());
            return "redirect:/";
        }

        System.out.println("로그인 안되어있음");
        return "user/login";
    }


    /***********************************************/
    // 내 회원 정보 메뉴
    @GetMapping("/mypage")
    public String get_my_page(Authentication auth, Model model) {
        if (auth != null) {
            String userId = auth.getName();
            UserDTO user = userMapper.getUserById(userId);
            if (user.getProfileImage() != null) {
                String base64Image = "data:image/jpeg;base64," +
                        Base64.getEncoder().encodeToString(user.getProfileImage());
                user.setBase64Image(base64Image);
            } else {
                user.setBase64Image("data:image/jpeg;base64,[defaultBase64EncodedImage]");
            }
            model.addAttribute("user", user);
            return "user/my-page";
        }
        return "redirect:/user/login";
    }



    /************************************************/
    @GetMapping("/discussion")
    public String get_my_discussion(Authentication auth, Model model, PageInfoDTO<DiscussionDTO> pageInfo) {
        if(auth != null) {
            String userId = auth.getName();
            PageInfoDTO<DiscussionDTO> myDiscussion =  discussionService.getMyDiscussion(new PageInfoDTO<>(), userId);
            Integer myDiscussionCount = myDiscussion.getTotalElementCount();
            model.addAttribute("myDiscussionCount", myDiscussionCount);
            model.addAttribute("myDiscussion", myDiscussion);
            model.addAttribute("pageInfo", pageInfo);
            log.error(myDiscussion);
            log.error(myDiscussionCount);
            return "user/my-talk";
        }
        return "redirect:/user/login";
    }

    /************************************************/

    @GetMapping("/lendbook")
    public String get_lendbook(Authentication auth, Model model, PageInfoDTO<CartDTO> pageInfo) {
        if (auth != null) {
            String userId = auth.getName();
            Integer activeLoanCount = loanService.getActiveLoanCountByUserId(userId);
            try {
                Map<LoanDTO, BookDTO> loanBookMap = loanService.getActiveLoanByUser(userId);
                model.addAttribute("activeLoanCount", activeLoanCount);
                model.addAttribute("loanBookMap", loanBookMap);
                model.addAttribute("pageInfo", pageInfo);
                log.error(activeLoanCount);
                log.error(loanBookMap);
            } catch (IllegalArgumentException e) {
                // 대출 중인 책이 없을 경우 처리
                model.addAttribute("loanBookMap", null);
            }
            return "user/lendbook";
        }
        return "redirect:/user/login";
    }

    @GetMapping("/lendbook/all")
    public String get_lendbook_all(
            Authentication auth,
            Model model,
            PageInfoDTO<CartDTO> pageInfo
    ){
        if (auth != null) {
            List<LoanDTO> allLendBooks = loanService.getLoansByUserId(auth.getName());
            model.addAttribute("allLendBooks", allLendBooks);
            model.addAttribute("pageInfo", pageInfo);
            return "user/lendbook";
        }
        return "redirect:/user/login";
    }

    // 책 대출하기
    @PostMapping("/lendbook/lend")
    public String post_lendbook_lend(
            Authentication auth,
            @ModelAttribute LoanDTO lendbook,
            @RequestParam Integer points
    ){
        if (auth != null) {
            loanService.createLoanWithPoints(lendbook, points);
            return "redirect:/user/lendbook";
        }
        return "redirect:/user/login";
    }

    /************************************************/

    @GetMapping("/wishlist")
    public String get_wishlist(
            Authentication auth,
            Model model,
            PageInfoDTO<CartDTO> pageInfo
    ) {
        if (auth != null) {
            String userId = auth.getName();
            PageInfoDTO<CartDTO> wishlist = bookService.getCartsByUser(pageInfo, userId);
            model.addAttribute("pageInfo", pageInfo);
            model.addAttribute("totalCount", wishlist.getTotalElementCount());
            model.addAttribute("wishlist", wishlist);
            log.error(wishlist);
            log.error(wishlist.getTotalElementCount());
            return "user/wishlist";
        }
        return "redirect:/user/login";
    }

    @GetMapping("/wishlist/add")
    public String get_wishlist_add(
            Authentication auth,
            @RequestParam String bookIsbn
    ) {
        bookService.insertBookToCart(auth.getName(), bookIsbn);
        return "redirect:/user/wishlist";
    }

    // 회원 정보 수정
    @GetMapping("/info-revise")
    public String get_user_info_revise(
            @AuthenticationPrincipal UserDTO user,
            Model model
    ) {
        if (user != null) {
//            UserDTO user = userMapper.getUserById(auth.getName()); // 유저 정보 가져옴
//            user.setPassword(null); // 비밀번호 유출 안되게
            model.addAttribute("user", user); // 유저 정보를 템플릿에 넘김
            return "user/info-revise";
        }
        return "redirect:/user/login";
    }

    // 아마도 userRestController로 가야할지도
    @PostMapping("/info-revise")
    public String post_user_info_revise(
            Authentication auth,
            @ModelAttribute @Validated UserDTO user,
            BindingResult bindingResult
    ){
        if(auth == null || !auth.getName().equals(user.getId())){
            return "redirect:/user/login";
        }
        if(bindingResult.hasErrors()) {
            return "user/info-revise";
        }

        userMapper.updateUser(user);
        return "redirect:/";

    }

    /*********************** 공지사항 *********************/
    // 어쩌면 메인으로 가야하는건가?
    // 공지사항 목록
    @GetMapping("/adminPost")
    public String get_allAdminPost(
            Authentication auth,
            Model model
    ){
        if (auth != null) {
            List<AdminPostDTO> allAdminPost = adminMapper.getAllAdminPosts();
            model.addAttribute("allAdminPost", allAdminPost);
            return "user/all-admin-post";
        }
        return "redirect:/user/login";
    }

    // 공지사항 한 페이지
    @GetMapping("/adminPost/{adminPostId}")
    public String get_adminPost(
            Authentication auth,
            @PathVariable Integer adminPostId,
            Model model
    ){
        if (auth != null) {
            List<AdminPostDTO> adminPost = adminMapper.getAdminPostById(adminPostId);
            model.addAttribute("adminPost", adminPost);
            return "user/admin-post";
        }
        return "redirect:/user/login";
    }

    /******************************/
    // 탈퇴
    @GetMapping("/resign")
    public void get_user_resign(){}

    @PostMapping("/resign")
    public String post_user_resign(
            Authentication auth
    ){
        if (auth != null) {
            userMapper.deleteUser(auth.getName());
        }
        return "redirect:/user/logout";
    }
}