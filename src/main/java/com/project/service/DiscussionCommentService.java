package com.project.service;

import com.project.mapper.DiscussionCommentMapper;
import com.project.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscussionCommentService {

    @Autowired
    private DiscussionCommentMapper discussionCommentMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 댓글 추가 및 댓글 수 확인 후 포인트 부여
     *
     * @param discussionId 토론 ID
     * @param userId       댓글 작성자 ID
     * @param content      댓글 내용
     */
    public void addComment(Integer discussionId, String userId, String content) {
        // 댓글 추가
        discussionCommentMapper.addComment(discussionId, userId, content);

        // 댓글 수 확인
        Integer commentCount = discussionCommentMapper.getCommentCountByDiscussion(discussionId);

        // 댓글이 10개가 되면 포인트 부여
        if (commentCount == 10) {
            userMapper.addPointToUser(userId, 5000);
        }
    }

    /**
     * 찬성 클릭 및 포인트 조건 확인
     *
     * @param commentId 댓글 ID
     */
    public void addLike(Integer commentId) {
        handleVote(commentId, true);
    }

    /**
     * 반대 클릭 및 포인트 조건 확인
     *
     * @param commentId 댓글 ID
     */
    public void addUnlike(Integer commentId) {
        handleVote(commentId, false);
    }

    /**
     * 찬성/반대 클릭 처리 및 포인트 조건 확인
     *
     * @param commentId 댓글 ID
     * @param isLike    찬성 여부 (true: 찬성, false: 반대)
     */
    private void handleVote(Integer commentId, boolean isLike) {
        // 찬성 또는 반대 값 증가
        if (isLike) {
            discussionCommentMapper.incrementLike(commentId);
        } else {
            discussionCommentMapper.incrementUnlike(commentId);
        }

        // 찬/반 합산 값 확인
        Integer totalVotes = discussionCommentMapper.getTotalVotesByCommentId(commentId);

        // 합산이 50 이상이면 포인트 부여
        if (totalVotes >= 50) {
            String userId = discussionCommentMapper.getUserIdByCommentId(commentId);
            userMapper.addPointToUser(userId, 5000);
        }
    }
}
