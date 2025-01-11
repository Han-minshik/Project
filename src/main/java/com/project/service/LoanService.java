package com.project.service;

import com.project.dto.LoanDTO;
import com.project.mapper.LoanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanService {
    @Autowired private LoanMapper loanMapper;
    public void createLoan(LoanDTO loanDTO) {
        int activeLoanCount = loanMapper.getActiveLoanCountByUserId(loanDTO.getUserId());
        if(activeLoanCount >= 5) {
            throw new IllegalArgumentException("최대 5권까지 대출할 수 있습니다.");
        }
        int availableCopies = loanMapper.getAvailableCopies(loanDTO.getBookIsbn());
        if(availableCopies <= 0) {
            throw new IllegalArgumentException("대출 가능한 재고가 없습니다.");
        }

        loanMapper.createLoan(loanDTO);
        loanMapper.decreaseCopiesAvailable(loanDTO.getBookIsbn());
    }
    /**
     * 대출 기록 조회
     * @param  userId : 사용자 ID
     * @return : 대출 기록 리스트
     */
    public List<LoanDTO> getLoansByUserId(String userId) {
        return loanMapper.getLoansByUserId(userId);
    }
    /**
     * 대출 상태 업데이트
     * - 반납 처리 시 상태를 '반납 완료'로 설정
     * - 반납일은 현재 날짜로 설정
     */
    public void updateLoanStatus(int loanId, int bookIsbn) {
        loanMapper.updateLoanStatus(loanId, "반납 완료");
        loanMapper.increaseCopiesAvailable(bookIsbn);
    }

    public List<LoanDTO> getAllLoans() {
        return loanMapper.getAllLoans();
    }
}
