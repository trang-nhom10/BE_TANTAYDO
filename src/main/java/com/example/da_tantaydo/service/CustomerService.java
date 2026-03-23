package com.example.da_tantaydo.service;


import com.example.da_tantaydo.model.dto.request.CustomerProfileRequestDTO;
import com.example.da_tantaydo.model.dto.response.AppointmentResponseDTO;
import com.example.da_tantaydo.model.dto.response.CustomerResponseDTO;
import com.example.da_tantaydo.model.dto.response.OrderResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerService {

    // KHÁCH TỰ UPDATE THÔNG TIN
    CustomerResponseDTO updateProfile(String gmail,
                                      CustomerProfileRequestDTO request,
                                      MultipartFile img);

    // KHÁCH XEM THÔNG TIN CỦA MÌNH
    CustomerResponseDTO getProfile(String gmail);

    // KHÁCH XEM LỊCH SỬ ĐẶT LỊCH

    List<AppointmentResponseDTO> getMyAppointments(Authentication authentication);

    // KHÁCH XEM LỊCH SỬ ĐƠN HÀNG
    Page<OrderResponseDTO> getMyOrders(String gmail, int page, int size);

    // ADMIN
    Page<CustomerResponseDTO> getAll(int page, int size);
    Page<CustomerResponseDTO> search(String keyword, int page, int size);
    CustomerResponseDTO getById(Long id);
    void delete(Long id);
}