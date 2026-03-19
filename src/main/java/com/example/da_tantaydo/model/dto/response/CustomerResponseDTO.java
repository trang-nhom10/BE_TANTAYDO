package com.example.da_tantaydo.model.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponseDTO {
    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private String cccd;
    private LocalDateTime date;
    private String address;
    private String img;
}