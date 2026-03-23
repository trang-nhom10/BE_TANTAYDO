package com.example.da_tantaydo.model.dto.request;

import com.example.da_tantaydo.model.entity.Doctor;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDTO {
    private Long customerId;
    private Long doctorId;
    private String name;
    private LocalDate date;
    private String phone;
    private String gmail;
    private String address;
    private LocalDate createdAt;
    private Doctor doctor;
    private LocalTime timeOpen;
    private String note;
}