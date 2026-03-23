package com.example.da_tantaydo.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class AvailableSlotDTO {
    private LocalDate workDate;
    private LocalTime timeSlot;
}