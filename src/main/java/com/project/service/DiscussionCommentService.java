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

    public void addLike(Integer commentId) {
        handleVote(commentId, true);
    }

    public void addUnlike(Integer commentId) {
        handleVote(commentId, false);
    }

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

    public void getCommentsWithSortAndPagination(PageInfoDTO<DiscussionCommentDTO> pageInfo, Integer discussionId) {
        // 기본값 설정
        if (pageInfo.getPage() < 1) {
            pageInfo.setPage(1);
        }
        if (pageInfo.getSize() == null) {
            pageInfo.setSize(5); // 기본 보기 설정
        }

        // 총 댓글 수 조회
        Integer totalCommentCount = discussionCommentMapper.getCommentCountByDiscussion(discussionId);

        if (totalCommentCount != null && totalCommentCount > 0) {
            // 댓글 데이터 조회
            List<DiscussionCommentDTO> comments = discussionCommentMapper.getCommentsWithSortAndPagination(pageInfo, discussionId);
            pageInfo.setTotalElementCount(totalCommentCount);
            pageInfo.setElements(comments);
        }
    }
}
