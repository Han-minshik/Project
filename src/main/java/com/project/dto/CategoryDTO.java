package com.project.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Getter
@Setter
@ToString
public class CategoryDTO {
    private Integer no;
    private String name;
    private List<CategoryDTO> children;
    private Integer level;
}
