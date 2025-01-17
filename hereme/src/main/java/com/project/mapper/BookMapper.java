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
