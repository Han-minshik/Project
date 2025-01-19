package com.project.mapper;

import com.project.dto.DiscussionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussionMapper {
    void createDiscussion(@Param("topic") String topic, @Param("contents") String contents, @Param("userId") String userId, @Param("bookIsbn") String bookIsbn);
    List<DiscussionDTO> getAllDiscussions();
    String getRecentCommentByDiscussionId(@Param("discussionId") Integer discussionId);
    Integer getCommentCountByDiscussion(@Param("discussionId") Integer discussionId);
    List<DiscussionDTO> getCurrentDiscussion();
}
