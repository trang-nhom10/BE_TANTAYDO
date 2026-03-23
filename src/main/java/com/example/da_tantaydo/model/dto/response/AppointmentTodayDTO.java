package com.example.da_tantaydo.model.dto.response;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AppointmentTodayDTO {
    private Long id;
    private String nameCustomer;
    private LocalDate year;
    private LocalDate createdAt;
    private LocalTime timeOpen;
    private String status;
}