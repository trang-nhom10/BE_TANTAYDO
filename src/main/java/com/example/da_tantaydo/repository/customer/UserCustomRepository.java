package com.example.da_tantaydo.repository.customer;

import com.example.da_tantaydo.model.dto.response.ProfileResponseDTO;

public interface UserCustomRepository {
    ProfileResponseDTO getProfileByEmail(String email);
}
