<<<<<<< HEAD:hereme/src/main/java/com/project/dto/DiscussionCommentDTO.java
package com.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class DiscussionCommentDTO {
    private int id;
    private String content;
    private int like;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String userId;
    private int discussionId;

    private List<UserDTO> user;
    private List<DiscussionDTO> discussion;
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
public class DiscussionCommentDTO {
    private Integer id;
    private String content;
    private Integer like;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String userId;
    private Integer discussionId;

    private List<UserDTO> user;
    private List<DiscussionDTO> discussion;
}
>>>>>>> 메인-페이지-디자인(정예은):src/main/java/com/project/dto/DiscussionCommentDTO.java
