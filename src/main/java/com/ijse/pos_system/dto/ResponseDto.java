package com.ijse.pos_system.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ResponseDto <T>  {
    private Long orderId; // The ID of the order
    private BigDecimal totalPrice; // The total price of the order
    private List<ItemRequestDto> items;
}
