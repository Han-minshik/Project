package com.project.service;

import com.project.dto.BookDTO;
import com.project.dto.ComplainDTO;
import com.project.dto.LoanDTO;
import com.project.dto.UserDTO;
import com.project.mapper.BookMapper;
import com.project.mapper.LoanMapper;
import com.project.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean join_user(UserDTO joinUser) {
        UserDTO findUser = userMapper.getUserById(joinUser.getId());
        if (findUser != null) {
            log.error("이미 회원가입이 되어있습니다.");
            return false;
        }

        String encodedPassword = passwordEncoder.encode(joinUser.getPassword());
        joinUser.setPassword(encodedPassword);
        userMapper.createUser(joinUser);
        return true;
    }

    public boolean reset_password(String id, String newPw) {
        boolean pwPatternResult = newPw.matches("^[0-9a-zA-Z~@#$%^&*()_=+.-]{4,10}");
        if (!pwPatternResult) {
            return false;
        }

        UserDTO findUser = userMapper.getUserById(id);
        findUser.setPassword(passwordEncoder.encode(newPw));
        userMapper.updateUser(findUser);
        return true;
    }

    public boolean updateUser(UserDTO modifyingUser) {
        UserDTO findUser = userMapper.getUserById(modifyingUser.getId());
        if (findUser != null) {
            modifyingUser.setPassword(passwordEncoder.encode(modifyingUser.getPassword()));
            userMapper.updateUser(modifyingUser);
            return true;
        }
        return false;
    }

    public Integer rentBookWithPoints(String userId, String isbn, Integer points) {
        Integer activeLoans = loanMapper.getActiveLoanCountByUserId(userId);
        if (activeLoans >= 5) {
            throw new IllegalArgumentException("최대 5권까지 대출 가능합니다.");
        }

        List<LoanDTO> existingLoan = loanMapper.getActiveLoanByUserAndBook(userId);

        if (existingLoan != null && !existingLoan.isEmpty()) {
            StringBuilder isbnList = new StringBuilder("이미 대출 중인 책이 있습니다: ");
            for (LoanDTO loan : existingLoan) {
                isbnList.append(loan.getBookIsbn()).append(", ");
            }
            // 마지막 쉼표 제거
            String message = isbnList.substring(0, isbnList.length() - 2);
            throw new IllegalArgumentException(message);
        }


        BookDTO book = bookMapper.getBookByIsbn(isbn);
        if (book == null) {
            throw new IllegalArgumentException("해당 ISBN에 해당하는 책이 없습니다.");
        }

        Integer availableCopies = loanMapper.getAvailableCopies(isbn);
        if (availableCopies == null || availableCopies <= 0) {
            LocalDateTime nextReturnDate = loanMapper.getFirstReturnDateByBookIsbn(isbn);
            throw new IllegalArgumentException("현재 대출 가능한 복사본이 없습니다. 다음 반납 예상일: " + nextReturnDate);
        }

        if (points == null || points == 0) {
            loanMapper.decreaseCopiesAvailable(isbn);
            return createLoan(userId, isbn, 0, book.getPrice());
        }

        Integer maxUsablePoints = (book.getPrice() / 10000) * 1000;
        if (points > maxUsablePoints) {
            points = maxUsablePoints;
        }

        Integer discountPrice = (points / 1000) * 10000;
        Integer finalPrice = book.getPrice() - discountPrice;

        deductPoints(userId, points);
        loanMapper.decreaseCopiesAvailable(isbn);
        return createLoan(userId, isbn, discountPrice, finalPrice);
    }

    public void deductPoints(String userId, Integer points) {
        Integer updatedRows = userMapper.deductPoints(userId, points);
        if (updatedRows == 0) {
            throw new IllegalStateException("포인트가 부족하여 차감에 실패했습니다.");
        }
    }

    public void returnBook(String userId, String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            throw new IllegalArgumentException("반납하려는 책의 ISBN이 필요합니다.");
        }

        // 대출 기록 조회
        List<LoanDTO> loans = loanMapper.getActiveLoanByUserAndBook(userId);
        if (loans == null || loans.isEmpty()) {
            throw new IllegalArgumentException("반납할 대출 기록이 없습니다.");
        }

        // 리스트를 순회하면서 해당 ISBN과 일치하는 대출 기록 처리
        boolean isReturned = false;
        for (LoanDTO loan : loans) {
            if (loan.getBookIsbn().equals(isbn)) {
                loanMapper.updateLoanStatus(loan.getId(), "반납 완료");
                loanMapper.increaseCopiesAvailable(isbn);
                log.info("책이 성공적으로 반납되었습니다: ISBN {}", isbn);
                isReturned = true;
                break; // ISBN이 일치하는 책을 처리한 후 종료
            }
        }

        // ISBN에 해당하는 대출 기록이 없으면 예외 처리
        if (!isReturned) {
            throw new IllegalArgumentException("반납하려는 책의 대출 기록이 없습니다: ISBN " + isbn);
        }
    }


    public void createComplain(String title, String contents, String userId) {
        ComplainDTO complain = new ComplainDTO();
        complain.setTitle(title);
        complain.setContents(contents);
        complain.setUserId(userId);
        userMapper.insertComplain(complain);
        log.info("컴플레인이 성공적으로 생성되었습니다: {}", complain);
    }

    public void updateComplain(Integer complainId, String newContents) {
        ComplainDTO complain = new ComplainDTO();
        complain.setNo(complainId);
        complain.setContents(newContents);
        userMapper.updateComplain(complain);
        log.info("컴플레인이 성공적으로 수정되었습니다: ID {}", complainId);
    }

    public void deleteComplain(Integer complainId) {
        userMapper.deleteComplain(complainId);
        log.info("컴플레인이 성공적으로 삭제되었습니다: ID {}", complainId);
    }

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
