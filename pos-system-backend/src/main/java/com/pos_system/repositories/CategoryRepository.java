package com.pos_system.repositories;

import java.util.List;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pos_system.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{
    boolean existsByName(String name);

    Optional<Category> findByName(String name);

    List<Category> findByIsActiveTrue();

    @Query("SELECT c FROM Category c WHERE c.isActive = true ORDER BY c.name ASC")
    List<Category> findAllActiveCategoriesOrderByName();
}
