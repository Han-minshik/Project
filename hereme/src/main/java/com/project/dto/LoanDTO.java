<<<<<<< HEAD:hereme/src/main/java/com/project/dto/LoanDTO.java
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
=======
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
    private Integer bookIsbn;

    private Integer discountPrice;
    private Integer finalPrice;

    private List<UserDTO> user;
    private List<BookDTO> book;
}
>>>>>>> 메인-페이지-디자인(정예은):src/main/java/com/project/dto/LoanDTO.java
