package com.example.da_tantaydo.controller;

import com.example.da_tantaydo.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;

    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestParam String gmail) {
        otpService.sendOtp(gmail);
        return ResponseEntity.ok("OTP đã được gửi đến " + gmail);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestParam String gmail,
                                            @RequestParam String otp) {
        boolean isValid = otpService.verifyOtp(gmail, otp);
        if (isValid) return ResponseEntity.ok("Xác thực thành công!");
        return ResponseEntity.badRequest().body("OTP không hợp lệ hoặc đã hết hạn.");
    }
}