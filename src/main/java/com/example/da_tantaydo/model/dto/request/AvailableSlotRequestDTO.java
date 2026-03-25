package com.example.da_tantaydo.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AvailableSlotRequestDTO {
    private Long doctorId;
    private LocalDate workDate;
}