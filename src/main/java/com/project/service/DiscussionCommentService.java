package com.project.service;

import com.project.dto.DiscussionCommentDTO;
import com.project.dto.PageInfoDTO;
import com.project.dto.ReviewDTO;
import com.project.mapper.DiscussionCommentMapper;
import com.project.mapper.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class DiscussionCommentService {

    @Autowired
    private DiscussionCommentMapper discussionCommentMapper;

    @Autowired
    private UserMapper userMapper;

    private static final int POINTS_THRESHOLD = 1000; // 포인트 지급 값
    private static final int COMMENT_COUNT_THRESHOLD = 10; // 댓글 10개 조건

    public void addComment(Integer discussionId, String userId, String content) {
        // 댓글 추가
        discussionCommentMapper.addComment(discussionId, userId, content);

        // 댓글 수 확인
        Integer commentCount = discussionCommentMapper.getCommentCountByDiscussion(discussionId);

        // 댓글이 10개에 도달하면 포인트 부여
        if (commentCount == COMMENT_COUNT_THRESHOLD) {
            String authorId = discussionCommentMapper.getDiscussionAuthorById(discussionId); // 토론 작성자 ID 가져오기
            userMapper.addPointToUser(authorId, POINTS_THRESHOLD);
        }
    }

    public void addLike(Integer commentId, String userId) {
        if (discussionCommentMapper.hasUserVoted(userId, commentId) > 0) {
            throw new IllegalStateException("이미 투표한 댓글입니다.");
        }
        log.error("commentId: " + commentId + ", userId: " + userId);
        discussionCommentMapper.addUserVote(commentId, userId);
        discussionCommentMapper.incrementLike(commentId);
    }

    public void addUnlike(Integer commentId, String userId) {
        if (discussionCommentMapper.hasUserVoted(userId, commentId) > 0) {
            throw new IllegalStateException("이미 투표한 댓글입니다.");
        }
        log.error("commentId: " + commentId + ", userId: " + userId);
        discussionCommentMapper.addUserVote(commentId, userId);
        discussionCommentMapper.incrementUnlike(commentId);
    }

    public Integer getLikeCount(Integer commentId) {
        return discussionCommentMapper.getLikeCount(commentId);
    }

    public Integer getUnlikeCount(Integer commentId) {
        return discussionCommentMapper.getUnlikeCount(commentId);
    }

    public Integer getDiscussionIdByCommentId(Integer commentId) {
        return discussionCommentMapper.getDiscussionIdByCommentId(commentId);
    }

    public PageInfoDTO<DiscussionCommentDTO> getCommentsWithSortAndPagination(PageInfoDTO<DiscussionCommentDTO> pageInfo, Integer discussionId) {
        pageInfo.setSize(3);
        if(pageInfo.getPage() < 1) {
            return null;
        }
        List<DiscussionCommentDTO> comments = discussionCommentMapper.getCommentsWithSortAndPagination(pageInfo, discussionId);
        if(!comments.isEmpty()) {
            Integer totalElementCount = discussionCommentMapper.getTotalCommentsByDiscussionId(discussionId);
            pageInfo.setTotalElementCount(totalElementCount);
            pageInfo.setElements(comments);
        }
        return pageInfo;
    }


    public DiscussionCommentDTO getFirstComment() {
        return discussionCommentMapper.getFirstComment();
    }

    public DiscussionCommentDTO getSecondComment() {
        return discussionCommentMapper.getSecondComment();
    }

}
