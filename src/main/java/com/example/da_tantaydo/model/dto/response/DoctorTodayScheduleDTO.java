package com.example.da_tantaydo.model.dto.response;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorTodayScheduleDTO {
    private Long scheduleId;
    private LocalDate workDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer maxPatient;
    private String scheduleStatus;
    private List<AppointmentTodayDTO> appointments;
}