package com.project.service;

import com.project.dto.BookDTO;
import com.project.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BookService {

    @Autowired private BookMapper bookMapper;

    public void get_book_list(PageInfoDTO<BookDTO> pageInfo){
        pageInfo.set

        return;
    }
    public BookDTO get_book(Integer bookIsbn){
        return bookMapper.getBookByIsbn(bookIsbn);
    }

    public Map<String, Map<String, Object>> get_reviews(
            PageInfoDTO<ReviewDTO> pageInfo,
            Integer bookIsbn
    ) {

    }
}
