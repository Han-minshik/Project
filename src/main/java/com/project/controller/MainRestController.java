package com.project.controller;


import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import com.project.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
public class MainRestController {

    @Autowired private EmailService emailService;
    private final Map<String, String> emailCertRepository = new HashMap<>();
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


}
