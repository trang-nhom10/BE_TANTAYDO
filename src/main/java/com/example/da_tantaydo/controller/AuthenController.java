package com.example.da_tantaydo.controller;

import com.example.da_tantaydo.model.dto.response.LoginReponseDTO;
import com.example.da_tantaydo.model.dto.response.ResponseDTO;
import com.example.da_tantaydo.model.dto.response.SuccessResponse;
import com.example.da_tantaydo.model.dto.request.LoginRequestDto;
import com.example.da_tantaydo.model.dto.request.RegisterRequestDTO;
import com.example.da_tantaydo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO request){
        userService.register(request);
        return ResponseEntity.ok( "register success");
    }
}
