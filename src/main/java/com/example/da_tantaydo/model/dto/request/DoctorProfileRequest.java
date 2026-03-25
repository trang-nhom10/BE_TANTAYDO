package com.example.da_tantaydo.model.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class DoctorProfileRequest {
    private String name;
    private String phone;
    private String information;
    private String address;
    private String lever;
}
