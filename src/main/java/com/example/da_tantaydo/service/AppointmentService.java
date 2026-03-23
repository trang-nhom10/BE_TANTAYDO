package com.example.da_tantaydo.service;

import com.example.da_tantaydo.model.dto.request.AppointmentRequestDTO;
import com.example.da_tantaydo.model.dto.request.AppointmentUpdateStatusDTO;
import com.example.da_tantaydo.model.dto.response.AppointmentResponseDTO;
import com.example.da_tantaydo.model.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AppointmentService {
    void create(AppointmentRequestDTO request, Authentication authentication);
    void updateStatus(Long id, AppointmentUpdateStatusDTO request);
    void cancel(Long id, AppointmentUpdateStatusDTO request);
    List<AppointmentResponseDTO> getAll();
    List<AppointmentResponseDTO> getByStatus(AppointmentStatus status);
    List<AppointmentResponseDTO> getByDoctor(Authentication authentication);
    List<AppointmentResponseDTO> getByGmail(String gmail);
    List<AppointmentResponseDTO> search(String keyword);
}