package com.example.da_tantaydo.service;


import com.example.da_tantaydo.model.dto.request.DoctorScheduleRequestDTO;
import com.example.da_tantaydo.model.dto.response.AvailableSlotDTO;
import com.example.da_tantaydo.model.dto.response.DoctorScheduleResponseDTO;
import com.example.da_tantaydo.model.dto.response.DoctorTodayScheduleDTO;
import org.springframework.security.core.Authentication;
import java.time.LocalDate;
import java.util.List;

public interface DoctorScheduleService {
    void create(DoctorScheduleRequestDTO request);
    void update(Long id, DoctorScheduleRequestDTO request);
    void delete(Long id);
    DoctorScheduleResponseDTO getById(Long id);
    List<DoctorScheduleResponseDTO> getAll();
    List<DoctorScheduleResponseDTO> getByDoctor(Long doctorId);
    List<DoctorScheduleResponseDTO> getByDate(LocalDate date);
    List<AvailableSlotDTO> getAvailableSlots(Long doctorId, LocalDate workDate);
    DoctorTodayScheduleDTO getTodaySchedule(Authentication authentication);
}