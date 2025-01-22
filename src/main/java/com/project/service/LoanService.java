package com.project.service;

import com.project.dto.BookDTO;
import com.project.dto.LoanDTO;
import com.project.mapper.BookMapper;
import com.project.mapper.LoanMapper;
import com.project.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Service
public class LoanService {
    @Autowired private LoanMapper loanMapper;
    @Autowired private UserMapper userMapper;
    @Autowired private BookMapper bookMapper;

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
    public String getAvailableCopies(String bookIsbn) {
        Integer availableCopies = loanMapper.getAvailableCopies(bookIsbn);

        if(availableCopies == null || availableCopies == 0) {
            return "불가";
        }
        return loanMapper.getAvailableCopies(bookIsbn).toString();
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
     * 특정 사용자가 대출 중인 책 확인
     */
    public Map<LoanDTO, BookDTO> getActiveLoanByUser(String userId) {
        List<LoanDTO> loans = loanMapper.getActiveLoanByUserAndBook(userId);
        if (loans == null) {
            throw new IllegalArgumentException("현재 사용자가 대출 중인 기록이 없습니다.");
        }
        List<String> isbns = loans.stream()
                .map(LoanDTO::getBookIsbn)
                .distinct()
                .toList();

        List<BookDTO> books = new ArrayList<>();
        for(String isbn : isbns) {
            BookDTO book = bookMapper.getBookByIsbn(isbn);
            if(book != null) {
                books.add(book);
            }
        }
        Map<String, BookDTO> bookmap = books.stream().collect(Collectors.toMap(BookDTO::getIsbn, book -> book));

        return loans.stream().collect(Collectors.toMap(loan -> loan, loan -> bookmap.get(loan.getBookIsbn())));
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
        List<LoanDTO> loans = loanMapper.getActiveLoanByUserAndBook(userId);

        if (loans != null && !loans.isEmpty()) {
            for (LoanDTO loan : loans) {
                if (loan.getFinalPrice() != null) {
                    Integer rewardPoints = (loan.getFinalPrice() / 1000) * 10;
                    userMapper.addPointToUser(userId, rewardPoints);
                    log.info("반납 완료 및 포인트 {} 적립: 사용자 ID {}, 책 ISBN {}", rewardPoints, userId, bookIsbn);
                } else {
                    log.warn("사용자 {}의 대출 기록 중 가격 정보가 없는 항목이 있습니다.", userId);
                }
            }
        } else {
            log.warn("사용자 {}에 대한 유효한 대출 기록이 존재하지 않습니다.", userId);
        }
    }

}
