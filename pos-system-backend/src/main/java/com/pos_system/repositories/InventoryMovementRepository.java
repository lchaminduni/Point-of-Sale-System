package com.pos_system.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pos_system.entities.InventoryMovement;
import com.pos_system.entities.Product;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovement,Integer>{
    List<InventoryMovement> findByProduct(Product product);
}
