package com.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ComplainDTO {
    private Integer no;
    private String title;
    private String contents;
    private String userId;
    private String status;

    private List<UserDTO> user;
}
