package com.project.service;

import com.project.dto.BookDTO;
import com.project.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookMapper bookMapper;

    /**
     * 모든 책 리스트 조회
     *
     * @return 책 리스트
     */
    public List<BookDTO> getAllBooks() {
        return bookMapper.getAllBooks();
    }

    /**
     * 제목으로 책 검색
     *
     * @param title 책 제목
     * @return 검색된 책 리스트
     */
    public List<BookDTO> searchBooksByName(String title) {
        return bookMapper.searchBooksByName(title);
    }

    /**
     * ISBN으로 특정 책 상세 정보 조회
     *
     * @param isbn 책 ISBN
     * @return 책 상세 정보
     */
    public BookDTO getBookByIsbn(String isbn) {
        return bookMapper.getBookByIsbn(isbn);
    }

    /**
     * 평균 평점이 가장 높은 책 조회
     *
     * @return 인기 있는 책 (평균 평점 기준)
     */
    public List<BookDTO> getPopularBook() {
        return bookMapper.getPopularBook();
    }
}
