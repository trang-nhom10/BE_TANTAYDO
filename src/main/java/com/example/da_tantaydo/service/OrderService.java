package com.example.da_tantaydo.service;


import com.example.da_tantaydo.model.dto.request.OrderRequestDTO;
import com.example.da_tantaydo.model.dto.request.OrderUpdateStatusDTO;
import com.example.da_tantaydo.model.dto.response.OrderResponseDTO;
import com.example.da_tantaydo.model.enums.OrderStatus;
import org.springframework.data.domain.Page;

public interface OrderService {
    OrderResponseDTO create(OrderRequestDTO request);
    OrderResponseDTO updateStatus(Long id, OrderUpdateStatusDTO request);
    void delete(Long id);
    OrderResponseDTO getById(Long id);
    Page<OrderResponseDTO> getAll(int page, int size);
    Page<OrderResponseDTO> getByCustomer(Long customerId, int page, int size);
    Page<OrderResponseDTO> getByStatus(OrderStatus status, int page, int size);
    Page<OrderResponseDTO> search(String keyword, int page, int size);
}