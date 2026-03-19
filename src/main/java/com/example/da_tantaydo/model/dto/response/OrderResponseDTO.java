package com.example.da_tantaydo.model.dto.response;

import com.example.da_tantaydo.model.enums.OrderStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {
    private Long id;
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private String doctorName;
    private String service;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private String note;
    private LocalDateTime createdAt;
}