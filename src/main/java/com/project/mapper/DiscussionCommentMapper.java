package com.project.mapper;

import com.project.dto.DiscussionCommentDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussionCommentMapper {

    /**
     * 찬성 값 증가
     *
     * @param commentId 댓글 ID
     */
    void incrementLike(@Param("commentId") Integer commentId);

    /**
     * 반대 값 증가
     *
     * @param commentId 댓글 ID
     */
    void incrementUnlike(@Param("commentId") Integer commentId);

    /**
     * 특정 댓글의 찬/반 합산 조회
     *
     * @param commentId 댓글 ID
     * @return 찬/반 합산
     */
    Integer getTotalVotesByCommentId(@Param("commentId") Integer commentId);

    /**
     * 특정 댓글 작성자 조회
     *
     * @param commentId 댓글 ID
     * @return 작성자 ID
     */
    String getUserIdByCommentId(@Param("commentId") Integer commentId);

    /**
     * 특정 토론의 댓글 수 조회
     *
     * @param discussionId 토론 ID
     * @return 댓글 개수
     */
    Integer getCommentCountByDiscussion(@Param("discussionId") Integer discussionId);

    /**
     * 댓글 추가
     *
     * @param discussionId 토론 ID
     * @param userId       작성자 ID
     * @param content      댓글 내용
     */
    void addComment(@Param("discussionId") Integer discussionId,
                    @Param("userId") String userId,
                    @Param("content") String content);

    List<DiscussionCommentDTO> getFirstDiscussionComment();

    List<DiscussionCommentDTO> getSecondDiscussionComment();
}
