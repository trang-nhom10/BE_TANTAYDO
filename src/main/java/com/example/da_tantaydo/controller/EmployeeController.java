package com.example.da_tantaydo.controller;

import com.example.da_tantaydo.model.dto.request.*;
import com.example.da_tantaydo.model.dto.response.EmployeeResponseDTO;
import com.example.da_tantaydo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // ✅ ADMIN - TẠO NHÂN VIÊN
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_EMPLOYEE')")
    public ResponseEntity<EmployeeResponseDTO> create(
            @RequestBody EmployeeCreateDTO request) {
        return ResponseEntity.ok(employeeService.create(request));
    }

    // ✅ ADMIN - CẬP NHẬT QUYỀN
    @PutMapping("/{id}/role")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_EMPLOYEE')")
    public ResponseEntity<EmployeeResponseDTO> updateRole(
            @PathVariable Long id,
            @RequestBody EmployeeUpdateRoleDTO request) {
        return ResponseEntity.ok(employeeService.updateRole(id, request));
    }

    // ✅ ADMIN - XÓA NHÂN VIÊN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_EMPLOYEE')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.ok("Xóa nhân viên thành công");
    }

    // ✅ ADMIN - LẤY TẤT CẢ NHÂN VIÊN
    // GET /api/employees?page=0&size=10
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_EMPLOYEE')")
    public ResponseEntity<Page<EmployeeResponseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(employeeService.getAll(page, size));
    }

    // ✅ ADMIN - TÌM KIẾM
    // GET /api/employees/search?keyword=nguyen&page=0&size=10
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_EMPLOYEE')")
    public ResponseEntity<Page<EmployeeResponseDTO>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(employeeService.search(keyword, page, size));
    }

    // ✅ ADMIN - LẤY CHI TIẾT
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_EMPLOYEE')")
    public ResponseEntity<EmployeeResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getById(id));
    }

    // ✅ NHÂN VIÊN - TỰ UPDATE THÔNG TIN CỦA MÌNH
    @PutMapping("/profile")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE_UPDATE_PROFILE')")
    public ResponseEntity<EmployeeResponseDTO> updateProfile(
            @RequestPart("request") EmployeeRequestDTO request,
            @RequestPart(value = "img", required = false) MultipartFile img,
            Authentication authentication) {
        String gmail = authentication.getName();
        return ResponseEntity.ok(employeeService.updateProfile(gmail, request, img));
    }

    // ✅ NHÂN VIÊN - XEM THÔNG TIN CỦA MÌNH
    @GetMapping("/profile")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE_UPDATE_PROFILE')")
    public ResponseEntity<EmployeeResponseDTO> getProfile(Authentication authentication) {
        String gmail = authentication.getName();
        return ResponseEntity.ok(employeeService.updateProfile(gmail, null, null));
    }
}