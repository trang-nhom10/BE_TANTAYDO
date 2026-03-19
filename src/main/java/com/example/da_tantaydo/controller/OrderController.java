package com.example.da_tantaydo.controller;

import com.example.da_tantaydo.model.dto.request.OrderRequestDTO;
import com.example.da_tantaydo.model.dto.request.OrderUpdateStatusDTO;
import com.example.da_tantaydo.model.dto.response.OrderResponseDTO;
import com.example.da_tantaydo.model.enums.OrderStatus;
import com.example.da_tantaydo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //  KHÁCH HÀNG TẠO ĐƠN
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('CUSTOMER_MANAGE_ORDER')")
    public ResponseEntity<OrderResponseDTO> create(
            @RequestBody OrderRequestDTO request) {
        return ResponseEntity.ok(orderService.create(request));
    }

    // NHÂN VIÊN/ADMIN CẬP NHẬT TRẠNG THÁI + GIÁ
    @PostMapping("status/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGE_ORDER','EMPLOYEE_MANAGE_ORDER')")
    public ResponseEntity<OrderResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody OrderUpdateStatusDTO request) {
        return ResponseEntity.ok(orderService.updateStatus(id, request));
    }

    // KHÁCH HÀNG HỦY ĐƠN
    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER_MANAGE_ORDER','ADMIN_MANAGE_ORDER')")
    public ResponseEntity<String> cancel(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.ok("Hủy đơn hàng thành công");
    }

    // LẤY CHI TIẾT
    @GetMapping("details/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGE_ORDER','EMPLOYEE_MANAGE_ORDER','CUSTOMER_MANAGE_ORDER')")
    public ResponseEntity<OrderResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getById(id));
    }

    //  ADMIN/NHÂN VIÊN - LẤY TẤT CẢ
    // GET /api/orders?page=0&size=10
    @GetMapping("/getall")
    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGE_ORDER','EMPLOYEE_MANAGE_ORDER')")
    public ResponseEntity<Page<OrderResponseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(orderService.getAll(page, size));
    }

    // LẤY ĐƠN HÀNG THEO KHÁCH HÀNG
    // GET /api/orders/customer/1?page=0&size=10
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGE_ORDER','CUSTOMER_MANAGE_ORDER')")
    public ResponseEntity<Page<OrderResponseDTO>> getByCustomer(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(orderService.getByCustomer(customerId, page, size));
    }

    //  LỌC THEO TRẠNG THÁI
    // GET /api/orders/status?status=PENDING&page=0&size=10
    @GetMapping("/status")
    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGE_ORDER','EMPLOYEE_MANAGE_ORDER')")
    public ResponseEntity<Page<OrderResponseDTO>> getByStatus(
            @RequestParam OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(orderService.getByStatus(status, page, size));
    }

    // TÌM KIẾM
    // GET /api/orders/search?keyword=nguyen&page=0&size=10
    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('ADMIN_MANAGE_ORDER','EMPLOYEE_MANAGE_ORDER')")
    public ResponseEntity<Page<OrderResponseDTO>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(orderService.search(keyword, page, size));
    }
}