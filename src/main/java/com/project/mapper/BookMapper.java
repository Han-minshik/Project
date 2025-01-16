package com.project.mapper;

import com.project.dto.BookDTO;
import com.project.dto.ReviewDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookMapper {
    List<BookDTO> getAllBooks();
    void deleteBook(String isbn); // Integer → String
    List<BookDTO> searchBooksByName(@Param("title") String title);
    BookDTO getBookByIsbn(@Param("isbn") String isbn);
}
