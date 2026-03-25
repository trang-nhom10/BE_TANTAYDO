package com.example.da_tantaydo.service.impl;

import com.example.da_tantaydo.helper.MediaStorageService;
import com.example.da_tantaydo.model.dto.request.CustomerProfileRequestDTO;
import com.example.da_tantaydo.model.dto.response.AppointmentResponseDTO;
import com.example.da_tantaydo.model.dto.response.CustomerResponseDTO;
import com.example.da_tantaydo.model.dto.response.OrderResponseDTO;
import com.example.da_tantaydo.model.entity.Customer;
import com.example.da_tantaydo.model.entity.User;
import com.example.da_tantaydo.repository.*;
import com.example.da_tantaydo.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final AppointmentRepository appointmentRepository;
    private final OrderRepository orderRepository;
    private final DataSourceRepository dataSourceRepository;
    private final MediaStorageService mediaStorageService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public CustomerResponseDTO getProfile(String gmail) {
        Customer customer = customerRepository.findByUserGmail(gmail).orElseThrow(() -> new RuntimeException("Customer not found."));
        return toDTO(customer);
    }

    @Override
    public void updateProfile(String gmail, CustomerProfileRequestDTO request, MultipartFile img) {
        User user = userRepository.findByGmail(gmail).orElseThrow(() -> new RuntimeException("User not found"));
        Customer customer = customerRepository.findByUserGmail(gmail).orElseThrow(() -> new RuntimeException("Customer not found"));
        if (request.getFullName() != null) customer.setFullName(request.getFullName());
        if (request.getPhone() != null) customer.setPhone(request.getPhone());
        if (request.getDate() != null) customer.setDate(LocalDate.parse(request.getDate()));
        if (request.getAddress() != null) customer.setAddress(request.getAddress());
        if (request.getPass() != null && !request.getPass().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPass()));
            userRepository.save(user);
        }

        if (img != null && !img.isEmpty()) {
            if (customer.getImg() != null && customer.getImg().matches("\\d+"))
                mediaStorageService.deleteMedia(Long.valueOf(customer.getImg()));
            customer.setImg(mediaStorageService.uploadMedia(img));
        }

        customerRepository.save(customer);
    }

    @Override
    public List<AppointmentResponseDTO> getMyAppointments(Authentication authentication) {
        String gmail = authentication.getName();
        return appointmentRepository
                .findByCustomerUserGmailOrderByCreateAtDesc(gmail)
                .stream()
                .map(a -> AppointmentResponseDTO.builder()
                        .id(a.getId())
                        .name(a.getNameCustomer())
                        .date(a.getYear())
                        .phone(a.getPhone())
                        .gmail(a.getGmail())
                        .address(a.getAddress())
                        .createdAt(a.getCreateAt() != null ? a.getCreateAt().toString() : null)
                        .doctorName(a.getDoctor() != null ? a.getDoctor().getName() : null)
                        .timeOpen(a.getTimeopen())
                        .note(a.getNote())
                        .status(a.getStatus() != null ? a.getStatus().name() : null)
                        .build())
                .toList();
    }

    @Override
    public List<OrderResponseDTO> getMyOrders(String gmail) {
        Customer customer = customerRepository.findByUserGmail(gmail).orElseThrow(() -> new RuntimeException("Customer not found."));
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customer.getId()).stream().map(
                o -> OrderResponseDTO.builder()
                        .id(o.getId())
                        .customerId(o.getCustomer().getId())
                        .customerName(o.getCustomer().getFullName())
                        .customerPhone(o.getCustomer().getPhone())
                        .doctorName(o.getDoctor() != null
                        ? o.getDoctor().getName() : null)
                        .service(o.getService())
                        .totalPrice(o.getTotalPrice())
                        .status(o.getStatus())
                        .note(o.getNote())
                        .createdAt(o.getCreatedAt())
                        .build()).toList();

    }

    @Override
    public List<CustomerResponseDTO> getAll() {
        return customerRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public List<CustomerResponseDTO> search(String keyword) {
        return customerRepository.search(keyword).stream().map(this::toDTO).toList();
    }

    @Override
    public CustomerResponseDTO getById(Long id) {
        return toDTO(customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found.")));
    }

    @Override
    public void delete(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found."));
        customerRepository.delete(customer);
    }

    private String getImgUrl(String mediaId) {
        if (mediaId == null) return null;
        try {
            return dataSourceRepository.findById(Long.parseLong(mediaId))
                    .map(ds -> ds.getImageUrl())
                    .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    private CustomerResponseDTO toDTO(Customer c) {
        return CustomerResponseDTO.builder()
                .id(c.getId())
                .email(c.getUser().getGmail())
                .fullName(c.getFullName())
                .phone(c.getPhone())
                .date(c.getDate().atStartOfDay())
                .address(c.getAddress())
                .img(getImgUrl(c.getImg()))
                .build();
    }
}