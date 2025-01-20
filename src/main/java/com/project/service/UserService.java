package com.project.service;

import com.project.dto.BookDTO;
import com.project.dto.ComplainDTO;
import com.project.dto.LoanDTO;
import com.project.dto.UserDTO;
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

import java.time.LocalDateTime;
import java.util.Objects;

@Log4j2
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private LoanMapper loanMapper;
    @Autowired private PasswordEncoder passwordEncoder;


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

    /**
     * 포인트를 사용하여 책 대출하기
     */
    public Integer rentBookWithPoints(String userId, String isbn, Integer points) {
        // 현재 대출 중인 책의 수 확인
        Integer activeLoans = loanMapper.getActiveLoanCountByUserId(userId);
        if (activeLoans >= 5) {
            throw new IllegalArgumentException("최대 5권까지 대출 가능합니다.");
        }

        // 동일 책이 이미 대출 중인지 확인
        LoanDTO existingLoan = loanMapper.getActiveLoanByUserAndBook(userId, isbn);
        if (existingLoan != null) {
            throw new IllegalArgumentException("이미 대출 중인 책입니다.");
        }

        // 책 정보 확인
        BookDTO book = bookMapper.getBookByIsbn(isbn);
        if (book == null) {
            throw new IllegalArgumentException("해당 ISBN에 해당하는 책이 없습니다.");
        }

        // 남은 복사본 확인
        Integer availableCopies = loanMapper.getAvailableCopies(isbn);
        if (availableCopies == null || availableCopies <= 0) {
            LocalDateTime nextReturnDate = loanMapper.getFirstReturnDateByBookIsbn(isbn);
            throw new IllegalArgumentException("현재 대출 가능한 복사본이 없습니다. 다음 반납 예상일: " + nextReturnDate);
        }

        // 포인트를 사용하지 않을 경우 기본 가격으로 대출
        if (points == null || points == 0) {
            loanMapper.decreaseCopiesAvailable(isbn); // 복사본 감소
            return createLoan(userId, isbn, 0, book.getPrice());
        }

        // 사용 가능한 최대 포인트 계산
        Integer maxUsablePoints = (book.getPrice() / 10000) * 1000; // 1000포인트 = 10000원
        if (points > maxUsablePoints) {
            points = maxUsablePoints;
        }

        // 포인트를 할인 가격으로 변환 및 최종 가격 계산
        Integer discountPrice = (points / 1000) * 10000; // 포인트를 할인 금액으로 변환
        Integer finalPrice = book.getPrice() - discountPrice;

        // 포인트 차감 및 대출 생성
        deductPoints(userId, points);
        loanMapper.decreaseCopiesAvailable(isbn); // 복사본 감소
        return createLoan(userId, isbn, discountPrice, finalPrice);
    }

    /**
     * 포인트 차감
     */
    public void deductPoints(String userId, Integer points) {
        Integer updatedRows = userMapper.deductPoints(userId, points);
        if (updatedRows == 0) {
            throw new IllegalStateException("포인트가 부족하여 차감에 실패했습니다.");
        }
    }

    /**
     * 책 반납 기능
     */
    public void returnBook(String userId, String isbn) {
        LoanDTO loan = loanMapper.getActiveLoanByUserAndBook(userId, isbn);
        if (loan == null) {
            throw new IllegalArgumentException("반납할 대출 기록이 없습니다.");
        }
        loanMapper.updateLoanStatus(loan.getId(), "반납 완료");
        loanMapper.increaseCopiesAvailable(isbn); // 복사본 증가
        log.info("책이 성공적으로 반납되었습니다: ISBN {}", isbn);
    }

    /**
     * 컴플레인 생성
     */
    public void createComplain(String title, String contents, String userId) {
        ComplainDTO complain = new ComplainDTO();
        complain.setTitle(title);
        complain.setContents(contents);
        complain.setUserId(userId);
        userMapper.insertComplain(complain);
        log.info("컴플레인이 성공적으로 생성되었습니다: {}", complain);
    }

    /**
     * 컴플레인 수정
     */
    public void updateComplain(Integer complainId, String newContents) {
        ComplainDTO complain = new ComplainDTO();
        complain.setNo(complainId);
        complain.setContents(newContents);
        userMapper.updateComplain(complain);
        log.info("컴플레인이 성공적으로 수정되었습니다: ID {}", complainId);
    }

    /**
     * 컴플레인 삭제
     */
    public void deleteComplain(Integer complainId) {
        userMapper.deleteComplain(complainId);
        log.info("컴플레인이 성공적으로 삭제되었습니다: ID {}", complainId);
    }

    /**
     * 공통 대출 생성 로직
     */
    private Integer createLoan(String userId, String isbn, Integer discountPrice, Integer finalPrice) {
        LoanDTO loan = new LoanDTO();
        loan.setUserId(userId);
        loan.setBookIsbn(isbn);
        loan.setDiscountPrice(discountPrice);
        loan.setFinalPrice(finalPrice);
        loanMapper.createLoan(loan);
        return finalPrice;
    }


}
