package com.example.da_tantaydo.model.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponseDTO {
    private Long id;
    private String name;
    private LocalDateTime date;
    private String phone;
    private String gmail;
    private String address;
    private String createdAt;
    private String doctorName;
    private LocalDateTime timeOpen;
    private String note;
    private String status;
}