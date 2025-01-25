package com.project.mapper;

import com.project.dto.*;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface BookMapper {
    List<BookDTO> getAllBooks();
    List<BookDTO> searchBooksByNameWithCount(@Param("pageInfo") PageInfoDTO<BookDTO> pageInfo,@Param("title") String title);
    BookDTO getBookByIsbn(@Param("isbn") String isbn);
    List<BookDTO> getPopularBook();
    Integer getDiscussionCountByBookIsbn(@Param("isbn") String isbn);
    Integer getParticipantCountByBookIsbn(@Param("isbn") String isbn);
    List<ReviewDTO> selectPaginatedReviewsByBookIsbn(@Param("pageInfo") PageInfoDTO<ReviewDTO> pageInfo, @Param("isbn") String isbn);
    @MapKey("rate")
    Map<String, Map<String, Object>> selectPaginatedReviewTotalCountByIsbn(@Param("isbn")String isbn);
    Integer selectPaginatedBooksTotalCount(@Param("pageInfo")PageInfoDTO<BookDTO> pageInfo);
    List<BookDTO> getPaginatedBooks(@Param("pageInfo") PageInfoDTO<BookDTO> pageInfo);
    List<BookDTO> getPopularBook5();
    List<BookDTO> getPopularBook2();
    List<CartDTO> selectCartsByUser(@Param("pageInfo")PageInfoDTO<CartDTO> pageInfo, @Param("userId")String userId);
    void insertBookToCart(@Param("cart")CartDTO cart);
    void deleteBookFromCart(@Param("cartNo")Integer cartNo, @Param("userId")String userId);
    List<BookImageDTO> getImageByIsbn(@Param("isbn")String isbn);
    List<BookDTO> getASCBestseller();
    List<BookDTO> getDESCBestseller();
    Integer selectCartCountByUser(@Param("userId")String userId);
    List<CategoryDTO> selectCategoryByIsbn(@Param("isbn")String isbn);
    void insertReview(@Param("userId")String userId, @Param("isbn") String isbn, @Param("content")String content);
    // 제목으로 검색된 총 책 개수 조회
    @Select("SELECT COUNT(*) FROM book WHERE title LIKE CONCAT('%', #{title}, '%')")
    Integer getTotalBookCountByTitle(@Param("title") String title);
}
