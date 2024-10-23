package com.ijse.pos_system.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ItemRequestDto {
    private String name;
    private BigDecimal price;
    private int quantity;
    private Long categoryId;
}
