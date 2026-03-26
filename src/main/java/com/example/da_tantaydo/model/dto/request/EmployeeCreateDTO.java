package com.example.da_tantaydo.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class EmployeeCreateDTO {
    private String gmail;
    private String name;
    private String gender;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
}
