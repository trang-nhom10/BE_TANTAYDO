package com.example.da_tantaydo.service.impl;

import com.example.da_tantaydo.model.dto.request.DoctorScheduleRequestDTO;
import com.example.da_tantaydo.model.dto.response.AppointmentTodayDTO;
import com.example.da_tantaydo.model.dto.response.AvailableSlotDTO;
import com.example.da_tantaydo.model.dto.response.DoctorScheduleResponseDTO;
import com.example.da_tantaydo.model.dto.response.DoctorTodayScheduleDTO;
import com.example.da_tantaydo.model.entity.Doctor;
import com.example.da_tantaydo.model.entity.DoctorSchedule;
import com.example.da_tantaydo.model.entity.User;
import com.example.da_tantaydo.model.enums.AppointmentStatus;
import com.example.da_tantaydo.model.enums.ScheduleStatus;
import com.example.da_tantaydo.repository.AppointmentRepository;
import com.example.da_tantaydo.repository.DoctorRepository;
import com.example.da_tantaydo.repository.DoctorScheduleRepository;
import com.example.da_tantaydo.repository.UserRepository;
import com.example.da_tantaydo.service.DoctorScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorScheduleServiceImpl implements DoctorScheduleService {

    private final DoctorScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    @Override
    public void create(DoctorScheduleRequestDTO request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId()).orElseThrow(() -> new RuntimeException("Doctor not found."));
        DoctorSchedule schedule = DoctorSchedule.builder()
                .doctor(doctor)
                .workDate(request.getWorkDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .maxPatient(request.getMaxPatient() != null ? request.getMaxPatient() : 10)
                .status(ScheduleStatus.AVAILABLE)
                .build();
        scheduleRepository.save(schedule);
    }

    @Override
    public void update(Long id, DoctorScheduleRequestDTO request) {
        DoctorSchedule schedule = scheduleRepository.findById(id).orElseThrow(() -> new RuntimeException("Schedule not found."));
        Doctor doctor = doctorRepository.findById(request.getDoctorId()).orElseThrow(() -> new RuntimeException("Doctor not found."));

        schedule.setDoctor(doctor);
        schedule.setWorkDate(request.getWorkDate());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        if (request.getMaxPatient() != null) {
            schedule.setMaxPatient(request.getMaxPatient());
        }

        scheduleRepository.save(schedule);
    }

    @Override
    public void delete(Long id) {
        if (!scheduleRepository.existsById(id))
            throw new RuntimeException("Schedule not found.");
        scheduleRepository.deleteById(id);
    }

    @Override
    public DoctorScheduleResponseDTO getById(Long id) {
        return toDTO(scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found.")));
    }

    @Override
    public List<DoctorScheduleResponseDTO> getAll() {
        return scheduleRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<DoctorScheduleResponseDTO> getByDoctor(Long doctorId) {
        return scheduleRepository.findByDoctorId(doctorId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<DoctorScheduleResponseDTO> getByDate(LocalDate date) {
        return scheduleRepository.findByWorkDate(date)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<AvailableSlotDTO> getAvailableSlots(Long doctorId, LocalDate workDate) {

        DoctorSchedule schedule = scheduleRepository.findByDoctorIdAndWorkDateAndStatus(doctorId, workDate, ScheduleStatus.AVAILABLE).orElseThrow(() -> new RuntimeException("Bác sĩ không có lịch làm việc vào ngày này."));
        List<AvailableSlotDTO> availableSlots = new ArrayList<>();
        LocalTime current = schedule.getStartTime();
        LocalTime endTime = schedule.getEndTime();

        while (current.isBefore(endTime)) {
            if (current.getHour() != 12) {
                boolean isBooked = appointmentRepository
                        .isSlotBooked(
                                doctorId,
                                workDate,
                                current,
                                AppointmentStatus.CONFIRMED
                        );
                if (!isBooked) {
                    availableSlots.add(new AvailableSlotDTO(workDate, current));
                }
            }
            current = current.plusHours(1);
        }
        return availableSlots;
    }

    @Override
    public DoctorTodayScheduleDTO getTodaySchedule(Authentication authentication, LocalDate date) {
        String gmail = authentication.getName();

        User user = userRepository.findByGmail(gmail)
                .orElseThrow(() -> new RuntimeException("User not found."));

        Doctor doctor = doctorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found."));

        DoctorSchedule schedule = scheduleRepository.findByDoctorIdAndWorkDate(doctor.getId(), date)
                .orElseThrow(() -> new RuntimeException("Không có lịch làm việc ngày " + date));

        List<AppointmentTodayDTO> appointments = appointmentRepository
                .findByDoctorIdAndCreateAtOrderByTimeopenAsc(doctor.getId(), date)
                .stream()
                .map(a -> AppointmentTodayDTO.builder()
                        .id(a.getId())
                        .nameCustomer(a.getNameCustomer())
                        .year(a.getYear())
                        .createdAt(a.getCreateAt())
                        .timeOpen(a.getTimeopen())
                        .status(a.getStatus().name())
                        .build())
                .collect(Collectors.toList());

        return DoctorTodayScheduleDTO.builder()
                .scheduleId(schedule.getId())
                .workDate(schedule.getWorkDate())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .maxPatient(schedule.getMaxPatient())
                .scheduleStatus(schedule.getStatus().name())
                .appointments(appointments)
                .build();
    }



    private DoctorScheduleResponseDTO toDTO(DoctorSchedule s) {
        return DoctorScheduleResponseDTO.builder()
                .id(s.getId())
                .doctorId(s.getDoctor().getId())
                .doctorName(s.getDoctor().getName())
                .workDate(s.getWorkDate())
                .startTime(s.getStartTime())
                .endTime(s.getEndTime())
                .maxPatient(s.getMaxPatient())
                .status(s.getStatus())
                .build();
    }
}