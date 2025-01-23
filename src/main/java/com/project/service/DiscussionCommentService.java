package com.project.service;

import com.project.dto.DiscussionCommentDTO;
import com.project.dto.PageInfoDTO;
import com.project.mapper.DiscussionCommentMapper;
import com.project.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
        handleVote(commentId, userId, true);
    }

    public void addUnlike(Integer commentId, String userId) {
        handleVote(commentId, userId, false);
    }

    private void handleVote(Integer commentId, String userId, boolean isLike) {
        // 사용자가 이미 투표했는지 확인
        Boolean hasVoted = discussionCommentMapper.hasUserVoted(userId, commentId);
        if (Boolean.TRUE.equals(hasVoted)) {
            throw new IllegalStateException("사용자는 이미 이 댓글에 투표했습니다.");
        }

        // 사용자 투표 기록 추가
        discussionCommentMapper.addUserVote(commentId, userId);

        // 찬성/반대 값 증가
        if (isLike) {
            discussionCommentMapper.incrementLike(commentId);
        } else {
            discussionCommentMapper.incrementUnlike(commentId);
        }

        // 찬/반 합산 값 확인 및 포인트 지급
        Integer totalVotes = discussionCommentMapper.getTotalVotesByCommentId(commentId);
        if (totalVotes >= 50) {
            String authorId = discussionCommentMapper.getUserIdByCommentId(commentId);
            userMapper.addPointToUser(authorId, POINTS_THRESHOLD);
        }
    }

    public PageInfoDTO<DiscussionCommentDTO> getCommentsWithSortAndPagination(PageInfoDTO<DiscussionCommentDTO> pageInfo, Integer discussionId) {
        if (pageInfo.getPage() < 1) {
            pageInfo.setPage(1);
        }
        if (pageInfo.getSize() == null || pageInfo.getSize() <= 0) {
            pageInfo.setSize(3);
        }

        Integer totalCommentCount = discussionCommentMapper.getTotalCommentsByDiscussionId(discussionId);
        pageInfo.setTotalElementCount(totalCommentCount);

        if (totalCommentCount != null && totalCommentCount > 0) {
            List<DiscussionCommentDTO> comments = discussionCommentMapper.getCommentsWithSortAndPagination(pageInfo, discussionId);
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
