package com.example.da_tantaydo.service;

import com.example.da_tantaydo.model.dto.response.ProfileResponseDTO;
import com.example.da_tantaydo.model.entity.User;
import com.example.da_tantaydo.model.dto.response.LoginReponseDTO;
import com.example.da_tantaydo.model.dto.request.LoginRequestDto;
import com.example.da_tantaydo.model.dto.request.RegisterRequestDTO;

public interface UserService {
    LoginReponseDTO login (LoginRequestDto request);
    User register (RegisterRequestDTO request);
    ProfileResponseDTO getProfile(String email);
}
