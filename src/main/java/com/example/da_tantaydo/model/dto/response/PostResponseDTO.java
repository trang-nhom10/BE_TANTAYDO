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
    private String status;
    private String img;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}