package com.project.service;

import com.project.dto.BookDTO;
import com.project.dto.LoanDTO;
import com.project.dto.PageInfoDTO;
import com.project.mapper.BookMapper;
import com.project.mapper.LoanMapper;
import com.project.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2
@Service
public class LoanService {
    @Autowired private LoanMapper loanMapper;
    @Autowired private UserMapper userMapper;
    @Autowired private BookMapper bookMapper;
    @Autowired private PortOneService portOneService;

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
        Integer availableCopies = loanMapper.getAvailableCopies(bookIsbn);

        if(availableCopies == null || availableCopies == 0) {
            return 0;
        }
        return loanMapper.getAvailableCopies(bookIsbn);
    }

    public Integer getUserPoints(String userId) {
        return loanMapper.getUserPoints(userId);
    }

    /**
     * 포인트 차감 메서드 (포인트가 있다면 사용)
     */
    public void deductUserPoints(String userId, Integer usedPoints) {
        if (usedPoints != null && usedPoints > 0) {
            log.info("🔹 사용자 {}의 포인트 {} 차감", userId, usedPoints);
            loanMapper.deductUserPoints(userId, usedPoints);
        }
    }

    @Transactional
    public void createLoanWithPoints(LoanDTO loan) {
        Integer availableCopies = loanMapper.getAvailableCopies(loan.getBookIsbn());
        if (availableCopies == null || availableCopies <= 0) {
            throw new IllegalArgumentException("현재 대출 가능한 복사본이 없습니다.");
        }

        String userId = loan.getUser().getId();
        Integer userPoints = loanMapper.getUserPoints(userId);

        // ✅ 포인트가 있으면 **무조건** 사용
        Integer maxUsablePoints = (loan.getOriginalPrice() / 10000) * 1000; // 최대 사용할 수 있는 포인트
        Integer usedPoints = Math.min(userPoints, maxUsablePoints); // 실제 사용할 포인트
        Integer discountPrice = (usedPoints / 1000) * 10000; // 포인트를 반영한 할인 금액

        // ✅ 최종 결제 금액: 포인트 적용 후 금액
        loan.setDiscountPrice(discountPrice);
        loan.setFinalPrice(Math.max(0, loan.getOriginalPrice() - discountPrice));

        // ✅ 대출 데이터 저장
        log.info("📌 대출 저장 (포인트 적용됨): " + loan);
        loanMapper.createLoan(loan);

        // ✅ 책 재고 감소
        loanMapper.decreaseCopiesAvailable(loan.getBookIsbn());

        // ✅ 포인트 차감 (포인트가 0이 아닌 경우만 실행)
        if (usedPoints > 0) {
            log.info("🔹 사용자 {}의 포인트 {} 차감", userId, usedPoints);
            loanMapper.deductUserPoints(userId, usedPoints);
        }
    }


    /**
     * 특정 사용자가 대출 중인 책 확인
     */
    public Map<LoanDTO, BookDTO> getActiveLoanByUser(String userId) {
        // 사용자의 대출 목록 조회
        List<LoanDTO> loans = loanMapper.getActiveLoanByUserAndBook(userId);
        if (loans == null || loans.isEmpty()) {
            throw new IllegalArgumentException("현재 사용자가 대출 중인 기록이 없습니다.");
        }

        // 대출 목록에서 ISBN 추출
        List<String> isbns = loans.stream()
                .map(LoanDTO::getBookIsbn)
                .distinct()
                .toList();

        List<BookDTO> books = isbns.stream()
                .map(isbn -> {
                    BookDTO book = bookMapper.getBookByIsbn(isbn);
                    return book;
                })
                .filter(Objects::nonNull)
                .toList();

        // ISBN을 키로 하는 맵 생성
        Map<String, BookDTO> bookMap = books.stream()
                .collect(Collectors.toMap(BookDTO::getIsbn, book -> book));

        // LoanDTO와 BookDTO를 매핑
        return loans.stream()
                .collect(Collectors.toMap(
                        loan -> loan,
                        loan -> bookMap.get(loan.getBookIsbn())
                ));
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
