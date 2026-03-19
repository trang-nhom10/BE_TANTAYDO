package com.example.da_tantaydo.controller;


import com.example.da_tantaydo.model.dto.request.DoctorScheduleRequestDTO;
import com.example.da_tantaydo.model.dto.response.DoctorScheduleResponseDTO;
import com.example.da_tantaydo.service.DoctorScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class DoctorScheduleController {

    private final DoctorScheduleService scheduleService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_SCHEDULE')")
    public ResponseEntity<DoctorScheduleResponseDTO> create(
            @RequestBody DoctorScheduleRequestDTO request) {
        return ResponseEntity.ok(scheduleService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_SCHEDULE')")
    public ResponseEntity<DoctorScheduleResponseDTO> update(
            @PathVariable Long id,
            @RequestBody DoctorScheduleRequestDTO request) {
        return ResponseEntity.ok(scheduleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_SCHEDULE')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        scheduleService.delete(id);
        return ResponseEntity.ok("Xóa ca khám thành công");
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorScheduleResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<DoctorScheduleResponseDTO>> getAll() {
        return ResponseEntity.ok(scheduleService.getAll());
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DoctorScheduleResponseDTO>> getByDoctor(
            @PathVariable Long doctorId) {
        return ResponseEntity.ok(scheduleService.getByDoctor(doctorId));
    }

    @GetMapping("/date")
    public ResponseEntity<List<DoctorScheduleResponseDTO>> getByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(scheduleService.getByDate(date));
    }
}