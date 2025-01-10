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
    private int id;
    private String title;
    private String content;
    private LocalDateTime uploadedAt;
    private int like;
    private String status;
    private LocalDateTime updatedAt;
    private String tag;
    private String comment;

    private String userId;
    private int bookIsbn;

    private List<UserDTO> user;
    private List<BookDTO> book;
}
