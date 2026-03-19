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

    @Override
    public AppointmentResponseDTO create(AppointmentRequestDTO request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("The customer could not be found."));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("No doctor found."));

        DoctorSchedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new RuntimeException("No schedule found."));

        if (schedule.getStatus() != com.example.da_tantaydo.model.enums.ScheduleStatus.AVAILABLE)
            throw new RuntimeException("This appointment is fully booked.");

        if (appointmentRepository.existsByCustomerIdAndScheduleId(
                request.getCustomerId(), request.getScheduleId()))
            throw new RuntimeException("You have already booked this appointment.");

        int current = appointmentRepository.countActiveByScheduleId(request.getScheduleId());
        if (current >= schedule.getMaxPatient())
            throw new RuntimeException("The schedule is full.");

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

        scheduleService.checkAndUpdateStatus(request.getScheduleId());

        return toDTO(saved);
    }

    @Override
    public AppointmentResponseDTO updateStatus(Long id, AppointmentUpdateStatusDTO request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found."));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED)
            throw new RuntimeException("This appointment has been cancelled and cannot be updated.");

        appointment.setStatus(request.getStatus());
        if (request.getNote() != null)
            appointment.setNote(request.getNote());

        return toDTO(appointmentRepository.save(appointment));
    }

    @Override
    public void cancel(Long id, String gmail) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found."));

        if (!appointment.getCustomer().getUser().getGmail().equals(gmail))
            throw new RuntimeException("You do not have permission to cancel this appointment.");

        if (appointment.getStatus() != AppointmentStatus.PENDING)
            throw new RuntimeException("Only pending appointments can be cancelled.");

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    @Override
    public AppointmentResponseDTO getById(Long id) {
        return toDTO(appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found.")));
    }

    @Override
    public Page<AppointmentResponseDTO> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return appointmentRepository.findAll(pageable).map(this::toDTO);
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

    private AppointmentResponseDTO toDTO(Appointment a) {
        return AppointmentResponseDTO.builder()
                .id(a.getId())
                .customerId(a.getCustomer().getId())
                .customerName(a.getCustomer().getFullName())
                .customerPhone(a.getCustomer().getPhone())
                .doctorId(a.getDoctor().getId())
                .doctorName(a.getDoctor().getFullName())
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