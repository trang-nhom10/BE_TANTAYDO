package com.example.da_tantaydo.model.dto.response;

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
    private String name;
    private LocalDate date;
    private String phone;
    private String gmail;
    private String address;
    private String createdAt;
    private String doctorName;
    private LocalTime timeOpen;
    private String note;
    private String status;
}