package com.project.service;

import com.project.dto.BookDTO;
import com.project.dto.ReviewDTO;
import com.project.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired private BookMapper bookMapper;
    public void createBook(BookDTO book) {
        bookMapper.createBook(book);
    }

    public BookDTO getBookByIsbn(int isbn) {
        return bookMapper.getBookByIsbn(isbn);
    }

    public List<BookDTO> getAllBooks() {
        return bookMapper.getAllBooks();
    }

    public void updateBook(BookDTO book) {
        bookMapper.updateBook(book);
    }

    public void deleteBook(int isbn) {
        bookMapper.deleteBook(isbn);
    }

    public List<BookDTO> searchBooksByName(String title) {
        return bookMapper.searchBooksByName(title);
    }

    public List<ReviewDTO> findReviewTitlesByBookTitle(String title) {
        return bookMapper.findReviewTitlesByBookTitle(title);
    }
}
