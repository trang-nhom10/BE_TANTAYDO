package com.example.da_tantaydo.model.dto.request;

import com.example.da_tantaydo.model.entity.Doctor;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDTO {
    private Long customerId;
    private Long doctorId;
    private  String name;
    private LocalDateTime date;
    private String phone;
    private String gmail;
    private String address;
    private String createdAt;
    private Doctor doctor;
    private  LocalDateTime timeOpen;
    private String note;
}