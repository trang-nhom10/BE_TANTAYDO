package com.example.da_tantaydo.model.dto.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorCreateDTO {
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private String specialized;
    private String information;
    private String address;
    private String lever;
    private Long roleId;
}