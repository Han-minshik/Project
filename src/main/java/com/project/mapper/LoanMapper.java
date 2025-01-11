package com.project.mapper;

import com.project.dto.LoanDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LoanMapper {
    void createLoan(LoanDTO loan);
    List<LoanDTO> getLoansByUserId(String userId);
    void updateLoanStatus(@Param("id") int id, @Param("status") String status);
    List<LoanDTO> getAllLoans();
    // 한 사용자가 빌릴 수 있는 책의 권수
    int getActiveLoanCountByUserId(@Param("userId") String userId);
    // 모든 사용자가 한 책에 대해 빌릴 수 있는 대출 횟
    int getAvailableCopies(@Param("bookIsbn") int bookIsbn);
    void decreaseCopiesAvailable(@Param("bookIsbn") int bookIsbn);
    void increaseCopiesAvailable(@Param("bookIsbn") int bookIsbn);
}
