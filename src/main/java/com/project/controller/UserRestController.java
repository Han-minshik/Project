package com.project.controller;

import com.project.dto.BookDTO;
import com.project.dto.LoanDTO;
import com.project.dto.UserDTO;
import com.project.service.BookService;
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
    @Autowired
    private BookService bookService;

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
    public ResponseEntity<Void> post_reset_password(
            Authentication auth,
            @RequestBody String password
    ){
        String id = auth.getName();
        userService.reset_password(id, password);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /******************************************/
//    @PostMapping("/info-revise")
//    public ResponseEntity<Void> post_info_revise(
//            // 아이디/비번만 찾는다면 auth로 충분
//            // 단, 다른 정보는 @AuthenticationPrincipal 써야됨
//            Authentication auth,
//            @ModelAttribute @Validated UserDTO reviseUser,
//            BindingResult bindingResult
//    ){
//        // 로그인 했습니까?
//        if(auth == null){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//        // 지금 로그인 한 나와 수정된 유저 객체가 같아야 한다
//        if(!auth.getName().equals(reviseUser.getId())){
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
//        // 비밀번호, 기타 패턴 조건 안 맞을시
//        if(bindingResult.hasErrors()){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//
//        boolean updateResult = userService.updateUser(reviseUser);
//        if(!updateResult){
//            // 업데이트 실패 시
//            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
//        }
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }

    /***************************************************/
    @PostMapping("/info")
    public ResponseEntity<Void> post_user_info(
            @RequestBody Integer bookIsbn
    ){
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /*************************************************/
    @DeleteMapping("/wishlist/delete")
    public ResponseEntity<Void> delete_wishlist(
            Authentication auth,
            @RequestBody Integer cartNo
    ){
        String userId = auth.getName();
        bookService.deleteBooksFromCart(cartNo, userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /*************************************************/

//    // 사용자가 주문 페이지에서 상품을 주문
//    @PostMapping("/lend")
//    public ResponseEntity<Void> post_lend(
//            @RequestBody LoanDTO loan, // RequestBody의 자료를 자동으로 loanDTO에 넣어준다
//            @SessionAttribute("carts") List<CartDTO> carts, // 미리 설정해놓은 주문할 상품들
//            @AuthenticationPrincipal UserDTO user
//    ){
//        loan.setUser(user); // 주문하려는 유저를 설정
//        loan.setCarts(carts); // 주문하려는 상품 정보를 등록
//        // 전달된 imp_uid 값이 잘못되었거나, Portone과의 연계가 실패(요청실패) 했을 경우 NULL
//        LoanDTO paymentInfo = portOneSerivce.payments_authentication(loan.getImpUid());
//        // imp_uid로 조회한 주문번호가 사용자가 대출 페이지에서 전달한 대출번호와 다르다?
//        if(Objects.isNull(paymentInfo) || !paymentInfo.getId().equals(loan.getId())){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        //// 기존에 대출하려고 하는 상품을 DB에서 조회하고, 실제 결제 내역과 비교
//        boolean result = orderService.check_order_total_price(orderDTO, paymentInfo);
//        if(!result){ // 결과값 다르다면 (포트원 != 실제 주문 금액)
//            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
//        }
//        // 결제가 정확하다면 대출 이력에 추가해야 한다
//        try {
//            orderService.add_order(orderDTO);
//        } catch (Exception e) {
//            log.error("대출 이력 추가 중 에러 발생: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//        // 최종 주문 완료
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }



}