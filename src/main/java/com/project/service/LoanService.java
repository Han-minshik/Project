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

