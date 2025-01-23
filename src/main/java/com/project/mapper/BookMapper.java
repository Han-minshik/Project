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
    PageInfoDTO<BookDTO> searchBooksByNameWithCount(PageInfoDTO<BookDTO> pageInfo, String title);
    BookDTO getBookByIsbn(String isbn);
    List<BookDTO> getPopularBook();
    Integer getDiscussionCountByBookIsbn(String isbn);
    Integer getParticipantCountByBookIsbn(String isbn);
    List<ReviewDTO> selectPaginatedReviewsByBookIsbn(PageInfoDTO<ReviewDTO> pageInfo, String isbn);
    @MapKey("rate")
    Map<String, Map<String, Object>> selectPaginatedReviewTotalCountByIsbn(String isbn);
    Integer selectPaginatedBooksTotalCount(PageInfoDTO<BookDTO> pageInfo);
    List<BookDTO> getPaginatedBooks(PageInfoDTO<BookDTO> pageInfo);
    List<BookDTO> getPopularBook5();
    List<BookDTO> getPopularBook2();
    List<CartDTO> selectCartsByUser(@Param("pageInfo") PageInfoDTO<CartDTO> pageInfo, String userId);
    void insertBookToCart(CartDTO cart);
    void deleteBookFromCart(Integer cartNo, String userId);
    List<BookImageDTO> getImageByIsbn(String isbn);
    List<BookDTO> getASCBestseller();
    List<BookDTO> getDESCBestseller();
    Integer selectCartCountByUser(String userId);
    List<CategoryDTO> selectCategoryByIsbn(String isbn);
}
