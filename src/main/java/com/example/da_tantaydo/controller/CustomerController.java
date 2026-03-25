package com.example.da_tantaydo.controller;

import com.example.da_tantaydo.model.dto.request.CustomerProfileRequestDTO;
import com.example.da_tantaydo.model.dto.response.CustomerResponseDTO;
import com.example.da_tantaydo.model.dto.response.OrderResponseDTO;
//import com.example.da_tantaydo.service.CustomerService;
import com.example.da_tantaydo.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    //  KHÁCH XEM THÔNG TIN CỦA MÌNH
    @GetMapping("/profile")
//    @PreAuthorize("hasAuthority('CUSTOMER_UPDATE_PROFILE')")
    public ResponseEntity<CustomerResponseDTO> getProfile(
            Authentication authentication) {
        return ResponseEntity.ok(
                customerService.getProfile(authentication.getName()));
    }

    //  KHÁCH TỰ UPDATE THÔNG TIN + ẢNH
    @PostMapping("update/profile")
    @PreAuthorize("hasAuthority('CUSTOMER_UPDATE_PROFILE')")
    public ResponseEntity<?> updateProfile(
            @RequestPart("request") CustomerProfileRequestDTO request,
            @RequestPart(value = "img", required = false) MultipartFile img,
            Authentication authentication) {
                customerService.updateProfile(authentication.getName(), request, img);
                return  ResponseEntity.ok("update success");
    }

    //  KHÁCH XEM LỊCH SỬ ĐẶT LỊCH CỦA MÌNH
    // GET /api/customers/my/appointments?page=0&size=10
//    @GetMapping("/my/appointments")
//    @PreAuthorize("hasAuthority('CUSTOMER_BOOK_APPOINTMENT')")
//    public ResponseEntity<Page<AppointmentResponseDTO>> getMyAppointments(
//            Authentication authentication,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        return ResponseEntity.ok(
//                customerService.getMyAppointments(
//                        authentication.getName(), page, size));
//    }

    //  KHÁCH XEM LỊCH SỬ ĐƠN HÀNG CỦA MÌNH
    // GET /api/customers/my/orders?page=0&size=10
    @GetMapping("/my/orders")
    @PreAuthorize("hasAuthority('CUSTOMER_MANAGE_ORDER')")
    public ResponseEntity<List<OrderResponseDTO>> getMyOrders(
            Authentication authentication) {
        return ResponseEntity.ok(
                customerService.getMyOrders(
                        authentication.getName()));
    }

    //  ADMIN - LẤY TẤT CẢ KHÁCH HÀNG
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_CUSTOMER')")
    public ResponseEntity<List<CustomerResponseDTO>> getAll() {
        return ResponseEntity.ok(customerService.getAll());
    }

    //  ADMIN - TÌM KIẾM
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_CUSTOMER')")
    public ResponseEntity<List<CustomerResponseDTO>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(customerService.search(keyword));
    }

    //  ADMIN - LẤY CHI TIẾT
    @GetMapping("details/{id}")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_CUSTOMER')")
    public ResponseEntity<CustomerResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getById(id));
    }

    //  ADMIN - XÓA KHÁCH HÀNG
    @PostMapping("delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN_MANAGE_CUSTOMER')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.ok("Xóa khách hàng thành công");
    }
}