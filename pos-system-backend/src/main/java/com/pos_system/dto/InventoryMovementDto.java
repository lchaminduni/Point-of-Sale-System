package com.pos_system.dto;

import java.time.LocalDateTime;

import com.pos_system.models.MovementType;
import com.pos_system.models.ReferenceType;

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
public class InventoryMovementDto {
    private Integer id;
    private Integer productId;
    private String productName;
    private MovementType movementType;
    private Integer quantity;
    private ReferenceType referenceType;
    private Integer referenceId;
    private String notes;
    private Integer createdBy;
    private String createdByName;
    private LocalDateTime createdAt;
}
