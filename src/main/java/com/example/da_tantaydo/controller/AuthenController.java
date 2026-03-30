package com.example.da_tantaydo.controller;

import com.example.da_tantaydo.model.dto.request.ResetPasswordRequest;
import com.example.da_tantaydo.model.dto.response.LoginReponseDTO;
import com.example.da_tantaydo.model.dto.response.SuccessResponse;
import com.example.da_tantaydo.model.dto.request.LoginRequestDto;
import com.example.da_tantaydo.model.dto.request.RegisterRequestDTO;
import com.example.da_tantaydo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenController {
    private final UserService userService;

    @PostMapping("/login")
    public SuccessResponse<LoginReponseDTO> login(@RequestBody LoginRequestDto request) {
        LoginReponseDTO user = userService.login(request);
        return new SuccessResponse<>(
                200,
                "Login success",
                user
        );
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO request) {
        if (request.getOtp() == null || request.getOtp().isBlank()) {
            return ResponseEntity.badRequest().body("Vui lòng nhập mã OTP xác thực");
        }
        try {
            userService.register(request);
            return ResponseEntity.ok("Đăng ký thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/forgot-password/reset")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        if (request.getOtp() == null || request.getOtp().isBlank()) {
            return ResponseEntity.badRequest().body("Vui lòng nhập mã OTP xác thực");
        }
        try {
            userService.forgotPassword(request.getGmail(), request.getOtp(), request.getNewPassword());
            return ResponseEntity.ok("Đổi mật khẩu thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
