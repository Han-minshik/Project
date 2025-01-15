package com.project.dto;

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
    private Integer isbn;
    private String title;
    private String author;
    private Integer price;
    private String publisher;
    private LocalDate publicationDate;
    private String genre;
    private Integer rating;
    private String rentalState;
    private Integer copiesAvailable;
    private String detail;
    // 등록일자
    private LocalDateTime createdAt;
    private CategoryDTO category;

    private List<BookImageDTO> images;
}
