package com.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class BookDTO {
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String publicationDate;
    private Integer price;
    private Integer copiesAvailable;
    private String detail;
    private CategoryDTO category;
    private byte[] image;
    // 등록일자
    private LocalDateTime createdAt;
    private Integer itemId;
    private Integer pageCount;
}
