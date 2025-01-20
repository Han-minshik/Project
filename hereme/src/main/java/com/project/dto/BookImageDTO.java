package com.project.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookImageDTO {
    private Integer no;
    private String originalFilename;
    @ToString.Exclude
    private byte[] data;
}
