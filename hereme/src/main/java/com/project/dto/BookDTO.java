<<<<<<< HEAD:hereme/src/main/java/com/project/dto/BookDTO.java
package com.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class BookDTO {
    private int isbn;
    private String title;
    private String author;
    private String publisher;
    private LocalDate publicationDate;
    private String genre;
    private Integer rating;
    private String rentalState;
    private int copiesAvailable;
    private String detail;
    private byte[] coverImage;
    // 등록일자
    private LocalDateTime createdAt;


    private String userId;
}
=======
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
>>>>>>> 메인-페이지-디자인(정예은):src/main/java/com/project/dto/BookDTO.java
