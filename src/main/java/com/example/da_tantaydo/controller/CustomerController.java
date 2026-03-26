package com.example.da_tantaydo.controller;

import com.example.da_tantaydo.model.dto.request.CustomerProfileRequestDTO;
import com.example.da_tantaydo.model.dto.response.CustomerResponseDTO;
import com.example.da_tantaydo.model.dto.response.OrderResponseDTO;
import com.example.da_tantaydo.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor

public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('CUSTOMER_MANAGER_CUSTOMER')")
    public ResponseEntity<CustomerResponseDTO> getProfile(
            Authentication authentication) {
        return ResponseEntity.ok(
                customerService.getProfile(authentication.getName()));
    }

    @PostMapping("update/profile")
    @PreAuthorize("hasAuthority('CUSTOMER_MANAGER_CUSTOMER')")
    public ResponseEntity<?> updateProfile(
            @RequestPart("request") CustomerProfileRequestDTO request,
            @RequestPart(value = "img", required = false) MultipartFile img,
            Authentication authentication) {
                customerService.updateProfile(authentication.getName(), request, img);
                return  ResponseEntity.ok("update success");
    }

    @GetMapping("/my/orders")
    @PreAuthorize("hasAuthority('CUSTOMER_MANAGER_CUSTOMER')")
    public ResponseEntity<List<OrderResponseDTO>> getMyOrders(
            Authentication authentication) {
        return ResponseEntity.ok(
                customerService.getMyOrders(
                        authentication.getName()));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGE_CUSTOMER','EMPLOYEE_MANAGE_ORDER')")
    public ResponseEntity<List<CustomerResponseDTO>> getAll() {
        return ResponseEntity.ok(customerService.getAll());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_CUSTOMER')")
    public ResponseEntity<List<CustomerResponseDTO>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(customerService.search(keyword));
    }

    @GetMapping("details/{id}")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_CUSTOMER')")
    public ResponseEntity<CustomerResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getById(id));
    }

    @PostMapping("delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_CUSTOMER')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.ok("delete success");
    }
}