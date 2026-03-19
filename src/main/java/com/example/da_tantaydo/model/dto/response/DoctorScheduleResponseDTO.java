package com.example.da_tantaydo.model.dto.response;

import com.example.da_tantaydo.model.enums.ScheduleStatus;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorScheduleResponseDTO {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private LocalDate workDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer maxPatient;
    private Integer currentPatient;
    private ScheduleStatus status;
}