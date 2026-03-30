package com.example.da_tantaydo.model.dto.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String gmail;
    private String otp;
    private String newPassword;
}
