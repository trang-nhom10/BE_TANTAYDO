package com.example.da_tantaydo.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyStatResponseDTO {
    private Integer year;
    private Integer month;
    private Long total;
    private Long confirmed;
    private Long pending;
    private Long cancelled;
}