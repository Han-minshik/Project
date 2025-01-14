package com.project.service;

import com.project.dto.BookDTO;
import com.project.dto.ReviewDTO;
import com.project.mapper.BookMapper;
import com.project.mapper.LoanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Service
public class BookService {
    @Autowired private BookMapper bookMapper;

    public List<BookDTO> getAllBooks() {
        return bookMapper.getAllBooks();
    }

    public void deleteBook(String isbn) { // Integer → String
        bookMapper.deleteBook(isbn);
    }

    public List<BookDTO> searchBooksByName(String title) {
        return bookMapper.searchBooksByName(title);
    }

    public BookDTO getBookDetails(String isbn) { // Integer → String
        return bookMapper.getBookDetails(isbn);
    }
}

