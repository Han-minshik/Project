package com.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class DiscussionDTO {
    private Integer id;
    private String bookTitle;
    private String topic;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String userId;
    private String bookIsbn;

    private byte[] bookImage;

    private List<UserDTO> user;
    private List<BookDTO> book;
    private List<DiscussionCommentDTO> comment;

    private String latestComment;
}
