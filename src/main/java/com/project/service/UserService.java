package com.project.service;

import com.project.dto.*;
import com.project.mapper.BookMapper;
import com.project.mapper.LoanMapper;
import com.project.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Objects;

@Log4j2
@Service
public class UserService {
    @Autowired private UserMapper userMapper;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private BookMapper bookMapper;
    @Autowired private LoanMapper loanMapper;

    public boolean join_user(UserDTO joinUser) {
        // 유저 중복 방지
        UserDTO findUser = userMapper.getUserById(joinUser.getId());
        if(Objects.nonNull(findUser)) {
            log.error("이미 회원가입이 되어있습니다.");
            return false;
        }

        String encodedPassword = passwordEncoder.encode(joinUser.getPassword());
        joinUser.setPassword(encodedPassword);

        userMapper.createUser(joinUser);
        return true;
    }

    public boolean update_user(
            UserDTO modifyingUser
    ) {
        UserDTO findUser = userMapper.getUserById(modifyingUser.getId()); // 사실 필요 없음 지워도 됨.
        if(Objects.nonNull(findUser)) { // 변경하려는 유저가 맞음
            modifyingUser.setPassword(passwordEncoder.encode(modifyingUser.getPassword())); // 비밀번호 암호화
            userMapper.updateUser(modifyingUser); // 단, 이게 int형이여야만 return true/false 사용 가능
            return true;
        }
        return false;
    }

    // 비밀번호 분실 시
    public boolean reset_password(String id, String newPw) {
        // 패턴 검사
        boolean pwPatternResult = newPw.matches("^[0-9a-zA-Z~@#$%^&*()_=+.-]{4,10}");
        if(!pwPatternResult) {
            return false;
        }

        UserDTO findUser = userMapper.getUserById(id);
        if(!newPw.equals(findUser.getPassword())) {
            findUser.setPassword(passwordEncoder.encode(findUser.getPassword())); // 비밀번호 암호화
            userMapper.updateUser(findUser);
            return true;
        }
        return false;
    }

    /*************************************************/
    // 리뷰 작성
    public void write_review(String userId, Integer bookIsbn, ReviewDTO review) {
        review.setUserId(userId);
        review.setBookIsbn(bookIsbn);
        userMapper.insertReview(review);
    }
    /************************************************/
    // 토론 게시글 작성
    public void write_discussion(String userId, Integer bookIsbn, DiscussionDTO discussion) {

    }

    // 토론 게시글의 댓글 작성
    public void write_comment(String userId, Integer bookIsbn, String comment) {

    }
    /****************** 시스템 ******************/
    // 포인트 부여하기
    public void grantPoint(String userId, Integer points, String reason) {
        boolean alreadyGranted = userMapper.hasPointGrantedForReason(userId, reason);
        if(!alreadyGranted) {
            userMapper.addPointToUser(userId, points);
            userMapper.addPointGrantHistory(userId, points, reason);
            log.info("포인트 지급 완료 : userId={}, points={}, reason{}", userId, points, reason);
        } else {
            log.info("포인트가 이미 지급된 사용자입니다. : userId={}, reason={}", userId, reason);
        }
    }
    // 댓글이 가장 많은 토론 생성자
    public void rewardDiscussionCreator() {
        String userId = userMapper.getTopDiscussionUser();
        if(userId != null) {
            grantPoint(userId, 1, "댓글 다수");
        }
    }
    // 좋아요가 가장 많은 댓글 작성자
    public void rewardTopCommenter(Integer discussionId) {
        String userId = userMapper.getTopCommentUserByDiscussionId(discussionId);
        if(userId != null) {
            grantPoint(userId, 1, "공감 다수");
        }
    }
    /*********************************************************/
    /**
     * 책 대여 기능 : 포인트를 사용하여 할인
     * @param userId : 사용자 Id
     * @param isbn : 대여하려는 책 ISBN
     * @param points : 사용하려는 포인트
     * @return : 최종 결제 금액
     */
    public Integer rentBookWithPoints(String userId, Integer isbn, Integer points) {
        // 대출 중복 확인
        Integer activeLoans = loanMapper.getActiveLoanCountByUserId(userId);
        if(activeLoans >= 5) {
            throw new IllegalArgumentException("최대 5권까지 대출 가능합니다.");
        }
        // 기존 대출 확인
        LoanDTO existingLoan = loanMapper.getActiveLoanByUserAndBook(userId, isbn);
        if(existingLoan != null) {
            throw new IllegalArgumentException("이미 대출 중인 책입니다.");
        }

        // 책 상세 정보 조회
        BookDTO book = bookMapper.getBookDetails(isbn);
        if(book == null) {
            throw new IllegalArgumentException("해당 ISBN에 해당하는 책이 없습니다.");
        }

        if(points == null || points == 0) {
            Integer finalPrice = book.getPrice();

            LoanDTO loan = new LoanDTO();
            loan.setUserId(userId);
            loan.setBookIsbn(isbn);
            loan.setDiscountPrice(0);
            loan.setFinalPrice(finalPrice);
            loanMapper.createLoan(loan);
            return finalPrice;
        }

        Integer maxUsablePoints = (book.getPrice() / 1000) * 10;
        if(points > maxUsablePoints) {
            points = maxUsablePoints;
        }

        Integer discountPrice = points / 10 * 1000;
        Integer finalPrice = book.getPrice() - discountPrice;

        userMapper.deductPoints(userId, points);

        LoanDTO loan = new LoanDTO();
        loan.setUserId(userId);
        loan.setBookIsbn(isbn);
        loan.setDiscountPrice(discountPrice);
        loan.setFinalPrice(finalPrice);
        loanMapper.createLoan(loan);

        return finalPrice;
    }

    public void deductPoints(String userId, Integer points) {
        Integer updatedRows = userMapper.deductPoints(userId, points);
        if(updatedRows == 0) {
            throw new IllegalStateException("포인트가 부족합니다");
        }
    }
}
