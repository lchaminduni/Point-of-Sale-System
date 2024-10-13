package com.ijse.pos_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ijse.pos_system.entities.Item;
@Repository
public interface ItemRepository extends JpaRepository <Item, Long>{
    
}
