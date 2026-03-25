package com.example.da_tantaydo.model.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DoctorProfileDTO {
    private String gmail;
    private String role;
    private List<String> permissions;
    private String name;
    private String phone;
    private String specialized;
    private String information;
    private LocalDate createdAt ;
    private String address;
    private String img;
    private String lever;

}
