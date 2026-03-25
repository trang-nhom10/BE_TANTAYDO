package com.example.da_tantaydo.service;


import com.example.da_tantaydo.model.dto.request.CustomerProfileRequestDTO;
import com.example.da_tantaydo.model.dto.response.AppointmentResponseDTO;
import com.example.da_tantaydo.model.dto.response.CustomerResponseDTO;
import com.example.da_tantaydo.model.dto.response.OrderResponseDTO;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface CustomerService {

    void updateProfile(String gmail, CustomerProfileRequestDTO request, MultipartFile img);
    CustomerResponseDTO getProfile(String gmail);
    List<AppointmentResponseDTO> getMyAppointments(Authentication authentication);
    List<OrderResponseDTO> getMyOrders(String gmail);
    List<CustomerResponseDTO> getAll();
    List<CustomerResponseDTO> search(String keyword);
    CustomerResponseDTO getById(Long id);
    void delete(Long id);
}