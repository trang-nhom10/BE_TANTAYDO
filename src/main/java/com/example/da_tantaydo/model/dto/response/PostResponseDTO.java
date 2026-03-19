package com.example.da_tantaydo.model.dto.response;


import com.example.da_tantaydo.model.enums.PostType;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDTO {
    private Long id;
    private String title;
    private String content;
    private PostType type;
    private String createdByName; // TÊN NHÂN VIÊN TẠO
    private LocalDateTime createdAt;
}