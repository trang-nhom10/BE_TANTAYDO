package com.example.da_tantaydo.controller;

import com.example.da_tantaydo.model.dto.request.AppointmentRequestDTO;
import com.example.da_tantaydo.model.dto.request.AppointmentUpdateStatusDTO;
import com.example.da_tantaydo.model.dto.response.AppointmentResponseDTO;
import com.example.da_tantaydo.model.dto.response.ResponseDTO;
import com.example.da_tantaydo.model.entity.AppointmentFile;
import com.example.da_tantaydo.model.enums.AppointmentStatus;
import com.example.da_tantaydo.repository.AppointmentFileRepository;
import com.example.da_tantaydo.service.AppointmentService;
import com.example.da_tantaydo.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final CustomerService customerService;
    private final AppointmentFileRepository appointmentFileRepository;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('CUSTOMER_MANAGE_APPOINTMENT')")
    public ResponseEntity<?> create(@RequestBody AppointmentRequestDTO request, Authentication authentication) {
        try {
            appointmentService.create(request, authentication);
            return ResponseEntity.ok("Appointment created successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred. Please try again.");
        }
    }

    @PostMapping("/update/status/{id}")
    @PreAuthorize("hasAuthority('DOCTOR_MANAGER_APPOINTMENT')")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestPart(value = "request") AppointmentUpdateStatusDTO request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        appointmentService.updateStatus(id, request, file);
        return ResponseEntity.ok("update success");

    }

    @PostMapping("/cancel/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER_MANAGE_APPOINTMENT')")
    public ResponseEntity<String> cancel(
            @PathVariable Long id,AppointmentUpdateStatusDTO request) {
        appointmentService.cancel(id,request);
        return ResponseEntity.ok("cancel success");
    }
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGE_APPOINTMENT','EMPLOYEE_MANAGE_ORDER')")
    public ResponseEntity<List<AppointmentResponseDTO>> getAll() {
        return ResponseEntity.ok(appointmentService.getAll());
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGE_APPOINTMENT','DOCTOR_MANAGER_APPOINTMENT','EMPLOYEE_MANAGE_APPOINTMENT')")
    public ResponseEntity<List<AppointmentResponseDTO>> getByStatus(
            @PathVariable AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.getByStatus(status));
    }

    @GetMapping("/my-appointments")
    @PreAuthorize("hasAuthority('DOCTOR_MANAGER_APPOINTMENT')")
    public ResponseEntity<ResponseDTO<List<AppointmentResponseDTO>>> getByDoctor(Authentication authentication) {
        List<AppointmentResponseDTO> result = appointmentService.getByDoctor(authentication);
        return ResponseEntity.ok(
                ResponseDTO.<List<AppointmentResponseDTO>>builder()
                        .status("success")
                        .code(200)
                        .message("Lấy danh sách lịch hẹn thành công")
                        .data(result)
                        .build()
        );
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('CUSTOMER_MANAGE_APPOINTMENT')")
    public ResponseEntity<List<AppointmentResponseDTO>> getMyAppointments(
            Authentication authentication) {
        return ResponseEntity.ok(customerService.getMyAppointments(authentication));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGE_APPOINTMENT','DOCTOR_MANAGER_APPOINTMENT','EMPLOYEE_MANAGE_APPOINTMENT')")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String nameCustomer,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createAt,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime timeopen) {
        try {
            return ResponseEntity.ok(appointmentService.search(nameCustomer, createAt, timeopen));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred. Please try again.");
        }
    }

    @GetMapping("/files")
    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGE_APPOINTMENT','EMPLOYEE_MANAGE_APPOINTMENT')")
    public ResponseEntity<?> getAllFiles() {
        List<AppointmentFile> files = appointmentFileRepository.findAll();
        return ResponseEntity.ok(files);
    }
}