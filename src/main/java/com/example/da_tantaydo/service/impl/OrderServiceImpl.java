package com.example.da_tantaydo.service.impl;


import com.example.da_tantaydo.model.dto.request.OrderRequestDTO;
import com.example.da_tantaydo.model.dto.request.OrderUpdateStatusDTO;
import com.example.da_tantaydo.model.dto.response.OrderResponseDTO;
import com.example.da_tantaydo.model.entity.*;
import com.example.da_tantaydo.model.enums.OrderStatus;
import com.example.da_tantaydo.repository.*;
import com.example.da_tantaydo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public OrderResponseDTO create(OrderRequestDTO request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

        Order order = Order.builder()
                .customer(customer)
                .doctor(doctor)
                .service(request.getService())
                .note(request.getNote())
                .status(OrderStatus.PENDING)
                .build();

        return toDTO(orderRepository.save(order));
    }

    // NHÂN VIÊN CẬP NHẬT TRẠNG THÁI + GIÁ TIỀN
    @Override
    public OrderResponseDTO updateStatus(Long id, OrderUpdateStatusDTO request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        // KHÔNG CHO CẬP NHẬT ĐƠN ĐÃ HỦY
        if (order.getStatus() == OrderStatus.CANCELLED)
            throw new RuntimeException("Đơn hàng đã bị hủy, không thể cập nhật");

        order.setStatus(request.getStatus());
        if (request.getTotalPrice() != null)
            order.setTotalPrice(request.getTotalPrice());

        return toDTO(orderRepository.save(order));
    }

    // KHÁCH HÀNG HỦY ĐƠN - CHỈ HỦY KHI CÒN PENDING
    @Override
    public void delete(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        if (order.getStatus() != OrderStatus.PENDING)
            throw new RuntimeException("Chỉ có thể hủy đơn hàng đang chờ xử lý");

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    @Override
    public OrderResponseDTO getById(Long id) {
        return toDTO(orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng")));
    }

    @Override
    public Page<OrderResponseDTO> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAllByOrderByCreatedAtDesc(pageable).map(this::toDTO);
    }

    @Override
    public Page<OrderResponseDTO> getByCustomer(Long customerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId, pageable)
                .map(this::toDTO);
    }

    @Override
    public Page<OrderResponseDTO> getByStatus(OrderStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findByStatusOrderByCreatedAtDesc(status, pageable)
                .map(this::toDTO);
    }

    @Override
    public Page<OrderResponseDTO> search(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.search(keyword, pageable).map(this::toDTO);
    }

    private OrderResponseDTO toDTO(Order o) {
        return OrderResponseDTO.builder()
                .id(o.getId())
                .customerId(o.getCustomer().getId())
                .customerName(o.getCustomer().getFullName())
                .customerPhone(o.getCustomer().getPhone())
                .doctorName(o.getDoctor() != null ? o.getDoctor().getName() : null)
                .service(o.getService())
                .totalPrice(o.getTotalPrice())
                .status(o.getStatus())
                .note(o.getNote())
                .createdAt(o.getCreatedAt())
                .build();
    }
}