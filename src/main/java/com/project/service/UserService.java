package com.project.service;

import com.project.dto.BookDTO;
import com.project.dto.LoanDTO;
import com.project.dto.ReviewDTO;
import com.project.dto.UserDTO;
import com.project.mapper.BookMapper;
import com.project.mapper.LoanMapper;
import com.project.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Log4j2
@Service
public class UserService {
    @Autowired private UserMapper userMapper;
    @Autowired private BookMapper bookMapper;
    @Autowired private LoanMapper loanMapper;
//    @Autowired private PasswordEncoder passwordEncoder;

    public Integer rentBookWithPoints(String userId, String isbn, Integer points) { // Integer → String
        Integer activeLoans = loanMapper.getActiveLoanCountByUserId(userId);
        if (activeLoans >= 5) {
            throw new IllegalArgumentException("최대 5권까지 대출 가능합니다.");
        }

        LoanDTO existingLoan = loanMapper.getActiveLoanByUserAndBook(userId, isbn); // Integer.valueOf 제거
        if (existingLoan != null) {
            throw new IllegalArgumentException("이미 대출 중인 책입니다.");
        }

        BookDTO book = bookMapper.getBookDetails(isbn); // Integer.valueOf 제거
        if (book == null) {
            throw new IllegalArgumentException("해당 ISBN에 해당하는 책이 없습니다.");
        }

        if (points == null || points == 0) {
            Integer finalPrice = book.getPrice();

            LoanDTO loan = new LoanDTO();
            loan.setUserId(userId);
            loan.setBookIsbn(isbn); // String으로 설정
            loan.setDiscountPrice(0);
            loan.setFinalPrice(finalPrice);
            loanMapper.createLoan(loan);
            return finalPrice;
        }

        Integer maxUsablePoints = (book.getPrice() / 1000) * 10;
        if (points > maxUsablePoints) {
            points = maxUsablePoints;
        }

        Integer discountPrice = points / 10 * 1000;
        Integer finalPrice = book.getPrice() - discountPrice;

        userMapper.deductPoints(userId, points);

        LoanDTO loan = new LoanDTO();
        loan.setUserId(userId);
        loan.setBookIsbn(isbn); // String으로 설정
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
