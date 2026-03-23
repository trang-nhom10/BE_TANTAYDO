package com.example.da_tantaydo.controller;

import com.example.da_tantaydo.model.dto.request.AppointmentRequestDTO;
import com.example.da_tantaydo.model.dto.request.AppointmentUpdateStatusDTO;
import com.example.da_tantaydo.model.dto.response.AppointmentResponseDTO;
import com.example.da_tantaydo.model.dto.response.ResponseDTO;
import com.example.da_tantaydo.model.enums.AppointmentStatus;
import com.example.da_tantaydo.service.AppointmentService;
import com.example.da_tantaydo.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final CustomerService customerService;
//KHÁCH HÀNG ĐẶT
    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestBody AppointmentRequestDTO request,
            Authentication authentication) {
        appointmentService.create(request, authentication);
        return ResponseEntity.ok("Create success");
    }
//DÀNH CHO BÁC SĨ
    @PostMapping("/update/status/{id}")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestBody AppointmentUpdateStatusDTO request) {
        appointmentService.updateStatus(id, request);
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

    @GetMapping("/search")
    public ResponseEntity<List<AppointmentResponseDTO>> search(
            @RequestParam String keyword) {
        return ResponseEntity.ok(appointmentService.search(keyword));
    }
}