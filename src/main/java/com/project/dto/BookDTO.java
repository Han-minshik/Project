package com.project.dto;

import com.project.dto.CategoryDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    private Integer pageCount;
    private Integer copiesAvailable;
    private String detail;
    private CategoryDTO category;
    private byte[] image;
    // 등록일자
    private LocalDateTime createdAt;
}
