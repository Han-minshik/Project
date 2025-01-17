package com.project.mapper;

import com.project.dto.LoanDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LoanMapper {
    void createLoan(LoanDTO loan);
    LoanDTO getLoanById(int id);
    List<LoanDTO> getLoansByUserId(String userId);
    void updateLoanStatus(LoanDTO loan);
    List<LoanDTO> getAllloans();
}
