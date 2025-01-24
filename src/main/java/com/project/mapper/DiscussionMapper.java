package com.project.mapper;

import com.project.dto.DiscussionDTO;
import com.project.dto.PageInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussionMapper {
    void createDiscussion(DiscussionDTO discussion);
    String getRecentCommentByDiscussionId(Integer discussionId);
    Integer getCommentCountByDiscussion(Integer discussionId);
    List<DiscussionDTO> getCurrentDiscussion();
    Integer selectPaginatedDiscussionsTotalCount(PageInfoDTO<DiscussionDTO> pageInfo);
    List<DiscussionDTO> getDiscussions(PageInfoDTO<DiscussionDTO> pageInfo);
    List<DiscussionDTO> getDiscussionsByBookIsbn(String bookIsbn);
    List<DiscussionDTO> getDiscussionByBookTitle(PageInfoDTO<DiscussionDTO> pageInfo, String title);
    byte[] getBookImageByTitle(String title);
    List<DiscussionDTO> getMyDiscussion(PageInfoDTO<DiscussionDTO> pageInfo, String userId);
    DiscussionDTO selectDiscussionByDiscussionId(Integer discussionId);
    Integer getTotalCountByTitle(String title);
    Integer getTotalCountByUser(String userId);
}
