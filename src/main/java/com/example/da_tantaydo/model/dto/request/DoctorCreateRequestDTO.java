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
public class DoctorCreateRequestDTO {
    private String gmail;
    private String name;
    private String specialized;
    private LocalDate createdAt ;

}
