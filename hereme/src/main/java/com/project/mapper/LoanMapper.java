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
    Integer getActiveLoanCountByUserId(@Param("userId") String userId);
    Integer getAvailableCopies(@Param("bookIsbn") String bookIsbn); // Integer → String
    void decreaseCopiesAvailable(@Param("bookIsbn") String bookIsbn); // Integer → String
    void increaseCopiesAvailable(@Param("bookIsbn") String bookIsbn); // Integer → String
    LoanDTO getActiveLoanByUserAndBook(@Param("userId") String userId, @Param("isbn") String isbn); // Integer → String
    LocalDateTime getFirstReturnDateByBookIsbn(@Param("isbn") String isbn);
}

