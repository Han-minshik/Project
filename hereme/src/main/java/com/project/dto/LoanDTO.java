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
    private int id;
    private LocalDateTime loanDate;
    private LocalDateTime returnDate;
    private String status;

    private String userId;
    private int bookIsbn;

    private List<UserDTO> user;
    private List<BookDTO> book;
}
