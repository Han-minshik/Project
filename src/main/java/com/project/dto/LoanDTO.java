package com.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class LoanDTO {
    private Integer id;
    private LocalDateTime loanDate;
    private LocalDateTime returnDate;
    private String status;

    private String userId;
    private String bookIsbn;

    private Integer discountPrice;
    private Integer finalPrice;

    private List<UserDTO> user;
    private List<BookDTO> book;
}
