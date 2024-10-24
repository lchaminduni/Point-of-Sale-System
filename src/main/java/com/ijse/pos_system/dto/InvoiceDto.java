package com.ijse.pos_system.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.ijse.pos_system.entities.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDto {
    private Long orderId;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private List<ItemDto> orderedItems;
}
