package com.ijse.pos_system.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class InvoiceItemDto {
    private String itemName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
}
