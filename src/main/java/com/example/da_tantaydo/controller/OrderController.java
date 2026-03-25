package com.example.da_tantaydo.controller;

import com.example.da_tantaydo.model.dto.request.OrderRequestDTO;
import com.example.da_tantaydo.model.dto.request.OrderUpdateStatusDTO;
import com.example.da_tantaydo.model.dto.response.OrderResponseDTO;
import com.example.da_tantaydo.model.enums.OrderStatus;
import com.example.da_tantaydo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('CUSTOMER_MANAGE_ORDER')")
    public ResponseEntity<OrderResponseDTO> create(
            @RequestBody OrderRequestDTO request) {
        return ResponseEntity.ok(orderService.create(request));
    }

    @PostMapping("status/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGE_ORDER','EMPLOYEE_MANAGE_ORDER')")
    public ResponseEntity<OrderResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody OrderUpdateStatusDTO request) {
        return ResponseEntity.ok(orderService.updateStatus(id, request));
    }

    @PostMapping("delete/{id}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER_MANAGE_ORDER','ADMIN_MANAGE_ORDER')")
    public ResponseEntity<String> cancel(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.ok("Hủy đơn hàng thành công");
    }

    @GetMapping("details/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGE_ORDER','EMPLOYEE_MANAGE_ORDER','CUSTOMER_MANAGE_ORDER')")
    public ResponseEntity<OrderResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getById(id));
    }

    @GetMapping("/getall")
    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGE_ORDER','EMPLOYEE_MANAGE_ORDER')")
    public ResponseEntity<List<OrderResponseDTO>> getAll( ){
        return ResponseEntity.ok(orderService.getAll());
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGE_ORDER','CUSTOMER_MANAGE_ORDER')")
    public ResponseEntity<List<OrderResponseDTO>> getByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getByCustomer(customerId));
    }

    @GetMapping("/status")
    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGE_ORDER','EMPLOYEE_MANAGE_ORDER')")
    public ResponseEntity<List<OrderResponseDTO>> getByStatus(@RequestParam OrderStatus status){
        return ResponseEntity.ok(orderService.getByStatus(status));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGE_ORDER','EMPLOYEE_MANAGE_ORDER')")
    public ResponseEntity<List<OrderResponseDTO>> search(@RequestParam String keyword){
        return ResponseEntity.ok(orderService.search(keyword));
    }
}