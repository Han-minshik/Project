package com.project.service;

import com.project.dto.DiscussionDTO;
import com.project.dto.PageInfoDTO;
import com.project.mapper.DiscussionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussionService {

    @Autowired
    private DiscussionMapper discussionMapper;

    /**
     * 새로운 토론 주제를 생성
     */
    public void createDiscussion(String topic, String contents, String userId, String bookIsbn) {
        discussionMapper.createDiscussion(topic, contents, userId, bookIsbn);
    }

    /**
     * 가장 최근의 댓글 내용 반환
     */
    public String getRecentCommentByDiscussionId(Integer discussionId) {
        return discussionMapper.getRecentCommentByDiscussionId(discussionId);
    }

    /**
     * 특정 토론에 대한 댓글 수 반환
     */
    public Integer getCommentCountByDiscussion(Integer discussionId) {
        return discussionMapper.getCommentCountByDiscussion(discussionId);
    }

    /**
     * 가장 최근의 토론 주제 반환 (최신 5개)
     */
    public List<DiscussionDTO> getCurrentDiscussions() {
        List<DiscussionDTO> discussions = discussionMapper.getCurrentDiscussion();
        return discussions.size() > 5 ? discussions.subList(0, 5) : discussions;
    }

    /**
     * 페이징된 토론 목록 반환 (책 정보 포함)
     */
    public PageInfoDTO<DiscussionDTO> getDiscussionsWithBookInfo(PageInfoDTO<DiscussionDTO> pageInfo) {
        // 기본 페이지 설정
        if (pageInfo.getPage() < 1) {
            pageInfo.setPage(1);
        }
        if (pageInfo.getSize() == null || pageInfo.getSize() <= 0) {
            pageInfo.setSize(5); // 기본 5개씩 노출
        }

        // 총 토론 개수 조회
        Integer totalDiscussionCount = discussionMapper.selectPaginatedDiscussionsTotalCount(pageInfo);

        // 데이터가 존재할 경우 페이징 처리
        if (totalDiscussionCount != null && totalDiscussionCount > 0) {
            List<DiscussionDTO> discussions = discussionMapper.getDiscussionsWithBookInfo(pageInfo);
            pageInfo.setTotalElementCount(totalDiscussionCount);
            pageInfo.setElements(discussions);
        }

        return pageInfo;
    }

    /**
     * 특정 토론 ID에 대한 토론 내용 반환
     */
    public DiscussionDTO getDiscussion(Integer discussionID) {
        List<DiscussionDTO> discussion = discussionMapper.getDiscussion(discussionID);
        return discussion != null && !discussion.isEmpty() ? discussion.getFirst() : null;
    }
}
