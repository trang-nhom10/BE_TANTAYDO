package com.example.da_tantaydo.model.dto.response;


import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgeDistributionResponseDTO {
    private String ageGroup;
    private Long total;
    private Double percentage;
}
