package com.project.service;

import com.project.dto.BookDTO;
import com.project.dto.PageInfoDTO;
import com.project.dto.ReviewDTO;
import com.project.mapper.BookMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BookService {

    private static final Logger log = LogManager.getLogger(BookService.class);

    @Autowired
    private BookMapper bookMapper;

    public List<BookDTO> getAllBooks() {
        return bookMapper.getAllBooks();
    }

    public List<BookDTO> searchBooksByName(String title) {
        return bookMapper.searchBooksByName(title);
    }

    public BookDTO getBookByIsbn(String isbn) {
        return bookMapper.getBookByIsbn(isbn);
    }

    public List<BookDTO> getPopularBook() {
        return bookMapper.getPopularBook();
    }

    public Map<String, Map<String, Object>> getPaginatedReviews(PageInfoDTO<ReviewDTO> pageInfo, String isbn) {
        // 최소 페이지 번호 확인
        pageInfo.setSize(3); // 리뷰는 기본적으로 3개씩 표시
        if (pageInfo.getPage() < 1) {
            return null;
        }

        // 총 리뷰 개수 및 통계 정보 조회
        Map<String, Map<String, Object>> result = bookMapper.selectPaginatedReviewTotalCountByIsbn(isbn);
        log.error(result);
        if (!result.isEmpty()) {
            // 총 리뷰 개수 가져오기
            Integer totalReviewCount = Integer.parseInt(result.get("result").get("count").toString());
            pageInfo.setTotalElementCount(totalReviewCount);

            // 페이징된 리뷰 리스트 조회
            List<ReviewDTO> reviews = bookMapper.selectPaginatedReviewsByBookIsbn(pageInfo, isbn);
            pageInfo.setElements(reviews);
        }

        return result;
    }

    public void getPaginatedBooks(PageInfoDTO<BookDTO> pageInfo) {
        // 기본값 설정: 페이지 번호와 보기 설정 (size)
        if (pageInfo.getPage() < 1) {
            pageInfo.setPage(1);
        }
        if (pageInfo.getSize() == null) {
            pageInfo.setSize(5); // 기본 보기 설정
        }

        // 총 책 개수 조회
        Integer totalBookCount = bookMapper.selectPaginatedBooksTotalCount(pageInfo);

        // 데이터가 존재할 경우 페이징 처리
        if (totalBookCount != 0) {
            // 페이징된 책 리스트 조회
            List<BookDTO> books = bookMapper.getPaginatedBooks(pageInfo);
            pageInfo.setTotalElementCount(totalBookCount);
            pageInfo.setElements(books);
        }
    }
}
