package com.example.da_tantaydo.controller;

import com.example.da_tantaydo.model.dto.request.DoctorCreateDTO;
import com.example.da_tantaydo.model.dto.request.DoctorProfileRequestDTO;
import com.example.da_tantaydo.model.dto.response.AppointmentResponseDTO;
import com.example.da_tantaydo.model.dto.response.DoctorResponseDTO;
import com.example.da_tantaydo.model.dto.response.OrderResponseDTO;
import com.example.da_tantaydo.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    // ADMIN TẠO BÁC SĨ
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_DOCTOR')")
    public ResponseEntity<DoctorResponseDTO> create(
            @RequestBody DoctorCreateDTO request) {
        return ResponseEntity.ok(doctorService.create(request));
    }

    //  ADMIN XÓA BÁC SĨ
    @PostMapping("delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_DOCTOR')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        doctorService.delete(id);
        return ResponseEntity.ok("Xóa bác sĩ thành công");
    }

    // ADMIN LẤY TẤT CẢ
    @GetMapping("/getall")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_DOCTOR')")
    public ResponseEntity<Page<DoctorResponseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(doctorService.getAll(page, size));
    }

    // ADMIN TÌM KIẾM
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_DOCTOR')")
    public ResponseEntity<Page<DoctorResponseDTO>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(doctorService.search(keyword, page, size));
    }

    //  LẤY CHI TIẾT
    @GetMapping("details/{id}")
    public ResponseEntity<DoctorResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getById(id));
    }

    // BÁC SĨ TỰ UPDATE THÔNG TIN + ẢNH
    @PutMapping("/update/profile")
    @PreAuthorize("hasAuthority('DOCTOR_UPDATE_PROFILE')")
    public ResponseEntity<DoctorResponseDTO> updateProfile(
            @RequestPart("request") DoctorProfileRequestDTO request,
            @RequestPart(value = "img", required = false) MultipartFile img,
            Authentication authentication) {
        return ResponseEntity.ok(
                doctorService.updateProfile(authentication.getName(), request, img));
    }

    // BÁC SĨ XEM LỊCH KHÁCH ĐẶT CỦA MÌNH
    @GetMapping("/my/appointments")
    @PreAuthorize("hasAuthority('DOCTOR_VIEW_APPOINTMENT')")
    public ResponseEntity<Page<AppointmentResponseDTO>> getMyAppointments(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                doctorService.getMyAppointments(authentication.getName(), page, size));
    }

    //  BÁC SĨ XEM ĐƠN HÀNG KHÁCH CỦA MÌNH
    @GetMapping("/my/orders")
    @PreAuthorize("hasAuthority('DOCTOR_VIEW_ORDER')")
    public ResponseEntity<Page<OrderResponseDTO>> getMyOrders(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                doctorService.getMyOrders(authentication.getName(), page, size));
    }
}