package com.project.service;

import com.project.dto.*;
import com.project.mapper.BookMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class BookService {

    private static final Logger log = LogManager.getLogger(BookService.class);

    @Autowired
    private BookMapper bookMapper;

    /**
     * 모든 책 리스트 조회
     */
    public List<BookDTO> getAllBooks() {
        return bookMapper.getAllBooks();
    }

    /**
     * 책 제목으로 검색
     */
    public PageInfoDTO<BookDTO> searchBooksByNameWithCount(PageInfoDTO<BookDTO> pageInfo, String title) {
        // 기본값 설정: 페이지 번호와 페이지 크기
        pageInfo.setPage(Math.max(pageInfo.getPage(), 1)); // 페이지 번호가 1보다 작으면 1로 설정
        pageInfo.setSize(pageInfo.getSize() != null && pageInfo.getSize() > 0 ? pageInfo.getSize() : 5); // 기본 크기: 5개

        // 책 검색 결과 조회 (Mapper 호출)
        List<Map<String, Object>> rawBooks = bookMapper.searchBooksByNameWithCount(pageInfo, title);

        // 검색 결과가 있을 경우
        if (rawBooks != null && !rawBooks.isEmpty()) {
            // 총 검색 결과 개수 계산
            Integer totalBookCount = rawBooks.stream()
                    .map(bookMap -> (Integer) bookMap.get("totalCount"))
                    .findFirst()
                    .orElse(0);

            List<BookDTO> books = rawBooks.stream().map(bookMap -> {
                BookDTO book = new BookDTO();
                book.setTitle((String) bookMap.get("title"));
                book.setAuthor((String) bookMap.get("author"));
                book.setImage((byte[]) bookMap.get("image"));
                book.setDetail((String) bookMap.get("detail"));
                book.setCopiesAvailable((Integer) bookMap.get("copiesAvailable"));
                // 필요한 필드를 추가로 설정
                return book;
            }).toList();

            pageInfo.setTotalElementCount(totalBookCount);
            pageInfo.setElements(books);
        } else {
            // 검색 결과가 없을 경우 빈 리스트 설정
            pageInfo.setTotalElementCount(0);
            pageInfo.setElements(Collections.emptyList());
        }

        return pageInfo;
    }



    /**
     * ISBN으로 책 조회
     */
    public BookDTO getBookByIsbn(String isbn) {
        try {
            return bookMapper.getBookByIsbn(isbn);
        } catch (Exception e) {
            log.error("Error while fetching book by ISBN: {}", isbn, e);
            throw new RuntimeException("Failed to fetch book by ISBN. Please try again later.");
        }
    }

    /**
     * 평균 평점이 가장 높은 책 조회
     */
    public List<BookDTO> getPopularBook() {
        return bookMapper.getPopularBook();
    }

    /**
     * 평균 평점이 높은 상위 5권 조회
     */
    public List<BookDTO> getPopularBook5() {
        return bookMapper.getPopularBook5();
    }

    /**
     * 평균 평점이 높은 상위 2권 조회
     */
    public List<BookDTO> getPopularBook2() {
        return bookMapper.getPopularBook2();
    }

    /**
     * 특정 책의 토론 주제 개수 조회
     */
    public Integer getDiscussionCountByBookIsbn(String isbn) {
        return bookMapper.getDiscussionCountByBookIsbn(isbn);
    }

    /**
     * 특정 책의 토론 참여자 수 조회
     */
    public Integer getParticipantCountByBookIsbn(String isbn) {
        return bookMapper.getParticipantCountByBookIsbn(isbn);
    }

    /**
     * 페이징된 책 리스트 반환
     */
    public PageInfoDTO<BookDTO> getPaginatedBooks(PageInfoDTO<BookDTO> pageInfo) {
        // 기본값 설정: 페이지 번호와 페이지 크기
        if (pageInfo.getPage() < 1) {
            pageInfo.setPage(1);
        }
        if (pageInfo.getSize() == null || pageInfo.getSize() <= 0) {
            pageInfo.setSize(5); // 기본 페이지 크기: 5개
        }

        // 총 책 개수 조회
        Integer totalBookCount = bookMapper.selectPaginatedBooksTotalCount(pageInfo);

        if (totalBookCount != null && totalBookCount > 0) {
            // 페이징된 책 리스트 조회
            List<BookDTO> books = bookMapper.getPaginatedBooks(pageInfo);
            pageInfo.setTotalElementCount(totalBookCount);
            pageInfo.setElements(books);
        }

        return pageInfo;
    }

    /**
     * 페이징된 리뷰 리스트와 통계 정보 반환
     */
    public PageInfoDTO<ReviewDTO> getPaginatedReviews(PageInfoDTO<ReviewDTO> pageInfo, String isbn) {
        // 기본값 설정
        if (pageInfo.getPage() < 1) {
            pageInfo.setPage(1);
        }
        if (pageInfo.getSize() == null || pageInfo.getSize() <= 0) {
            pageInfo.setSize(3); // 기본 페이지 크기: 3개
        }

        // 총 리뷰 개수 및 통계 정보 조회
        Map<String, Map<String, Object>> reviewStats = bookMapper.selectPaginatedReviewTotalCountByIsbn(isbn);

        if (reviewStats != null && !reviewStats.isEmpty()) {
            // 총 리뷰 개수 가져오기
            Integer totalReviewCount = Integer.parseInt(reviewStats.get("result").get("count").toString());
            pageInfo.setTotalElementCount(totalReviewCount);

            // 페이징된 리뷰 리스트 조회
            List<ReviewDTO> reviews = bookMapper.selectPaginatedReviewsByBookIsbn(pageInfo, isbn);
            pageInfo.setElements(reviews);
        }

        return pageInfo;
    }

    /**
     * 유저의 장바구니 상품 조회
     */
    public PageInfoDTO<CartDTO> getCartsByUser(PageInfoDTO<CartDTO> pageInfo, String userId) {
        // 페이지 번호 검증 및 초기화
        if (pageInfo.getPage() < 1) {
            pageInfo.setPage(1); // 기본값 설정
        }
        if (pageInfo.getSize() == null || pageInfo.getSize() <= 0) {
            pageInfo.setSize(5); // 기본 페이지 크기 설정
        }

        // 카트 총 개수 조회
        Integer totalCartCount = bookMapper.selectCartCountByUser(userId);
        pageInfo.setTotalElementCount(totalCartCount);

        if(totalCartCount != null && totalCartCount > 0) {
            List<CartDTO> carts = bookMapper.selectCartsByUser(pageInfo, userId);
            pageInfo.setElements(carts);
        }

        return pageInfo;
    }



    /**
     * 특정 책을 카트에 추가
     */
    public void insertBookToCart(String userId, String bookIsbn) {
        try {
            // CartDTO 객체 생성 및 값 설정
            CartDTO cart = new CartDTO();

            UserDTO user = new UserDTO();
            user.setId(userId);
            cart.setUser(user);

            BookDTO book = new BookDTO();
            book.setIsbn(bookIsbn);
            cart.setBook(book);

            bookMapper.insertBookToCart(cart);
        } catch (Exception e) {
            log.error("Error while adding book to cart for userId: {} and bookIsbn: {}", userId, bookIsbn, e);
            throw new RuntimeException("Failed to add book to cart. Please try again later.");
        }
    }


    /**
     * 특정 책을 카트에서 삭제
     */
    public void deleteBooksFromCart(List<CartDTO> carts, String userId) {
        try {
            if (carts == null || carts.isEmpty()) {
                throw new IllegalArgumentException("Cart items cannot be null or empty.");
            }

            UserDTO user = new UserDTO();
            user.setId(userId);

            bookMapper.deleteBookFromCart(carts, user);
        } catch (Exception e) {
            log.error("Error while deleting books from cart for userId: {}", userId, e);
            throw new RuntimeException("Failed to delete books from cart. Please try again later.");
        }
    }

    /**
     * 특정 책의 이미지 조회
     */
    public List<BookImageDTO> getImageByIsbn(String isbn) {
        try {
            return bookMapper.getImageByIsbn(isbn);
        } catch (Exception e) {
            log.error("Error while fetching images for book ISBN: {}", isbn, e);
            throw new RuntimeException("Failed to fetch book images. Please try again later.");
        }
    }

    /**
     * 홈 페이지 베스트셀러 조회
     */
    public List<BookDTO> getBooksForHomePage() {
        List<BookDTO> ascBooks = bookMapper.getASCBestseller();
        List<BookDTO> descBooks = bookMapper.getDESCBestseller();
        List<BookDTO> homePageBooks = new ArrayList<>();

        homePageBooks.addAll(ascBooks);
        homePageBooks.addAll(descBooks);

        return homePageBooks;
    }

    public List<CategoryDTO> getCategoryHierarchyByIsbn(String isbn) {
        List<CategoryDTO> categoryHierarchy = bookMapper.selectCategoryByIsbn(isbn);
        return categoryHierarchy;
    }

}
