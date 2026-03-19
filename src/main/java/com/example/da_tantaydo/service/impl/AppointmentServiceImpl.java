package com.example.da_tantaydo.service.impl;


import com.example.da_tantaydo.model.dto.request.AppointmentRequestDTO;
import com.example.da_tantaydo.model.dto.request.AppointmentUpdateStatusDTO;
import com.example.da_tantaydo.model.dto.response.AppointmentResponseDTO;
import com.example.da_tantaydo.model.entity.*;
import com.example.da_tantaydo.model.enums.AppointmentStatus;
import com.example.da_tantaydo.repository.*;
import com.example.da_tantaydo.service.AppointmentService;
import com.example.da_tantaydo.service.DoctorScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorScheduleRepository scheduleRepository;
    private final DoctorScheduleService scheduleService;

    // ============================================
    // KHÁCH HÀNG ĐẶT LỊCH
    // ============================================
    @Override
    public AppointmentResponseDTO create(AppointmentRequestDTO request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

        DoctorSchedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ca khám"));

        // KIỂM TRA CA CÒN AVAILABLE KHÔNG
        if (schedule.getStatus() != com.example.da_tantaydo.model.enums.ScheduleStatus.AVAILABLE)
            throw new RuntimeException("Ca khám này không còn chỗ trống");

        // KIỂM TRA KHÁCH ĐÃ ĐẶT CA NÀY CHƯA
        if (appointmentRepository.existsByCustomerIdAndScheduleId(
                request.getCustomerId(), request.getScheduleId()))
            throw new RuntimeException("Bạn đã đặt ca khám này rồi");

        // KIỂM TRA CA CÒN CHỖ KHÔNG
        int current = appointmentRepository.countActiveByScheduleId(request.getScheduleId());
        if (current >= schedule.getMaxPatient())
            throw new RuntimeException("Ca khám đã đầy");

        Appointment appointment = Appointment.builder()
                .customer(customer)
                .doctor(doctor)
                .schedule(schedule)
                .service(request.getService())
                .reason(request.getReason())
                .note(request.getNote())
                .status(AppointmentStatus.PENDING)
                .build();

        Appointment saved = appointmentRepository.save(appointment);

        // TỰ ĐỘNG CẬP NHẬT TRẠNG THÁI CA KHÁM NẾU ĐẦY
        scheduleService.checkAndUpdateStatus(request.getScheduleId());

        return toDTO(saved);
    }

    // ============================================
    // ADMIN/NHÂN VIÊN CẬP NHẬT TRẠNG THÁI
    // ============================================
    @Override
    public AppointmentResponseDTO updateStatus(Long id, AppointmentUpdateStatusDTO request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn"));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED)
            throw new RuntimeException("Lịch hẹn đã bị hủy, không thể cập nhật");

        appointment.setStatus(request.getStatus());
        if (request.getNote() != null)
            appointment.setNote(request.getNote());

        return toDTO(appointmentRepository.save(appointment));
    }

    // ============================================
    // CUSTOMER HỦY LỊCH CỦA MÌNH
    // ============================================
    @Override
    public void cancel(Long id, String gmail) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn"));

        // KIỂM TRA LỊCH NÀY CÓ PHẢI CỦA CUSTOMER NÀY KHÔNG
        if (!appointment.getCustomer().getUser().getGmail().equals(gmail))
            throw new RuntimeException("Bạn không có quyền hủy lịch này");

        // CHỈ HỦY ĐƯỢC KHI ĐANG PENDING
        if (appointment.getStatus() != AppointmentStatus.PENDING)
            throw new RuntimeException("Chỉ có thể hủy lịch đang chờ xác nhận");

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    @Override
    public AppointmentResponseDTO getById(Long id) {
        return toDTO(appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn")));
    }

    @Override
    public Page<AppointmentResponseDTO> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return appointmentRepository.findAll(
                pageable).map(this::toDTO);
    }

    @Override
    public Page<AppointmentResponseDTO> getByStatus(
            AppointmentStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return appointmentRepository
                .findByStatusOrderByCreatedAtDesc(status, pageable)
                .map(this::toDTO);
    }

    @Override
    public Page<AppointmentResponseDTO> getByDoctor(Long doctorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return appointmentRepository
                .findByDoctorIdOrderByCreatedAtDesc(doctorId, pageable)
                .map(this::toDTO);
    }

    // CUSTOMER XEM LỊCH CỦA MÌNH QUA GMAIL
    @Override
    public Page<AppointmentResponseDTO> getByGmail(String gmail, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return appointmentRepository
                .findByCustomerUserGmailOrderByCreatedAtDesc(gmail, pageable)
                .map(this::toDTO);
    }

    @Override
    public Page<AppointmentResponseDTO> search(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return appointmentRepository.search(keyword, pageable).map(this::toDTO);
    }

    // ============================================
    // CONVERT ENTITY -> DTO
    // ============================================
    private AppointmentResponseDTO toDTO(Appointment a) {
        return AppointmentResponseDTO.builder()
                .id(a.getId())
                .customerId(a.getCustomer().getId())
                .customerName(a.getCustomer().getFullName())
                .customerPhone(a.getCustomer().getPhone())
                .doctorId(a.getDoctor().getId())
                .doctorName(a.getDoctor().getName())
                .scheduleId(a.getSchedule().getId())
                .workDate(a.getSchedule().getWorkDate())
                .startTime(a.getSchedule().getStartTime())
                .endTime(a.getSchedule().getEndTime())
                .service(a.getService())
                .reason(a.getReason())
                .note(a.getNote())
                .status(a.getStatus())
                .createdAt(a.getCreatedAt())
                .build();
    }
}