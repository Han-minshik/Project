<<<<<<< HEAD:hereme/src/main/java/com/project/dto/AdminPostDTO.java
package com.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@ToString
public class AdminPostDTO {
    private int id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String userId;

    private List<UserDTO> user;
}
=======
package com.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@ToString
public class AdminPostDTO {
    private Integer id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String userId;

    private List<UserDTO> user;
}
>>>>>>> 메인-페이지-디자인(정예은):src/main/java/com/project/dto/AdminPostDTO.java
