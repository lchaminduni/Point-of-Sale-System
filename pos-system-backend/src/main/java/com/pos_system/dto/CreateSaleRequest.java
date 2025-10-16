package com.pos_system.dto;

import java.math.BigDecimal;
import java.util.List;

import com.pos_system.entities.PaymentMethod;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class CreateSaleRequest {
    private String customerName;
    private String customerPhone;

    @NotNull
    private PaymentMethod paymentMethod;

    @NotNull
    private BigDecimal discountAmount;

    private String notes;

    @NotEmpty(message = "Sale must contain at least one item")
    private List<SaleItemRequest> items; 

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SaleItemRequest {
        @NotNull(message = "Product ID is required")
        private Integer productId;

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;

        @NotNull(message = "Unit price is required")
        private BigDecimal unitPrice;

        @NotNull
        private PaymentMethod paymentMethod;

        @NotNull
        private BigDecimal discountAmount;

        @NotEmpty(message = "Sale must contain at least one item")
        private List<SaleItemRequest> items;

    }
}
