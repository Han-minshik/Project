package com.project.service;

import com.project.dto.LoanDTO;
import com.project.mapper.LoanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanService {
    @Autowired private LoanMapper loanMapper;

    public void createLoan(LoanDTO loanDTO) {
        Integer activeLoanCount = loanMapper.getActiveLoanCountByUserId(loanDTO.getUserId());
        if (activeLoanCount >= 5) {
            throw new IllegalArgumentException("최대 5권까지 대출할 수 있습니다.");
        }

        Integer availableCopies = loanMapper.getAvailableCopies(loanDTO.getBookIsbn()); // Integer.valueOf 제거
        if (availableCopies <= 0) {
            throw new IllegalArgumentException("대출 가능한 재고가 없습니다.");
        }

        loanMapper.createLoan(loanDTO);
        loanMapper.decreaseCopiesAvailable(loanDTO.getBookIsbn()); // Integer.valueOf 제거
    }

    public List<LoanDTO> getLoansByUserId(String userId) {
        return loanMapper.getLoansByUserId(userId);
    }

    public void updateLoanStatus(Integer loanId, String bookIsbn) { // Integer → String
        loanMapper.updateLoanStatus(loanId, "반납 완료");
        loanMapper.increaseCopiesAvailable(bookIsbn); // Integer → String
    }

    public List<LoanDTO> getAllLoans() {
        return loanMapper.getAllLoans();
    }
}

