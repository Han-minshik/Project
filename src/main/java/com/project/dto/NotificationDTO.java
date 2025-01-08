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
