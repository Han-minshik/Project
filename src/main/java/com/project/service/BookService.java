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
import java.util.stream.Collectors;

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
        List<BookDTO> result = bookMapper.searchBooksByNameWithCount(pageInfo, title);
        Integer totalElementCount = bookMapper.getTotalBookCountByTitle(title); // 총 개수 가져오기
        pageInfo.setTotalElementCount(totalElementCount);
        pageInfo.setElements(result);
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
    public Map<String, Map<String, Object>> getPaginatedReviews(PageInfoDTO<ReviewDTO> pageInfo, String isbn) {
        pageInfo.setSize(3);
        if(pageInfo.getPage() < 1) {
            return null;
        }
        Map<String, Map<String,Object>> result = bookMapper.selectPaginatedReviewTotalCountByIsbn(isbn);
        log.error(result);
        if(!result.isEmpty()) {
            Integer totalElementCount = Integer.parseInt(result.get("result").get("count").toString());
            var reviews = bookMapper.selectPaginatedReviewsByBookIsbn(pageInfo, isbn);
            pageInfo.setTotalElementCount(totalElementCount);
            pageInfo.setElements(reviews);
        }
        return result;
    }


    /**
     * 유저의 장바구니 상품 조회
     */
    public List<CartDTO> getCartsByUser(UserDTO user) {
        List<CartDTO> wishlist = bookMapper.selectCartsByUser(user);
        log.error("Raw Wishlist: {}", wishlist);

        // Null 요소 제거
        wishlist = wishlist.stream()
                .filter(cart -> cart != null && cart.getBook() != null)
                .collect(Collectors.toList());

        log.error("Filtered Wishlist: {}", wishlist);
        return wishlist;
    }


    public Integer getCartCountByUser(String userId) {
        return bookMapper.selectCartCountByUser(userId);
    }

    /**
     * 특정 책을 카트에 추가
     */
    public CartDTO insertBookToCart(BookDTO book, UserDTO user) {
        if (book == null || user == null) {
            throw new IllegalArgumentException("책 정보나 사용자 정보가 null입니다.");
        }
        log.info("Book Info: {}", book);
        log.info("User Info: {}", user);

        CartDTO cart = new CartDTO();
        cart.setBook(book);
        cart.setUser(user);

        bookMapper.insertBookToCart(cart, user); // MyBatis 매퍼 호출
        return cart;
    }


    /**
     * 특정 책을 카트에서 삭제
     */
    public void deleteBooksFromCart(Integer cartNo, String userId) {
        try {
//            if (carts == null || carts.isEmpty()) {
//                throw new IllegalArgumentException("Cart items cannot be null or empty.");
//            }

            // UserDTO user = new UserDTO();
            // user.setId(userId);

            bookMapper.deleteBookFromCart(cartNo, userId);
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
        log.error(categoryHierarchy);
        return categoryHierarchy;
    }

    public void insertReview(String userId, String isbn, String content) {
        bookMapper.insertReview(userId, isbn, content);
    }

}
