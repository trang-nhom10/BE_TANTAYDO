package com.example.da_tantaydo.model.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
public class EmployeeProfileRequest {
    private String fullName;
    private String phone;
    private String gender;
    private LocalDate date;
    private String address;
    private String cccd;
    private String pass;
}
