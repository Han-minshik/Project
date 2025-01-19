package com.project.mapper;

import com.project.dto.DiscussionCommentDTO;
import com.project.dto.PageInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussionCommentMapper {
    void incrementLike(@Param("commentId") Integer commentId);
    void incrementUnlike(@Param("commentId") Integer commentId);
    Integer getTotalVotesByCommentId(@Param("commentId") Integer commentId);
    String getUserIdByCommentId(@Param("commentId") Integer commentId);
    Integer getCommentCountByDiscussion(@Param("discussionId") Integer discussionId);
    void addComment(@Param("discussionId") Integer discussionId,
                    @Param("userId") String userId,
                    @Param("content") String content);
    List<DiscussionCommentDTO> getFirstDiscussionComment();
    List<DiscussionCommentDTO> getSecondDiscussionComment();
    List<DiscussionCommentDTO> getCommentsWithSortAndPagination(PageInfoDTO<DiscussionCommentDTO> pageInfo, Integer discussionId);
    Integer getTotalCommentsByDiscussionId(Integer discussionId);
}
