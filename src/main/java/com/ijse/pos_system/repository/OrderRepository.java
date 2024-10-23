package com.ijse.pos_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ijse.pos_system.entities.Order;

public interface OrderRepository extends JpaRepository<Order,Long>{
    
}
