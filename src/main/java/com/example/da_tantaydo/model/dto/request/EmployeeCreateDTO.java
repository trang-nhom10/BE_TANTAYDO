package com.example.da_tantaydo.model.dto.request;


import com.example.da_tantaydo.model.enums.Gender;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreateDTO {
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private Gender gender;
    private LocalDate date;
    private String address;
    private String cccd;
    private Long roleId;
}
