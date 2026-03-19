package com.example.da_tantaydo.service;

import com.example.da_tantaydo.model.dto.request.AppointmentRequestDTO;
import com.example.da_tantaydo.model.dto.request.AppointmentUpdateStatusDTO;
import com.example.da_tantaydo.model.dto.response.AppointmentResponseDTO;
import com.example.da_tantaydo.model.enums.AppointmentStatus;
import org.springframework.data.domain.Page;

public interface AppointmentService {
    AppointmentResponseDTO create(AppointmentRequestDTO request);
    AppointmentResponseDTO updateStatus(Long id, AppointmentUpdateStatusDTO request);
    void cancel(Long id, String gmail); // CUSTOMER HỦY LỊCH CỦA MÌNH
    AppointmentResponseDTO getById(Long id);
    Page<AppointmentResponseDTO> getAll(int page, int size);
    Page<AppointmentResponseDTO> getByStatus(AppointmentStatus status, int page, int size);
    Page<AppointmentResponseDTO> getByDoctor(Long doctorId, int page, int size);
    Page<AppointmentResponseDTO> getByGmail(String gmail, int page, int size); // CUSTOMER XEM LỊCH
    Page<AppointmentResponseDTO> search(String keyword, int page, int size);
}