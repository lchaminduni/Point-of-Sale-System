package com.pos_system.services.serviceImpl;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pos_system.dto.InventoryMovementDto;
import com.pos_system.entities.InventoryMovement;
import com.pos_system.entities.Product;
import com.pos_system.entities.User;
import com.pos_system.models.MovementType;
import com.pos_system.models.ReferenceType;
import com.pos_system.repositories.InventoryMovementRepository;
import com.pos_system.repositories.ProductRepository;
import com.pos_system.repositories.UserRepository;
import com.pos_system.services.InventoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService{
    private final InventoryMovementRepository inventoryMovementRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));
    }

    @Override
    public InventoryMovementDto addStock(Integer productId, Integer quantity, ReferenceType refType, Integer refId, String notes, User user) {
        if (user == null) user = getCurrentUser(); //ensure not null

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setStockQuantity(product.getStockQuantity() + quantity);
        productRepository.save(product);

        InventoryMovement movement = InventoryMovement.builder()
                .product(product)
                .movementType(MovementType.IN)
                .quantity(quantity)
                .referenceType(refType)
                .referenceId(refId)
                .notes(notes)
                .createdBy(user) //guaranteed not null
                .build();

        inventoryMovementRepository.save(movement);
        return mapToDTO(movement);
    }

    @Override
    public InventoryMovementDto removeStock(Integer productId, Integer quantity, ReferenceType refType, Integer refId, String notes, User user) {
        if (user == null) user = getCurrentUser(); //ensure not null

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStockQuantity() < quantity)
            throw new RuntimeException("Not enough stock");

        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        InventoryMovement movement = InventoryMovement.builder()
                .product(product)
                .movementType(MovementType.OUT)
                .quantity(quantity)
                .referenceType(refType)
                .referenceId(refId)
                .notes(notes)
                .createdBy(user) // guaranteed not null
                .build();

        inventoryMovementRepository.save(movement);
        return mapToDTO(movement);
    }

    @Override
    public List<InventoryMovementDto> getMovementsByProduct(Integer productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        return inventoryMovementRepository.findByProduct(product)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    private InventoryMovementDto mapToDTO(InventoryMovement movement) {
        return InventoryMovementDto.builder()
                .id(movement.getId())
                .productId(movement.getProduct().getId())
                .productName(movement.getProduct().getName())
                .movementType(movement.getMovementType())
                .quantity(movement.getQuantity())
                .referenceType(movement.getReferenceType())
                .referenceId(movement.getReferenceId())
                .notes(movement.getNotes())
                .createdBy(movement.getCreatedBy().getId())
                .createdByName(movement.getCreatedBy().getFirstName() + " " + movement.getCreatedBy().getLastName())
                .createdAt(movement.getCreatedAt())
                .build();
    }
}
