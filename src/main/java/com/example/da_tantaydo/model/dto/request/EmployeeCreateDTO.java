package com.example.da_tantaydo.model.dto.request;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class EmployeeCreateDTO {
    private String gmail;
    private String name;
    private String gender;
    private LocalDate createdAt;
}
