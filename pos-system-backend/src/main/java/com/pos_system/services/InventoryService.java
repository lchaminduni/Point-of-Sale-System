package com.pos_system.services;

import java.util.List;

import com.pos_system.dto.InventoryMovementDto;
import com.pos_system.entities.User;
import com.pos_system.models.ReferenceType;

public interface InventoryService {
    InventoryMovementDto addStock(Integer productId, Integer quantity, ReferenceType refType, Integer refId, String notes, User user);
    InventoryMovementDto removeStock(Integer productId, Integer quantity, ReferenceType refType, Integer refId, String notes, User user);
    List<InventoryMovementDto> getMovementsByProduct(Integer productId);
}
