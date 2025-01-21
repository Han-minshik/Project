<<<<<<< HEAD:hereme/src/main/java/com/project/mapper/BookMapper.java
package com.project.mapper;

import com.project.dto.BestsellerDTO;
import com.project.dto.BookDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookMapper {
    void createBook(BookDTO book);
    BookDTO getBookByIsbn(int isbn);
    List<BookDTO> getAllBooks();
    void updateBook(BookDTO book);
    void deleteBook(int isbn);
    List<BestsellerDTO> getBestsellerList();
}
=======
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
    List<Map<String, Object>> searchBooksByNameWithCount(String title);
    BookDTO getBookByIsbn(String isbn);
    List<BookDTO> getPopularBook();
    Integer getDiscussionCountByBookIsbn(String isbn);
    Integer getParticipantCountByBookIsbn(String isbn);
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
    List<BookImageDTO> getImageByIsbn(String isbn);
}
>>>>>>> 메인-페이지-디자인(정예은):src/main/java/com/project/mapper/BookMapper.java
