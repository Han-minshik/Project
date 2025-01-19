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

    public void createDiscussion(String topic, String contents, String userId, String bookIsbn) {
        discussionMapper.createDiscussion(topic, contents, userId, bookIsbn);
    }

    public List<DiscussionDTO> getAllDiscussions() {
        return discussionMapper.getAllDiscussions();
    }

    public String getRecentCommentByDiscussionId(Integer discussionId) {
        return discussionMapper.getRecentCommentByDiscussionId(discussionId);
    }

    public Integer getCommentCountByDiscussion(Integer discussionId) {
        return discussionMapper.getCommentCountByDiscussion(discussionId);
    }

    public List<DiscussionDTO> getCurrentDiscussion() {
        return discussionMapper.getCurrentDiscussion();
    }

    public void getDiscussionsWithBookInfo(PageInfoDTO<DiscussionDTO> pageInfo) {
        // 페이지 기본값 설정
        if (pageInfo.getPage() < 1) {
            pageInfo.setPage(1);
        }
        if (pageInfo.getSize() == null) {
            pageInfo.setSize(5); // 기본 보기 설정
        }

        // 총 토론 개수 조회
        Integer totalDiscussionCount = discussionMapper.selectPaginatedDiscussionsTotalCount(pageInfo);

        // 데이터가 존재할 경우 페이징 처리
        if (totalDiscussionCount != null && totalDiscussionCount > 0) {
            List<DiscussionDTO> discussions = discussionMapper.getDiscussionsWithBookInfo(pageInfo);
            pageInfo.setTotalElementCount(totalDiscussionCount);
            pageInfo.setElements(discussions);
        }
    }
}
