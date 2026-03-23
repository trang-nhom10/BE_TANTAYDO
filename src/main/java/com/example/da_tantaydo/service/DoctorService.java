package com.example.da_tantaydo.service;
import com.example.da_tantaydo.model.dto.request.DoctorCreateRequestDTO;
import com.example.da_tantaydo.model.dto.request.DoctorProfileRequestDTO;
import com.example.da_tantaydo.model.dto.response.AppointmentResponseDTO;
import com.example.da_tantaydo.model.dto.response.DoctorResponseDTO;
import com.example.da_tantaydo.model.dto.response.OrderResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DoctorService {
    // ADMIN
    void create(DoctorCreateRequestDTO request);
    void delete(Long id);
    List<DoctorResponseDTO> getAll();
    List<DoctorResponseDTO> search(String name, String specialized,String lever);
    void updateProfile(String gmail, DoctorProfileRequestDTO request, MultipartFile img);
    List<AppointmentResponseDTO> getMyAppointments(Authentication authentication);
}