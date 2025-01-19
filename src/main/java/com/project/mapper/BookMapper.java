package com.project.mapper;

import com.project.dto.BookDTO;
import com.project.dto.ReviewDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookMapper {
    List<BookDTO> getAllBooks();
    List<BookDTO> searchBooksByName(@Param("title") String title);
    BookDTO getBookByIsbn(@Param("isbn") String isbn);
    List<BookDTO> getPopularBook();
    Double getAverageRatingByIsbn(@Param("isbn") String isbn);
    List<ReviewDTO> getBookReviewsWithLikes(@Param("isbn") String isbn);
    Integer getDiscussionCountByBookIsbn(@Param("isbn") String isbn);
    Integer getParticipantCountByBookIsbn(@Param("isbn") String isbn);
    Integer getCountReviews(String isbn);
}
