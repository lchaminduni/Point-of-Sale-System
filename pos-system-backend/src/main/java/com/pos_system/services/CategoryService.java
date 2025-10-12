package com.pos_system.services;

import java.util.List;

import com.pos_system.dto.CategoryRequestDto;
import com.pos_system.dto.CategoryResponseDto;

public interface CategoryService {
    CategoryResponseDto createCategory(CategoryRequestDto categoryRequest);

    CategoryResponseDto updateCategory(Integer id, CategoryRequestDto categoryRequest);

    void deleteCategory(Integer id);

    CategoryResponseDto getCategoryById(Integer id);

    List<CategoryResponseDto> getAllActiveCategories();

    List<CategoryResponseDto> getAllActiveCategoriesOrderByName();
}
