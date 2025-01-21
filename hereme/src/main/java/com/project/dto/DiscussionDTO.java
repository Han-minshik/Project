<<<<<<< HEAD:hereme/src/main/java/com/project/dto/DiscussionDTO.java
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
public class DiscussionDTO {
    private Integer id;
    private String topic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String userId;
    private Integer bookIsbn;

    private List<UserDTO> user;
    private List<BookDTO> book;
}
>>>>>>> 메인-페이지-디자인(정예은):src/main/java/com/project/dto/DiscussionDTO.java
