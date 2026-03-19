package com.example.da_tantaydo.controller;


import com.example.da_tantaydo.model.dto.request.AppointmentRequestDTO;
import com.example.da_tantaydo.model.dto.request.AppointmentUpdateStatusDTO;
import com.example.da_tantaydo.model.dto.response.AppointmentResponseDTO;
import com.example.da_tantaydo.model.enums.AppointmentStatus;
import com.example.da_tantaydo.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    // ✅ KHÁCH HÀNG ĐẶT LỊCH
    @PostMapping
    @PreAuthorize("hasAuthority('CUSTOMER_BOOK_APPOINTMENT')")
    public ResponseEntity<AppointmentResponseDTO> create(
            @RequestBody AppointmentRequestDTO request) {
        return ResponseEntity.ok(appointmentService.create(request));
    }

    // ✅ ADMIN/NHÂN VIÊN CẬP NHẬT TRẠNG THÁI
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGE_APPOINTMENT','EMPLOYEE_MANAGE_APPOINTMENT')")
    public ResponseEntity<AppointmentResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody AppointmentUpdateStatusDTO request) {
        return ResponseEntity.ok(appointmentService.updateStatus(id, request));
    }

    // ✅ CUSTOMER HỦY LỊCH CỦA MÌNH
    @DeleteMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('CUSTOMER_BOOK_APPOINTMENT')")
    public ResponseEntity<String> cancel(
            @PathVariable Long id,
            Authentication authentication) {
        appointmentService.cancel(id, authentication.getName());
        return ResponseEntity.ok("Hủy lịch hẹn thành công");
    }

    // ✅ LẤY CHI TIẾT
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGE_APPOINTMENT','EMPLOYEE_MANAGE_APPOINTMENT')")
    public ResponseEntity<AppointmentResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getById(id));
    }

    // ✅ ADMIN/NHÂN VIÊN - LẤY TẤT CẢ PHÂN TRANG
    // GET /api/appointments?page=0&size=10
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGE_APPOINTMENT','EMPLOYEE_MANAGE_APPOINTMENT')")
    public ResponseEntity<Page<AppointmentResponseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(appointmentService.getAll(page, size));
    }

    // ✅ LỌC THEO TRẠNG THÁI
    // GET /api/appointments/status?status=PENDING&page=0&size=10
    @GetMapping("/status")
    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGE_APPOINTMENT','EMPLOYEE_MANAGE_APPOINTMENT')")
    public ResponseEntity<Page<AppointmentResponseDTO>> getByStatus(
            @RequestParam AppointmentStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(appointmentService.getByStatus(status, page, size));
    }

    // ✅ LẤY LỊCH THEO BÁC SĨ
    // GET /api/appointments/doctor/1?page=0&size=10
    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGE_APPOINTMENT','EMPLOYEE_MANAGE_APPOINTMENT')")
    public ResponseEntity<Page<AppointmentResponseDTO>> getByDoctor(
            @PathVariable Long doctorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(appointmentService.getByDoctor(doctorId, page, size));
    }

    // ✅ CUSTOMER XEM LỊCH CỦA MÌNH
    // GET /api/appointments/my?page=0&size=10
    @GetMapping("/my")
    @PreAuthorize("hasAuthority('CUSTOMER_BOOK_APPOINTMENT')")
    public ResponseEntity<Page<AppointmentResponseDTO>> getMyAppointments(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                appointmentService.getByGmail(authentication.getName(), page, size));
    }

    // ✅ TÌM KIẾM THEO TÊN/SĐT/DỊCH VỤ/BÁC SĨ
    // GET /api/appointments/search?keyword=nguyen&page=0&size=10
    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGE_APPOINTMENT','EMPLOYEE_MANAGE_APPOINTMENT')")
    public ResponseEntity<Page<AppointmentResponseDTO>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(appointmentService.search(keyword, page, size));
    }
}