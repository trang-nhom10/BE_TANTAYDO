package com.example.da_tantaydo.service.impl;

import com.example.da_tantaydo.helper.MediaStorageService;
import com.example.da_tantaydo.model.dto.request.CustomerProfileRequestDTO;
import com.example.da_tantaydo.model.dto.response.AppointmentResponseDTO;
import com.example.da_tantaydo.model.dto.response.CustomerResponseDTO;
import com.example.da_tantaydo.model.dto.response.OrderResponseDTO;
import com.example.da_tantaydo.model.entity.Customer;
import com.example.da_tantaydo.repository.*;
import com.example.da_tantaydo.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final AppointmentRepository appointmentRepository;
    private final OrderRepository orderRepository;
    private final DataSourceRepository dataSourceRepository;
    private final MediaStorageService mediaStorageService;

    // ============================================
    // KHÁCH XEM THÔNG TIN CỦA MÌNH
    // ============================================
    @Override
    public CustomerResponseDTO getProfile(String gmail) {
        Customer customer = customerRepository.findByUserGmail(gmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        return toDTO(customer);
    }

    // ============================================
    // KHÁCH TỰ UPDATE THÔNG TIN + ẢNH
    // ============================================
    @Override
    public CustomerResponseDTO updateProfile(String gmail,
                                             CustomerProfileRequestDTO request,
                                             MultipartFile img) {
        Customer customer = customerRepository.findByUserGmail(gmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        customer.setFullName(request.getFullName());
        customer.setPhone(request.getPhone());
        customer.setCccd(request.getCccd());
        customer.setAddress(request.getAddress());

        if (request.getDate() != null) {
            customer.setDate(LocalDateTime.parse(request.getDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }

        // ✅ UPLOAD ẢNH LÊN CLOUDINARY
        if (img != null && !img.isEmpty()) {
            if (customer.getImg() != null) {
                mediaStorageService.deleteMedia(Long.parseLong(customer.getImg()));
            }
            String mediaId = mediaStorageService.uploadMedia(img);
            customer.setImg(mediaId);
        }

        return toDTO(customerRepository.save(customer));
    }

    // ============================================
    // KHÁCH XEM LỊCH SỬ ĐẶT LỊCH
    // ============================================
    @Override
    public Page<AppointmentResponseDTO> getMyAppointments(String gmail,
                                                          int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return appointmentRepository
                .findByCustomerUserGmailOrderByCreatedAtDesc(gmail, pageable)
                .map(a -> AppointmentResponseDTO.builder()
                        .id(a.getId())
                        .customerId(a.getCustomer().getId())
                        .customerName(a.getCustomer().getFullName())
                        .customerPhone(a.getCustomer().getPhone())
                        .doctorId(a.getDoctor().getId())
                        .doctorName(a.getDoctor().getFullName())
                        .scheduleId(a.getSchedule().getId())
                        .workDate(a.getSchedule().getWorkDate())
                        .startTime(a.getSchedule().getStartTime())
                        .endTime(a.getSchedule().getEndTime())
                        .service(a.getService())
                        .reason(a.getReason())
                        .note(a.getNote())
                        .status(a.getStatus())
                        .createdAt(a.getCreatedAt())
                        .build());
    }

    // ============================================
    // KHÁCH XEM LỊCH SỬ ĐƠN HÀNG
    // ============================================
    @Override
    public Page<OrderResponseDTO> getMyOrders(String gmail, int page, int size) {
        Customer customer = customerRepository.findByUserGmail(gmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        Pageable pageable = PageRequest.of(page, size);
        return orderRepository
                .findByCustomerIdOrderByCreatedAtDesc(customer.getId(), pageable)
                .map(o -> OrderResponseDTO.builder()
                        .id(o.getId())
                        .customerId(o.getCustomer().getId())
                        .customerName(o.getCustomer().getFullName())
                        .customerPhone(o.getCustomer().getPhone())
                        .doctorName(o.getDoctor() != null
                                ? o.getDoctor().getFullName() : null)
                        .service(o.getService())
                        .totalPrice(o.getTotalPrice())
                        .status(o.getStatus())
                        .note(o.getNote())
                        .createdAt(o.getCreatedAt())
                        .build());
    }

    // ============================================
    // ADMIN QUẢN LÍ KHÁCH HÀNG
    // ============================================
    @Override
    public Page<CustomerResponseDTO> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by("id").descending());
        return customerRepository.findAll(pageable).map(this::toDTO);
    }

    @Override
    public Page<CustomerResponseDTO> search(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return customerRepository.search(keyword, pageable).map(this::toDTO);
    }

    @Override
    public CustomerResponseDTO getById(Long id) {
        return toDTO(customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng")));
    }

    @Override
    public void delete(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        customerRepository.delete(customer);
    }

    // ============================================
    // LẤY URL ẢNH TỪ DATASOURCE
    // ============================================
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
                .cccd(c.getCccd())
                .date(c.getDate())
                .address(c.getAddress())
                .img(getImgUrl(c.getImg())) // ✅ TRẢ VỀ URL
                .build();
    }
}