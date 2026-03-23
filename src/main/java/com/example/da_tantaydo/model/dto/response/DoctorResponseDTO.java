package com.example.da_tantaydo.model.dto.response;


import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorResponseDTO {
    private Long id;
    private String email;
    private String name;
    private String phone;
    private String specialized;
    private String information;
    private String address;
    private String img;
    private String lever;
    private LocalDateTime createdAt;
}