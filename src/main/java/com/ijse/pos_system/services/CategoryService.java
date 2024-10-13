package com.ijse.pos_system.services;

import java.util.List;

import com.ijse.pos_system.dto.CategoryDto;
import com.ijse.pos_system.entities.ItemCategory;

public interface CategoryService {
    ItemCategory saveCategory(CategoryDto category);
    ItemCategory updateCategory(Long id, CategoryDto categoryDto);
    void deleteCategory(Long id);
    List<ItemCategory> getAllCategories();
    ItemCategory getCategoryById(Long id);
}
