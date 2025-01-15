package com.project.mapper;

import com.project.dto.LoanDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LoanMapper {
    void createLoan(LoanDTO loan);
    List<LoanDTO> getLoansByUserId(String userId);
    void updateLoanStatus(@Param("id") Integer id, @Param("status") String status);
    List<LoanDTO> getAllLoans();
    // 한 사용자가 빌릴 수 있는 책의 권수
    Integer getActiveLoanCountByUserId(@Param("userId") String userId);
    // 대출 가능한 책 재고
    Integer getAvailableCopies(@Param("bookIsbn") Integer bookIsbn);
    void decreaseCopiesAvailable(@Param("bookIsbn") Integer bookIsbn);
    void increaseCopiesAvailable(@Param("bookIsbn") Integer bookIsbn);
    LoanDTO getActiveLoanByUserAndBook(@Param("userId") String userId, @Param("isbn") Integer isbn);
}
