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
    private int id;
    private String topic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String userId;
    private int bookIsbn;

    private List<UserDTO> user;
    private List<BookDTO> book;
}
