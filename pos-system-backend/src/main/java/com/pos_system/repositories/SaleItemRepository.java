package com.pos_system.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pos_system.entities.SaleItem;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem,Integer>{
    List<SaleItem> findBySaleId(Integer saleId);

    void deleteBySaleId(Integer saleId);
}
