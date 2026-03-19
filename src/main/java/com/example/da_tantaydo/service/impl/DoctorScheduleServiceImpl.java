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
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

        // KIỂM TRA TRÙNG GIỜ
        boolean overlap = scheduleRepository.existsOverlap(
                request.getDoctorId(),
                request.getWorkDate(),
                request.getStartTime(),
                request.getEndTime(),
                0L // 0L vì là tạo mới, không có id để loại trừ
        );
        if (overlap) {
            throw new RuntimeException("Bác sĩ đã có ca khám trong khung giờ này");
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
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ca khám"));

        // KIỂM TRA TRÙNG GIỜ (loại trừ chính nó)
        boolean overlap = scheduleRepository.existsOverlap(
                request.getDoctorId(),
                request.getWorkDate(),
                request.getStartTime(),
                request.getEndTime(),
                id // loại trừ chính ca đang sửa
        );
        if (overlap) {
            throw new RuntimeException("Bác sĩ đã có ca khám trong khung giờ này");
        }

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

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
            throw new RuntimeException("Không tìm thấy ca khám");
        scheduleRepository.deleteById(id);
    }

    @Override
    public DoctorScheduleResponseDTO getById(Long id) {
        return toDTO(scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ca khám")));
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

    // ✅ GỌI HÀM NÀY SAU KHI KHÁCH ĐẶT LỊCH THÀNH CÔNG
    @Override
    public void checkAndUpdateStatus(Long scheduleId) {
        DoctorSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ca khám"));

        int current = scheduleRepository.countCurrentPatient(scheduleId);

        if (current >= schedule.getMaxPatient()) {
            schedule.setStatus(ScheduleStatus.FULL);
            scheduleRepository.save(schedule);
        }
    }

    private DoctorScheduleResponseDTO toDTO(DoctorSchedule s) {
        int current = scheduleRepository.countCurrentPatient(s.getId());
        return DoctorScheduleResponseDTO.builder()
                .id(s.getId())
                .doctorId(s.getDoctor().getId())
                .doctorName(s.getDoctor().getFullName())
                .workDate(s.getWorkDate())
                .startTime(s.getStartTime())
                .endTime(s.getEndTime())
                .maxPatient(s.getMaxPatient())
                .currentPatient(current)
                .status(s.getStatus())
                .build();
    }
}
