package com.project.mapper;

import com.project.dto.*;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface BookMapper {
    List<BookDTO> getAllBooks();
    @MapKey("isbn")
    List<Map<String, Object>> searchBooksByNameWithCount(@Param("title") String title);
    BookDTO getBookByIsbn(@Param("isbn") String isbn);
    List<BookDTO> getPopularBook();
    Integer getDiscussionCountByBookIsbn(@Param("isbn") String isbn);
    Integer getParticipantCountByBookIsbn(@Param("isbn") String isbn);
    List<ReviewDTO> selectPaginatedReviewsByBookIsbn(PageInfoDTO<ReviewDTO> pageInfo, String isbn);
    @MapKey("rate")
    Map<String, Map<String, Object>> selectPaginatedReviewTotalCountByIsbn(String isbn);
    Integer selectPaginatedBooksTotalCount(PageInfoDTO<BookDTO> pageInfo);
    List<BookDTO> getPaginatedBooks(@Param("pageInfo") PageInfoDTO<BookDTO> pageInfo);
    List<BookDTO> getPopularBook5();
    List<BookDTO> getPopularBook2();
    List<CartDTO> selectCartsByUser(UserDTO user);
    void insertBookToCart(CartDTO cart);
    void deleteBookFromCart(List<CartDTO> carts, UserDTO user);
}
