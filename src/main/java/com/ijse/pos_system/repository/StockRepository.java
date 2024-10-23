package com.ijse.pos_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ijse.pos_system.entities.Stock;


@Repository
public interface StockRepository extends JpaRepository<Stock, Long>{
    //Stock findByItemId(Long itemId);
    Optional<Stock> findByItemId(Long itemId);
}
