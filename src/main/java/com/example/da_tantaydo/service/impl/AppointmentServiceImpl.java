package com.example.da_tantaydo.service.impl;

import com.example.da_tantaydo.model.dto.request.AppointmentRequestDTO;
import com.example.da_tantaydo.model.dto.request.AppointmentUpdateStatusDTO;
import com.example.da_tantaydo.model.dto.response.AppointmentResponseDTO;
import com.example.da_tantaydo.model.entity.*;
import com.example.da_tantaydo.model.enums.AppointmentStatus;
import com.example.da_tantaydo.repository.*;
import com.example.da_tantaydo.service.AppointmentService;
import com.example.da_tantaydo.service.DoctorScheduleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorScheduleRepository scheduleRepository;
    private final DoctorScheduleService scheduleService;
    private final UserRepository userRepository;

    @Override
    public void create(AppointmentRequestDTO request, Authentication authentication) {
        String gmail = authentication.getName();
        User user = userRepository.findByGmail(gmail).orElseThrow(() -> new RuntimeException("User not found."));
        Customer customer = customerRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Customer not found."));
        Doctor doctor = doctorRepository.findById(request.getDoctorId()).orElseThrow(() -> new RuntimeException("No doctor found."));
        Appointment appointment = new Appointment();
        appointment.setNameCustomer(request.getName());
        appointment.setYear(request.getDate());
        appointment.setPhone(request.getPhone());
        appointment.setGmail(request.getGmail());
        appointment.setAddress(request.getAddress());
        appointment.setCreateAt(LocalDateTime.now());
        appointment.setDoctor(doctor);
        appointment.setCustomer(customer);
        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setTimeopen(request.getTimeOpen());
        appointment.setNote(request.getNote());
        appointmentRepository.save(appointment);
    }

    @Override
    public void updateStatus(Long id, AppointmentUpdateStatusDTO request) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Appointment not found."));
        if (appointment.getStatus() == AppointmentStatus.CANCELLED)
            throw new RuntimeException("This appointment has been cancelled and cannot be updated.");
        appointment.setStatus(request.getStatus());
        appointmentRepository.save(appointment);
    }

    @Override
    public void cancel(Long id, AppointmentUpdateStatusDTO request) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Appointment not found."));
        if (appointment.getStatus() != AppointmentStatus.PENDING)
            throw new RuntimeException("Only pending appointments can be cancelled.");
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    @Override
    public List<AppointmentResponseDTO> getAll() {
        return appointmentRepository.findAll()
                .stream()
                .map(a -> AppointmentResponseDTO.builder()
                        .id(a.getId())
                        .name(a.getNameCustomer())
                        .date(a.getYear())
                        .phone(a.getPhone())
                        .gmail(a.getGmail())
                        .address(a.getAddress())
                        .createdAt(a.getCreateAt() != null ? a.getCreateAt().toString() : null)
                        .doctorName(a.getDoctor() != null ? a.getDoctor().getName() : null)
                        .timeOpen(a.getTimeopen())
                        .note(a.getNote())
                        .status(a.getStatus() != null ? a.getStatus().name() : null)
                        .build())
                .toList();
    }
    @Override
    public List<AppointmentResponseDTO> getByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatusOrderByCreateAtDesc(status)
                .stream().map(this::toDTO).toList();
    }

    @Override
    public List<AppointmentResponseDTO> getByDoctor(Authentication authentication) {
        Doctor doctor = doctorRepository.findByUserGmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Doctor not found."));

        return appointmentRepository.findByDoctorIdOrderByCreateAtDesc(doctor.getId())
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<AppointmentResponseDTO> getByGmail(String gmail) {
        return appointmentRepository.findByCustomerUserGmailOrderByCreateAtDesc(gmail)
                .stream().map(this::toDTO).toList();
    }

    @Override
    public List<AppointmentResponseDTO> search(String keyword) {
        return appointmentRepository.search(keyword)
                .stream().map(this::toDTO).toList();
    }
    private AppointmentResponseDTO toDTO(Appointment a) {
        return AppointmentResponseDTO.builder()
                .id(a.getId())
                .name(a.getNameCustomer())
                .date(a.getYear())
                .phone(a.getPhone())
                .gmail(a.getGmail())
                .address(a.getAddress())
                .createdAt(a.getCreateAt() != null ? a.getCreateAt().toString() : null)
                .doctorName(a.getDoctor() != null ? a.getDoctor().getName() : null) // ✅
                .timeOpen(a.getTimeopen())
                .note(a.getNote())
                .status(a.getStatus() != null ? a.getStatus().name() : null)
                .build();
    }

}