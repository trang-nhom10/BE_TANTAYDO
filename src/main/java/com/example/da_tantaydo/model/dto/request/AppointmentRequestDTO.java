package com.example.da_tantaydo.model.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDTO {
    private Long customerId;
    private Long doctorId;
    private Long scheduleId;
    private String service;
    private String reason;
    private String note;
}