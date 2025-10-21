package com.pos_system.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pos_system.dto.InventoryMovementDto;
import com.pos_system.entities.User;
import com.pos_system.models.ReferenceType;
import com.pos_system.services.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @PostMapping("/add-stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InventoryMovementDto> addStock(
            @RequestParam Integer productId,
            @RequestParam Integer quantity,
            @RequestParam ReferenceType refType,
            @RequestParam(required = false) Integer refId,
            @RequestParam(required = false) String notes,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(inventoryService.addStock(productId, quantity, refType, refId, notes, user));
    }

    @PostMapping("/remove-stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InventoryMovementDto> removeStock(
            @RequestParam Integer productId,
            @RequestParam Integer quantity,
            @RequestParam ReferenceType refType,
            @RequestParam(required = false) Integer refId,
            @RequestParam(required = false) String notes,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(inventoryService.removeStock(productId, quantity, refType, refId, notes, user));
    }

    @GetMapping("/movements/{productId}")
    public ResponseEntity<List<InventoryMovementDto>> getMovements(@PathVariable Integer productId) {
        return ResponseEntity.ok(inventoryService.getMovementsByProduct(productId));
    }
}
