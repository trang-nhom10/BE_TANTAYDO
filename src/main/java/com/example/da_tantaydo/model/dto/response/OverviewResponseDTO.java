package com.example.da_tantaydo.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OverviewResponseDTO {
    private Long totalBooked;
    private Double totalBookedChange;
    private Long totalPending;
    private Double totalPendingChange;
    private Long totalConfirmed;
    private Double totalConfirmedChange;
    private Long totalCancelled;
    private Double totalCancelledChange;
    private Long totalDoctors;
    private Long totalCustomers;
}