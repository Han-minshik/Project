package com.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class BestsellerDTO {
    private int id;
    private int rank;
    private LocalDateTime createdAt;

    private int bookIsbn;

    private List<BookDTO> book;
}
