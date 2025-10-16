package com.pos_system.services;

import java.util.List;

import com.pos_system.dto.SaleItemRequestDto;
import com.pos_system.dto.SaleItemResponseDto;

public interface SaleItemService {
    SaleItemResponseDto addSaleItem(SaleItemRequestDto saleItemRequest);

    SaleItemResponseDto updateSaleItem(Integer id, SaleItemRequestDto saleItemRequest);

    void deleteSaleItem(Integer id);

    SaleItemResponseDto getSaleItemById(Integer id);

    List<SaleItemResponseDto> getItemsBySale(Integer saleId);
}
