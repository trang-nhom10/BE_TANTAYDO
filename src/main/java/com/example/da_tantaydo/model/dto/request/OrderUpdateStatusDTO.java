package com.example.da_tantaydo.model.dto.request;


import com.example.da_tantaydo.model.enums.OrderStatus;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateStatusDTO {
    private OrderStatus status;
    private BigDecimal totalPrice; // NHÂN VIÊN NHẬP GIÁ KHI HOÀN THÀNH
}
