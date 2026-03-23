package com.example.da_tantaydo.controller;

import com.example.da_tantaydo.model.dto.request.AvailableSlotRequestDTO;
import com.example.da_tantaydo.model.dto.request.DoctorScheduleRequestDTO;
import com.example.da_tantaydo.model.dto.response.AvailableSlotDTO;
import com.example.da_tantaydo.model.dto.response.DoctorScheduleResponseDTO;
import com.example.da_tantaydo.service.DoctorScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class DoctorScheduleController {

    private final DoctorScheduleService scheduleService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DoctorScheduleRequestDTO request) {
        scheduleService.create(request);
        return ResponseEntity.ok("create success");

    }

    @PostMapping("update/{id}")
//    @PreAuthorize("hasAuthority('ADMIN_MANAGE_SCHEDULE')")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody DoctorScheduleRequestDTO request) {
        scheduleService.update(id, request);
        return ResponseEntity.ok("update success");
    }

    @PostMapping("delete/{id}")
//    @PreAuthorize("hasAuthority('ADMIN_MANAGE_SCHEDULE')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        scheduleService.delete(id);
        return ResponseEntity.ok("delete success");
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorScheduleResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.getById(id));
    }

    @GetMapping("/getall")
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
//khách hàng xem lịch null
    @PostMapping("/available-slots")
    public ResponseEntity<List<AvailableSlotDTO>> getAvailableSlots(
            @RequestBody AvailableSlotRequestDTO request) {
        return ResponseEntity.ok(scheduleService.getAvailableSlots(request.getDoctorId(), request.getWorkDate()));
    }
// xem lịch bác sĩ
    @GetMapping("/today")
    public ResponseEntity<?> getTodaySchedule(Authentication authentication) {
        try {
            return ResponseEntity.ok(scheduleService.getTodaySchedule(authentication));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred. Please try again.");
        }
    }
}