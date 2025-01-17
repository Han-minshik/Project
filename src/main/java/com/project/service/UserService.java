package com.project.service;

import com.project.dto.BookDTO;
import com.project.dto.LoanDTO;
import com.project.mapper.BookMapper;
import com.project.mapper.LoanMapper;
import com.project.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private LoanMapper loanMapper;

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

        // 포인트를 사용하지 않을 경우 기본 가격으로 대출
        if (points == null || points == 0) {
            return createLoan(userId, isbn, 0, book.getPrice());
        }

        // 사용 가능한 최대 포인트 계산
        Integer maxUsablePoints = (book.getPrice() / 1000) * 10;
        if (points > maxUsablePoints) {
            points = maxUsablePoints;
        }

        // 포인트를 할인 가격으로 변환 및 최종 가격 계산
        Integer discountPrice = points / 10 * 1000;
        Integer finalPrice = book.getPrice() - discountPrice;

        // 포인트 차감 및 대출 생성
        deductPoints(userId, points);
        return createLoan(userId, isbn, discountPrice, finalPrice);
    }

    public void deductPoints(String userId, Integer points) {
        Integer updatedRows = userMapper.deductPoints(userId, points);
        if (updatedRows == 0) {
            throw new IllegalStateException("포인트가 부족합니다.");
        }
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
