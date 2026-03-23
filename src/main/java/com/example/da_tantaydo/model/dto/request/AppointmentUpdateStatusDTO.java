package com.example.da_tantaydo.model.dto.request;


import com.example.da_tantaydo.model.enums.AppointmentStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentUpdateStatusDTO {
    private AppointmentStatus status;
}