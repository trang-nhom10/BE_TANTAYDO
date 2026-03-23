package com.example.da_tantaydo.controller;

import com.example.da_tantaydo.model.dto.request.DoctorCreateRequestDTO;
import com.example.da_tantaydo.model.dto.request.DoctorProfileRequestDTO;
import com.example.da_tantaydo.model.dto.response.AppointmentResponseDTO;
import com.example.da_tantaydo.model.dto.response.DoctorResponseDTO;
import com.example.da_tantaydo.model.dto.response.ResponseDTO;
import com.example.da_tantaydo.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping("/create")
//    @PreAuthorize("hasAuthority('ADMIN_MANAGE_DOCTOR')")
    public ResponseEntity<?> create(@RequestBody DoctorCreateRequestDTO request) {
       doctorService.create(request);
       return ResponseEntity.ok("create success");
    }

    @PostMapping("/delete/{id}")
//    @PreAuthorize("hasAuthority('ADMIN_MANAGE_DOCTOR')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        doctorService.delete(id);
        return ResponseEntity.ok("delete successs");
    }

    @GetMapping("/getall")
//    @PreAuthorize("hasAuthority('ADMIN_MANAGE_DOCTOR')")
    public ResponseEntity<?> getAll( ){
        return  ResponseEntity.ok(Map.of(
                "message", "Get all success",
                "data", doctorService.getAll()
                ));
    }
    @GetMapping("/search")
//    @PreAuthorize("hasAuthority('ADMIN_MANAGE_DOCTOR')")
    public ResponseEntity<ResponseDTO<List<DoctorResponseDTO>>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String specialized,
            @RequestParam(required = false) String lever) {

        List<DoctorResponseDTO> result = doctorService.search(name, specialized, lever);
        return ResponseEntity.ok(
                ResponseDTO.<List<DoctorResponseDTO>>builder()
                        .status("success")
                        .code(200)
                        .message("Search success")
                        .data(result)
                        .build()
        );
    }

    @PutMapping("/update/profile")
//    @PreAuthorize("hasAuthority('DOCTOR_UPDATE_PROFILE')")//chưa test
    public ResponseEntity<?> updateProfile(
            @RequestPart("request") DoctorProfileRequestDTO request,
            @RequestPart(value = "img", required = false) MultipartFile img,
            Authentication authentication) {
                doctorService.updateProfile(authentication.getName(), request, img);
              return   ResponseEntity.ok("update success");
    }

    // BÁC SĨ XEM LỊCH KHÁCH ĐẶT CỦA MÌNH// chưa test
    @GetMapping("/my-appointments")
    public ResponseEntity<ResponseDTO<List<AppointmentResponseDTO>>> getMyAppointments(Authentication authentication) {
        List<AppointmentResponseDTO> result = doctorService.getMyAppointments(authentication);
        return ResponseEntity.ok(
                ResponseDTO.<List<AppointmentResponseDTO>>builder()
                        .status("success")
                        .code(200)
                        .message("Successfully retrieved the appointment list.")
                        .data(result)
                        .build()
        );
    }
}