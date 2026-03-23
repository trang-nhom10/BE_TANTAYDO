package com.example.da_tantaydo.model.dto.request;


import com.example.da_tantaydo.model.enums.PostType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDTO {
    private String title;
    private String content;
    private PostType status;
    private Long category;
    private LocalDateTime createAt;
}
