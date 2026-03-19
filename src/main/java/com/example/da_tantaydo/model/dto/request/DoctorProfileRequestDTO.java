package com.example.da_tantaydo.model.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorProfileRequestDTO {
    private String fullName;
    private String phone;
    private String specialized;
    private String information;
    private String address;
    private String lever;
}