package com.project.mapper;

import com.project.dto.BookDTO;
import com.project.dto.DiscussionDTO;
import com.project.dto.PageInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DiscussionMapper {
    void createDiscussion(@Param("topic") String topic, @Param("contents") String contents, @Param("userId") String userId, @Param("bookIsbn") String bookIsbn);
    List<DiscussionDTO> getAllDiscussions();
    String getRecentCommentByDiscussionId(@Param("discussionId") Integer discussionId);
    Integer getCommentCountByDiscussion(@Param("discussionId") Integer discussionId);
    List<DiscussionDTO> getCurrentDiscussion();
    Integer selectPaginatedDiscussionsTotalCount(PageInfoDTO<DiscussionDTO> pageInfo);
    List<DiscussionDTO> getDiscussionsWithBookInfo(PageInfoDTO<DiscussionDTO> pageInfo);

}
