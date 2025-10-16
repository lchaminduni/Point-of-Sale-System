package com.pos_system.services.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pos_system.dto.SaleItemRequestDto;
import com.pos_system.dto.SaleItemResponseDto;
import com.pos_system.entities.Product;
import com.pos_system.entities.Sale;
import com.pos_system.entities.SaleItem;
import com.pos_system.repositories.ProductRepository;
import com.pos_system.repositories.SaleItemRepository;
import com.pos_system.repositories.SaleRepository;
import com.pos_system.services.SaleItemService;

@Service
public class SaleItemServiceImpl implements SaleItemService{
    @Autowired
    private SaleItemRepository saleItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Override
    public SaleItemResponseDto addSaleItem(SaleItemRequestDto dto) {
        Sale sale = saleRepository.findById(dto.getSaleId())
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        SaleItem saleItem = SaleItem.builder()
                .sale(sale)
                .product(product)
                .quantity(dto.getQuantity())
                .unitPrice(dto.getUnitPrice())
                .totalPrice(dto.getTotalPrice())
                .build();

        SaleItem saved = saleItemRepository.save(saleItem);
        return mapToResponseDTO(saved);
    }

    @Override
    public SaleItemResponseDto updateSaleItem(Integer id, SaleItemRequestDto dto) {
        SaleItem saleItem = saleItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale item not found"));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        saleItem.setProduct(product);
        saleItem.setQuantity(dto.getQuantity());
        saleItem.setUnitPrice(dto.getUnitPrice());
        saleItem.setTotalPrice(dto.getTotalPrice());

        SaleItem updated = saleItemRepository.save(saleItem);
        return mapToResponseDTO(updated);
    }

    @Override
    public void deleteSaleItem(Integer id) {
        SaleItem saleItem = saleItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale item not found"));
        saleItemRepository.delete(saleItem);
    }

    @Override
    public SaleItemResponseDto getSaleItemById(Integer id) {
        SaleItem saleItem = saleItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale item not found"));
        return mapToResponseDTO(saleItem);
    }

    @Override
    public List<SaleItemResponseDto> getItemsBySale(Integer saleId) {
        return saleItemRepository.findBySaleId(saleId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private SaleItemResponseDto mapToResponseDTO(SaleItem saleItem) {
        return SaleItemResponseDto.builder()
                .id(saleItem.getId())
                .saleId(saleItem.getSale().getId())
                .productId(saleItem.getProduct().getId())
                .productName(saleItem.getProduct().getName())
                .quantity(saleItem.getQuantity())
                .unitPrice(saleItem.getUnitPrice())
                .totalPrice(saleItem.getTotalPrice())
                .build();
    }
}
