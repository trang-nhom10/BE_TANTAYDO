package com.example.da_tantaydo.controller;

import com.example.da_tantaydo.model.dto.response.ResponseDTO;
import com.example.da_tantaydo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.da_tantaydo.utils.Constants;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ResponseDTO> getProfile (Authentication authentication){
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("success")
                .code(Constants.HTTP_STATUS.SUCCESS)
                .message("get profile success")
                .data(userService.getProfile(authentication.getName())).build());

    }
}
