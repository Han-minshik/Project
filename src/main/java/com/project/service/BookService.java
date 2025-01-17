package com.project.service;

import com.project.dto.BookDTO;
import com.project.dto.ReviewDTO;
import com.project.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookMapper bookMapper;

    // 책 추가
    public void createBook(BookDTO book) {
        bookMapper.createBook(book);
    }

    // 책 목록 조회, pagination 필요
    public List<BookDTO> getAllBooks() {
        return bookMapper.getAllBooks();
    }

    // 책 수정
    public void updateBook(BookDTO book) {
        bookMapper.updateBook(book);
    }

    // 책 삭제
    public void deleteBook(Integer isbn) {
        bookMapper.deleteBook(isbn);
    }

    // 책 검색 by 검색어
    public List<BookDTO> searchBooksByName(String keyword) {
        return bookMapper.searchBooksByName(keyword);
    }

    // 리뷰 조회
    public List<ReviewDTO> findReviewTitlesByBookTitle(String title) {
        return bookMapper.findReviewTitlesByBookTitle(title);
    }

    // 책 페이지 내용 조회
    public BookDTO getBookDetails(Integer isbn) { 
        return bookMapper.getBookDetails(isbn);
    }
}
