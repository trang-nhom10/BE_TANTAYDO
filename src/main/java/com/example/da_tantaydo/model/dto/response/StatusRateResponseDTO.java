package com.example.da_tantaydo.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class StatusRateResponseDTO {
    private String status;
    private Long total;
    private Double percentage;
}