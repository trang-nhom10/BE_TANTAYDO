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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final CustomerService customerService;
    private final AppointmentFileRepository appointmentFileRepository;
//KHÁCH HÀNG ĐẶT
    @PostMapping("/create")
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
//DÀNH CHO BÁC SĨ
    @PostMapping("/update/status/{id}")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestPart(value = "request") AppointmentUpdateStatusDTO request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        appointmentService.updateStatus(id, request, file);
        return ResponseEntity.ok("update success");

    }
//khách hủy đơn
    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancel(
            @PathVariable Long id,AppointmentUpdateStatusDTO request) {
        appointmentService.cancel(id,request);
        return ResponseEntity.ok("cancel success");
    }
//admin xem full lịch đặt
    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>> getAll() {
        return ResponseEntity.ok(appointmentService.getAll());
    }

    //tìm theo trạng thái
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AppointmentResponseDTO>> getByStatus(
            @PathVariable AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.getByStatus(status));
    }
// xem đơn của bác sĩ
    @GetMapping("/my-appointments")
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
// danh sách  khách hàng đặt đơn mình

    @GetMapping("/my")
    public ResponseEntity<List<AppointmentResponseDTO>> getMyAppointments(
            Authentication authentication) {
        return ResponseEntity.ok(customerService.getMyAppointments(authentication));
    }

    //nhân viên tìm kiếm
    @GetMapping("/search")
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
    public ResponseEntity<?> getAllFiles() {
        List<AppointmentFile> files = appointmentFileRepository.findAll();
        return ResponseEntity.ok(files);
    }
}