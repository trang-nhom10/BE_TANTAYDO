package com.example.da_tantaydo.model.dto.request;


import com.example.da_tantaydo.model.enums.PostType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDTO {
    private String title;
    private String content;
    private PostType type;
    private Long createdBy; // ID NHÂN VIÊN TẠO
}
