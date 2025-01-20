<<<<<<< HEAD:hereme/src/main/java/com/project/dto/NotificationDTO.java
package com.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class NotificationDTO {
    private int id;
    private String message;
    private String status;

    private String userId;

    private List<UserDTO> user;
}
=======
package com.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class NotificationDTO {
    private Integer id;
    private String message;
    private String status;

    private String userId;

    private List<UserDTO> user;

    private String type;
}
>>>>>>> 메인-페이지-디자인(정예은):src/main/java/com/project/dto/NotificationDTO.java
