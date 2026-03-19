package com.example.da_tantaydo.model.dto.response;


import com.example.da_tantaydo.model.enums.AppointmentStatus;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponseDTO {
    private Long id;
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private Long doctorId;
    private String doctorName;
    private Long scheduleId;
    private LocalDate workDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String service;
    private String reason;
    private String note;
    private AppointmentStatus status;
    private LocalDateTime createdAt;
}