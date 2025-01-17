package com.project.service;

import com.project.dto.DiscussionDTO;
import com.project.mapper.DiscussionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussionService {

    @Autowired
    private DiscussionMapper discussionMapper;

    /**
     * 새로운 토론 생성
     *
     * @param topic   토론 주제
     * @param contents 내용
     * @param userId   사용자 ID
     * @param bookIsbn 책 ISBN
     */
    public void createDiscussion(String topic, String contents, String userId, String bookIsbn) {
        discussionMapper.createDiscussion(topic, contents, userId, bookIsbn);
    }

    /**
     * 모든 토론 조회
     *
     * @return 토론 리스트
     */
    public List<DiscussionDTO> getAllDiscussions() {
        return discussionMapper.getAllDiscussions();
    }

    /**
     * 특정 토론의 최근 댓글 가져오기
     *
     * @param discussionId 토론 ID
     * @return 최근 댓글 내용
     */
    public String getRecentCommentByDiscussionId(Integer discussionId) {
        return discussionMapper.getRecentCommentByDiscussionId(discussionId);
    }

    /**
     * 특정 토론의 댓글 수 조회
     *
     * @param discussionId 토론 ID
     * @return 댓글 개수
     */
    public Integer getCommentCountByDiscussion(Integer discussionId) {
        return discussionMapper.getCommentCountByDiscussion(discussionId);
    }

    /**
     * 가장 최근의 토론 주제 조회
     *
     * @return 최신 토론 주제
     */
    public List<DiscussionDTO> getCurrentDiscussion() {
        return discussionMapper.getCurrentDiscussion();
    }
}
