package com.project.mapper;

import com.project.dto.BookDTO;
import com.project.dto.BookImageDTO;
import com.project.dto.ReviewDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookMapper {
    // adminMapper로 이동
//    void createBook(BookDTO book);
//    List<BookDTO> getAllBooks();


    /*************************************/
    // 책 제목으로 검색
    List<BookDTO> searchBooksByName(@Param("title") String title);
    List<ReviewDTO> findReviewTitlesByBookTitle(@Param("title") String title);
    BookDTO getBookDetails(@Param("isbn") Integer isbn);

}
