package com.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class ReviewDTO {
    private Integer id;
    private String title;
    private String content;
    private LocalDateTime uploadedAt;
    private Integer like;
    private String status;
    private LocalDateTime updatedAt;
    private String tag;
    private String comment;

    private String userId;
    private Integer bookIsbn;

    private List<UserDTO> user;
    private List<BookDTO> book;
}
