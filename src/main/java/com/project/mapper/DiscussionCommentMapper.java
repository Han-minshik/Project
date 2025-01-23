package com.project.mapper;

import com.project.dto.DiscussionCommentDTO;
import com.project.dto.PageInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussionCommentMapper {
    void incrementLike(Integer commentId);
    void incrementUnlike(Integer commentId);
    Integer getTotalVotesByCommentId(Integer commentId);
    String getUserIdByCommentId(Integer commentId);
    Integer getCommentCountByDiscussion(Integer discussionId);
    void addComment(@Param("discussionId") Integer discussionId, @Param("userId") String userId, @Param("content") String content);
    Boolean hasUserVoted(@Param("userId") String userId, @Param("commentId") Integer commentId);
    void addUserVote(@Param("commentId") Integer commentId, @Param("userId") String userId);
    String getDiscussionAuthorById(Integer discussionId);
    List<DiscussionCommentDTO> getFirstDiscussionComment();
    List<DiscussionCommentDTO> getSecondDiscussionComment();
    List<DiscussionCommentDTO> getCommentsWithSortAndPagination(@Param("pageInfo") PageInfoDTO<DiscussionCommentDTO> pageInfo,
                                                                @Param("discussionId") Integer discussionId);
    Integer getTotalCommentsByDiscussionId(Integer discussionId);
    DiscussionCommentDTO getFirstComment();
    DiscussionCommentDTO getSecondComment();
}
