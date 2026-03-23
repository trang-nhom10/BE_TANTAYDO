package com.example.da_tantaydo.service.impl;

import com.example.da_tantaydo.model.dto.request.AppointmentRequestDTO;
import com.example.da_tantaydo.model.dto.request.AppointmentUpdateStatusDTO;
import com.example.da_tantaydo.model.dto.response.AppointmentResponseDTO;
import com.example.da_tantaydo.model.entity.*;
import com.example.da_tantaydo.model.enums.AppointmentStatus;
import com.example.da_tantaydo.model.enums.ScheduleStatus;
import com.example.da_tantaydo.repository.*;
import com.example.da_tantaydo.service.AppointmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Override
    public void create(AppointmentRequestDTO request, Authentication authentication) {
        String gmail = authentication.getName();
        User user = userRepository.findByGmail(gmail).orElseThrow(() -> new RuntimeException("User not found."));
        Customer customer = customerRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Customer not found."));
        Doctor doctor = doctorRepository.findById(request.getDoctorId()).orElseThrow(() -> new RuntimeException("No doctor found."));

        LocalTime timeOpen = request.getTimeOpen();
        if (timeOpen.getMinute() != 0 || timeOpen.getSecond() != 0) {
            throw new RuntimeException("The reservation time must be exactly 08:00.");
        }
        if (timeOpen.getHour() == 12) {
            throw new RuntimeException("The 12:00 - 13:00 time slot is a long break; please choose a different time.");
        }
        DoctorSchedule schedule = scheduleRepository
                .findByDoctorIdAndWorkDateAndStatus(request.getDoctorId(), request.getCreatedAt(), ScheduleStatus.AVAILABLE)
                .orElseThrow(() -> new RuntimeException("The doctor doesn't have any appointments on this day."));

        if (timeOpen.isBefore(schedule.getStartTime()) || !timeOpen.isBefore(schedule.getEndTime())) {
            throw new RuntimeException("The appointment time must be within the doctor's working hours. (" + schedule.getStartTime() + " - " + schedule.getEndTime() + ").");
        }
        boolean isBooked = appointmentRepository.isSlotBooked(
                request.getDoctorId(),
                request.getCreatedAt(),
                timeOpen,
                AppointmentStatus.CONFIRMED
        );
        if (isBooked) {
            throw new RuntimeException("time " + timeOpen + " The reservation has already been taken, please choose a different time.");
        }

        Appointment appointment = new Appointment();
        appointment.setNameCustomer(request.getName());
        appointment.setYear(request.getDate());
        appointment.setPhone(request.getPhone());
        appointment.setGmail(request.getGmail());
        appointment.setAddress(request.getAddress());
        appointment.setCreateAt(request.getCreatedAt());
        appointment.setDoctor(doctor);
        appointment.setCustomer(customer);
        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setTimeopen(timeOpen);
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

    public List<AppointmentResponseDTO> search(String nameCustomer, LocalDate createAt, LocalTime timeopen) {
        return appointmentRepository.search(nameCustomer, createAt, timeopen)
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
                        .build()
                )
                .collect(Collectors.toList());
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