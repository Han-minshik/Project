package com.project.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import com.project.dto.UserDTO;
import com.project.mapper.UserMapper;
import com.project.service.PortOneSerivce;
import com.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired private UserService userService;
    @Autowired private UserMapper userMapper;
    @Autowired private PortOneSerivce portOneSerivce;

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
            @ModelAttribute UserDTO user,
            HttpSession session
    ) {
        // 전화번호 인증 확인 여부
        String impUid = (String) session.getAttribute("impUid");
        if(impUid == null) {
            log.error("전화번호 인증 확인 실패");
            return "user/join";
        }
        // 포트원 인증 통과 여부
        String ci = portOneSerivce.tel_authentication(impUid, user.getTel());
        if(ci == null) {
            log.error("포트원 인증 확인 실패");
            return "user/join";
        }
        user.setCi(ci);

        // 이메일 인증 확인 여부
        String certCompleteEmail = (String) session.getAttribute("emailAuth");
        if(certCompleteEmail == null || !certCompleteEmail.equals(user.getEmail())) {
            log.error("이메일 인증 확인 실패");
            return "user/join";
        }

        log.info("가입할 user" + user);
        boolean signUpResult = userService.join_user(user);
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

    /************************************************/

    @GetMapping("/findId")
    public void get_findId(){}

    @GetMapping("/changePw")
    public String get_changePw(
        Authentication auth,
        String oldPw,
        String newPw
    ){
        String id = auth.getName();
        boolean changePwResult =  userService.change_password(id, oldPw, newPw);
        if (changePwResult){
            return "redirect:/";
        }
        return "user/changePw";
    }

    /******************************************/





}
