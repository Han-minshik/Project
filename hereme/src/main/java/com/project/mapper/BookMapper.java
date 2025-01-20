<<<<<<< HEAD:hereme/src/main/java/com/project/mapper/BookMapper.java
package com.project.mapper;

import com.project.dto.BestsellerDTO;
import com.project.dto.BookDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookMapper {
    void createBook(BookDTO book);
    BookDTO getBookByIsbn(int isbn);
    List<BookDTO> getAllBooks();
    void updateBook(BookDTO book);
    void deleteBook(int isbn);
    List<BestsellerDTO> getBestsellerList();
}
=======
package com.project.mapper;

import com.project.dto.BookDTO;
import com.project.dto.BookImageDTO;
import com.project.dto.ReviewDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookMapper {
    void createBook(BookDTO book);
    List<BookDTO> getAllBooks();
    void updateBook(BookDTO book);
    void deleteBook(Integer isbn);
    // 책 제목으로 검색
    List<BookDTO> searchBooksByName(@Param("title") String title);
    List<ReviewDTO> findReviewTitlesByBookTitle(@Param("title") String title);
    BookDTO getBookDetails(@Param("isbn") Integer isbn);
}
>>>>>>> 메인-페이지-디자인(정예은):src/main/java/com/project/mapper/BookMapper.java
