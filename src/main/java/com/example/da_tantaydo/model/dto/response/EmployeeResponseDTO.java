package com.example.da_tantaydo.model.dto.response;


import com.example.da_tantaydo.model.enums.Gender;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponseDTO {
    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private Gender gender;
    private LocalDate date;
    private String address;
    private String cccd;
    private String img;
    private String roleName;
    private LocalDateTime createdAt;
}