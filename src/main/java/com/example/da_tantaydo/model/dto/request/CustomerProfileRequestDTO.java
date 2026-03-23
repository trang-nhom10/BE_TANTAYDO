package com.example.da_tantaydo.model.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileRequestDTO {
    private String fullName;
    private String phone;
    private String date;
    private String address;
}