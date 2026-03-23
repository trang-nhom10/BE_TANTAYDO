package com.example.da_tantaydo.service.impl;


import com.example.da_tantaydo.model.dto.request.DoctorScheduleRequestDTO;
import com.example.da_tantaydo.model.dto.response.DoctorScheduleResponseDTO;
import com.example.da_tantaydo.model.entity.Doctor;
import com.example.da_tantaydo.model.entity.DoctorSchedule;
import com.example.da_tantaydo.model.enums.ScheduleStatus;
import com.example.da_tantaydo.repository.DoctorRepository;
import com.example.da_tantaydo.repository.DoctorScheduleRepository;
import com.example.da_tantaydo.service.DoctorScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorScheduleServiceImpl implements DoctorScheduleService {

    private final DoctorScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public DoctorScheduleResponseDTO create(DoctorScheduleRequestDTO request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found."));

        boolean overlap = scheduleRepository.existsOverlap(
                request.getDoctorId(),
                request.getWorkDate(),
                request.getStartTime(),
                request.getEndTime(),
                0L
        );
        if (overlap) {
            throw new RuntimeException("The doctor already has a schedule in this time slot.");
        }

        DoctorSchedule schedule = DoctorSchedule.builder()
                .doctor(doctor)
                .workDate(request.getWorkDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .maxPatient(request.getMaxPatient() != null ? request.getMaxPatient() : 10)
                .status(ScheduleStatus.AVAILABLE)
                .build();

        return toDTO(scheduleRepository.save(schedule));
    }

    @Override
    public DoctorScheduleResponseDTO update(Long id, DoctorScheduleRequestDTO request) {
        DoctorSchedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found."));

        boolean overlap = scheduleRepository.existsOverlap(
                request.getDoctorId(),
                request.getWorkDate(),
                request.getStartTime(),
                request.getEndTime(),
                id
        );
        if (overlap) {
            throw new RuntimeException("The doctor already has a schedule in this time slot.");
        }

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found."));

        schedule.setDoctor(doctor);
        schedule.setWorkDate(request.getWorkDate());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        if (request.getMaxPatient() != null) {
            schedule.setMaxPatient(request.getMaxPatient());
        }

        return toDTO(scheduleRepository.save(schedule));
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