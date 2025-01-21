package com.project.mapper;

import com.project.dto.LoanDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface LoanMapper {
    void createLoan(LoanDTO loan);
    List<LoanDTO> getLoansByUserId(String userId);
    void updateLoanStatus(@Param("id") Integer id, @Param("status") String status);
    List<LoanDTO> getAllLoans();
    Integer getActiveLoanCountByUserId(String userId);
    Integer getAvailableCopies(String bookIsbn); // Integer → String
    void decreaseCopiesAvailable( String bookIsbn); // Integer → String
    void increaseCopiesAvailable(String bookIsbn); // Integer → String
    LoanDTO getActiveLoanByUserAndBook(String userId); // Integer → String
    LocalDateTime getFirstReturnDateByBookIsbn(String isbn);
}

