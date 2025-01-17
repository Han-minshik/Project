package com.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ComplainDTO {
    private Integer no;
    private String title;
    private String contents;
    private String userId;
}
