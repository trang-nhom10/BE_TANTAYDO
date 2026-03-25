package com.example.da_tantaydo.service;


import com.example.da_tantaydo.model.dto.request.OrderRequestDTO;
import com.example.da_tantaydo.model.dto.request.OrderUpdateStatusDTO;
import com.example.da_tantaydo.model.dto.response.OrderResponseDTO;
import com.example.da_tantaydo.model.enums.OrderStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    OrderResponseDTO create(OrderRequestDTO request);
    OrderResponseDTO updateStatus(Long id, OrderUpdateStatusDTO request);
    void delete(Long id);
    OrderResponseDTO getById(Long id);
    List<OrderResponseDTO> getAll();
    List<OrderResponseDTO> getByCustomer(Long customerId);
    List<OrderResponseDTO> getByStatus(OrderStatus status);
    List<OrderResponseDTO> search(String keyword);
}