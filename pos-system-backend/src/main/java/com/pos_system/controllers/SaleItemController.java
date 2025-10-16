package com.pos_system.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pos_system.dto.SaleItemRequestDto;
import com.pos_system.dto.SaleItemResponseDto;
import com.pos_system.services.SaleItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sale-items")
@CrossOrigin(origins = "*")
public class SaleItemController {
    @Autowired
    private SaleItemService saleItemService;

    @GetMapping("/sale/{saleId}")
    public ResponseEntity<List<SaleItemResponseDto>> getItemsBySale(@PathVariable Integer saleId) {
        return ResponseEntity.ok(saleItemService.getItemsBySale(saleId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSaleItemById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(saleItemService.getSaleItemById(id));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('CASHIER')")
    public ResponseEntity<?> addSaleItem(@Valid @RequestBody SaleItemRequestDto dto) {
        try {
            return ResponseEntity.ok(saleItemService.addSaleItem(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CASHIER')")
    public ResponseEntity<?> updateSaleItem(@PathVariable Integer id, @Valid @RequestBody SaleItemRequestDto dto) {
        try {
            return ResponseEntity.ok(saleItemService.updateSaleItem(id, dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteSaleItem(@PathVariable Integer id) {
        try {
            saleItemService.deleteSaleItem(id);
            return ResponseEntity.ok(Map.of("message", "Sale item deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
