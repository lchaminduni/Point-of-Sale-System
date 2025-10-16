package com.pos_system.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.pos_system.entities.PaymentMethod;
import com.pos_system.entities.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleResponseDto {
    private Integer id;
    private String saleNumber;
    private String cashierName;
    private String customerName;
    private String customerPhone;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime saleDate;
    private List<SaleItemResponseDto> saleItems;

}
