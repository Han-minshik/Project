package com.project.controller;

import com.project.dto.BookDTO;
import com.project.dto.UserDTO;
import com.project.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import com.project.mapper.UserMapper;
import com.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/user")
public class UserRestController {
    @Autowired private UserMapper userMapper;
    @Autowired private UserService userService;
    @Autowired private EmailService emailService;
    private final Map<String, String> emailCertRepository = new HashMap<>();

    /*******************************************/
    @GetMapping("/id/{userId}")
    public ResponseEntity<Void> find_user_by_id(
            @PathVariable String userId
    ) {
        if(userMapper.getUserById(userId) == null){ // 중복 아니면 200
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FOUND).build(); // 중복이면
    }

//    @GetMapping("/name/{userName}")
//    public ResponseEntity<Void> find_user_by_nickname(
//            @PathVariable String userName
//    ) {
//        if(userMapper.selectUserByName(userName) == null){ // 중복 아니면 200
//            return ResponseEntity.ok().build();
//        }
//        return ResponseEntity.status(HttpStatus.FOUND).build(); // 중복이면
//    }
    /**********************************************************/
    // 전화번호 인증
    @PostMapping("/tel/auth")
    public ResponseEntity<Void> post_tel_auth(
            @RequestBody String impUid,
            HttpSession session
    ) {
        session.setAttribute("impUid", impUid);
        return ResponseEntity.ok().build();
    }
    /*********************************************************/
    // 이메일 인증 확인 버튼 클릭
    @GetMapping("/email/auth")
    public ResponseEntity<Void> get_email_auth(
            @RequestParam String email,
            @RequestParam String certNumber,
            HttpSession session
    ) {
        log.info("email = " + emailCertRepository.get(email));
        if(certNumber == null || !emailCertRepository.get(email).equals(certNumber)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("이메일 인증 통과");
        session.setAttribute("emailAuth", email); // 회원가입 submit 검증할 때 필요
        return ResponseEntity.ok().build();
    }

    // 이메일 인증 버튼 클릭
    @PostMapping("/email/auth")
    public ResponseEntity<Void> post_email_auth(
            @RequestBody String email_to
    ){
        log.info("post_email_auth");
        try{
            String certNumber = emailService.send_cert_mail(email_to);
            emailCertRepository.put(email_to, certNumber);
            return ResponseEntity.ok().build();
        } catch (MessagingException e) {
            log.error("인증 이메일 전송 중 오류 발생... : " + e.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }

    /**********************************************************/
    @GetMapping("/findId/{email}")
    public ResponseEntity<String> get_findId_by_email(
            @PathVariable String email
    ) {
        String foundId = userMapper.findIdByEmail(email);

        if(foundId != null){
            return ResponseEntity.status(HttpStatus.FOUND).body(foundId);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/resetPw/password")
    public ResponseEntity<Void> post_change_password(
        Authentication auth,
        @RequestBody String password
    ){
        String id = auth.getName();
        userService.reset_password(id, password);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    /******************************************/
    @PostMapping("/changeInfo")
    public ResponseEntity<Void> post_user_change_info(
            // 아이디/비번만 찾는다면 auth로 충분
            // 단, 다른 정보는 @AuthenticationPrincipal 써야됨
            Authentication auth, 
            @ModelAttribute @Validated UserDTO modifyingUser,
            BindingResult bindingResult
    ){
        // 로그인 했습니까?
        if(auth == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // 지금 로그인 한 나와 수정된 유저 객체가 같아야 한다
        if(!auth.getName().equals(modifyingUser.getId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // 비밀번호, 기타 패턴 조건 안 맞을시
        if(bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        boolean updateResult = userService.updateUser(modifyingUser);
        if(!updateResult){
            // 업데이트 실패 시
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /***************************************************/
    @PostMapping("/info")
    public ResponseEntity<Void> post_user_info(
            @RequestBody Integer bookIsbn
    ){

        return ResponseEntity.status(HttpStatus.OK).build();

    }

}
