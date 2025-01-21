package com.project.service;

import com.project.dto.LoanDTO;
import com.project.mapper.LoanMapper;
import com.project.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Service
public class LoanService {
    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 특정 사용자의 대출 기록 조회
     */
    public List<LoanDTO> getLoansByUserId(String userId) {
        return loanMapper.getLoansByUserId(userId);
    }

    /**
     * 대출 상태 업데이트 및 책 재고 증가 (반납 처리)
     */
    public void updateLoanStatus(Integer loanId, String bookIsbn) {
        loanMapper.updateLoanStatus(loanId, "반납 완료");
        loanMapper.increaseCopiesAvailable(bookIsbn);
    }

    /**
     * 모든 대출 기록 조회
     */
    public List<LoanDTO> getAllLoans() {
        return loanMapper.getAllLoans();
    }

    /**
     * 사용자가 대출 중인 책의 개수 확인
     */
    public Integer getActiveLoanCountByUserId(String userId) {
        return loanMapper.getActiveLoanCountByUserId(userId);
    }

    /**
     * 특정 책의 대출 가능한 복사본 확인
     */
    public Integer getAvailableCopies(String bookIsbn) {
        return loanMapper.getAvailableCopies(bookIsbn);
    }

    /**
     * 포인트를 사용하여 대출 생성
     */
    public void createLoanWithPoints(LoanDTO loan, Integer points) {
        Integer availableCopies = loanMapper.getAvailableCopies(loan.getBookIsbn());
        if (availableCopies == null || availableCopies <= 0) {
            LocalDateTime nextReturnDate = loanMapper.getFirstReturnDateByBookIsbn(loan.getBookIsbn());
            throw new IllegalArgumentException("현재 대출 가능한 복사본이 없습니다. 다음 반납 예상일: " + nextReturnDate);
        }

        Integer discountPrice = 0;
        if (points != null && points > 0) {
            Integer maxUsablePoints = (loan.getFinalPrice() / 10000) * 1000;
            points = Math.min(points, maxUsablePoints);
            discountPrice = (points / 1000) * 10000;
            loan.setDiscountPrice(discountPrice);
            loan.setFinalPrice(loan.getFinalPrice() - discountPrice);
        }

        loanMapper.createLoan(loan);
        loanMapper.decreaseCopiesAvailable(loan.getBookIsbn());
    }

    /**
     * 특정 사용자가 대출 중인 책의 정보 확인
     */
    public LoanDTO getActiveLoanByUser(String userId) {
        LoanDTO loan = loanMapper.getActiveLoanByUserAndBook(userId);
        if (loan == null) {
            throw new IllegalArgumentException("현재 사용자가 대출 중인 기록이 없습니다.");
        }
        return loan;
    }

    /**
     * 특정 책의 첫 번째 반납 예정일 조회
     */
    public LocalDateTime getFirstReturnDateByBookIsbn(String bookIsbn) {
        return loanMapper.getFirstReturnDateByBookIsbn(bookIsbn);
    }

    /**
     * 반납 처리 및 포인트 적립
     */
    public void returnBookWithReward(Integer loanId, String bookIsbn, String userId) {
        // 대출 상태를 '반납 완료'로 업데이트
        loanMapper.updateLoanStatus(loanId, "반납 완료");
        loanMapper.increaseCopiesAvailable(bookIsbn);

        // 대출 중인 사용자 확인 및 포인트 적립
        LoanDTO loan = loanMapper.getActiveLoanByUserAndBook(userId);
        if (loan != null && loan.getFinalPrice() != null) {
            Integer rewardPoints = (loan.getFinalPrice() / 1000) * 10;
            userMapper.addPointToUser(userId, rewardPoints);
            log.info("반납 완료 및 포인트 {} 적립: 사용자 ID {}, 책 ISBN {}", rewardPoints, userId, bookIsbn);
        } else {
            log.warn("사용자 {}에 대한 유효한 대출 기록이 존재하지 않습니다.", userId);
        }
    }
}
