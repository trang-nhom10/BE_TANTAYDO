package com.example.da_tantaydo.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FeedbackResponseDTO {
    private Long id;
    private String fullName;
    private String gmail;
    private String sick;
    private String text;
    private LocalDateTime createdAt;
    private Integer evaluate;
}