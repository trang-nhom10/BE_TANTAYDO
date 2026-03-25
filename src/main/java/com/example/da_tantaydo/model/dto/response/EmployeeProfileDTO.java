package com.example.da_tantaydo.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class EmployeeProfileDTO {
    private String gmail;
    private String role;
    private List<String> permissions;
    private String fullName;
    private String phone;
    private String gender;
    private LocalDate  date;
    private String address;
    private LocalDate createAt;
    private String cccd;
    private String img;
}