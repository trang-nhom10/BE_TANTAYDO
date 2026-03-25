package com.example.da_tantaydo.controller;

import com.example.da_tantaydo.model.dto.request.*;
import com.example.da_tantaydo.model.dto.response.EmployeeResponseDTO;
import com.example.da_tantaydo.model.dto.response.ResponseDTO;
import com.example.da_tantaydo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_EMPLOYEE')")
    public ResponseEntity<?> create(@RequestBody EmployeeCreateDTO request) {
        employeeService.create(request);
        return ResponseEntity.ok("create success");
    }

    @PostMapping("/role/{id}")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_EMPLOYEE')")
    public ResponseEntity<?> updateRole(@PathVariable Long id, @RequestBody EmployeeUpdateRoleDTO request) {
        employeeService.updateRole(id, request);
         return ResponseEntity.ok("update success");
    }

    @PostMapping("/upload/profile")
    @PreAuthorize("hasAuthority('EMPLOYEE_UPDATE_PROFILE')")
    public ResponseEntity<?> uploadProfile(
            @RequestPart(value = "request") EmployeeProfileRequest request,
            @RequestPart(value = "img", required = false) MultipartFile img,
            Authentication authentication) {
        String gmail = authentication.getName();
        employeeService.UpdateProFile(gmail, request, img);
        return ResponseEntity.ok("Profile updated successfully");
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_EMPLOYEE')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.ok("delete success");
    }

    @GetMapping("/getall")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_EMPLOYEE')")
    public ResponseEntity<ResponseDTO<List<EmployeeResponseDTO>>> getAll() {
        List<EmployeeResponseDTO> result = employeeService.getAll();
        return ResponseEntity.ok(
                ResponseDTO.<List<EmployeeResponseDTO>>builder()
                        .status("success")
                        .code(200)
                        .message("Successfully retrieved the employee list.")
                        .data(result)
                        .build()
        );
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_EMPLOYEE')")
    public ResponseEntity<ResponseDTO<List<EmployeeResponseDTO>>> search(@RequestParam(required = false) String fullName,
            @RequestParam(required = false) String address) {
        List<EmployeeResponseDTO> result = employeeService.search(fullName, address);
        return ResponseEntity.ok(
                ResponseDTO.<List<EmployeeResponseDTO>>builder()
                        .status("success")
                        .code(200)
                        .message("search success")
                        .data(result)
                        .build()
        );
    }
}