package com.example.da_tantaydo.service;


import com.example.da_tantaydo.model.dto.request.DoctorScheduleRequestDTO;
import com.example.da_tantaydo.model.dto.response.DoctorScheduleResponseDTO;
import java.time.LocalDate;
import java.util.List;

public interface DoctorScheduleService {
    DoctorScheduleResponseDTO create(DoctorScheduleRequestDTO request);
    DoctorScheduleResponseDTO update(Long id, DoctorScheduleRequestDTO request);
    void delete(Long id);
    DoctorScheduleResponseDTO getById(Long id);
    List<DoctorScheduleResponseDTO> getAll();
    List<DoctorScheduleResponseDTO> getByDoctor(Long doctorId);
    List<DoctorScheduleResponseDTO> getByDate(LocalDate date);// GỌI SAU KHI ĐẶT LỊCH
}