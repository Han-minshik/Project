package com.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class DiscussionCommentDTO {
    private int id;
    private String content;
    private int like;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String userId;
    private int discussionId;

    private List<UserDTO> user;
    private List<DiscussionDTO> discussion;
}
