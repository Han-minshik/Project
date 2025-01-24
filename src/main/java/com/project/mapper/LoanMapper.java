package com.project.mapper;

import com.project.dto.BookDTO;
import com.project.dto.CartDTO;
import com.project.dto.LoanDTO;
import com.project.dto.PageInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface LoanMapper {
    void createLoan(@Param("loan") LoanDTO loan);
    List<LoanDTO> getLoansByUserId(@Param("userId") String userId);
    void updateLoanStatus(@Param("id") Integer id, @Param("status") String status);
    List<LoanDTO> getAllLoans();
    Integer getActiveLoanCountByUserId(@Param("userId") String userId);
    Integer getAvailableCopies(@Param("bookIsbn") String bookIsbn); // Integer → String
    void decreaseCopiesAvailable(@Param("bookIsbn") String bookIsbn); // Integer → String
    void increaseCopiesAvailable(@Param("bookIsbn") String bookIsbn); // Integer → String
    List<LoanDTO> getActiveLoanByUserAndBook(@Param("userId") String userId); // Integer → String
    LocalDateTime getFirstReturnDateByBookIsbn(@Param("isbn") String isbn);
}

