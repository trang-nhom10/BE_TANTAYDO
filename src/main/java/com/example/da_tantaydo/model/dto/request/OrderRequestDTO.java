package com.example.da_tantaydo.model.dto.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    private Long customerId;
    private Long doctorId;
    private String service;
    private String note;
}