package com.pos_system.services.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pos_system.dto.CategoryRequestDto;
import com.pos_system.dto.CategoryResponseDto;
import com.pos_system.entities.Category;
import com.pos_system.repositories.CategoryRepository;
import com.pos_system.services.CategoryService;


@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequest) {
        if (categoryRepository.existsByName(categoryRequest.getName())) {
            throw new RuntimeException("Category with this name already exists");
        }

        Category category = Category.builder()
                .name(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .isActive(categoryRequest.getIsActive())
                .build();

        Category saved = categoryRepository.save(category);
        return mapToResponseDTO(saved);
    }

    @Override
    public CategoryResponseDto updateCategory(Integer id, CategoryRequestDto categoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!category.getName().equals(categoryRequest.getName())
                && categoryRepository.existsByName(categoryRequest.getName())) {
            throw new RuntimeException("Category with this name already exists");
        }

        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        category.setIsActive(categoryRequest.getIsActive());

        Category updated = categoryRepository.save(category);
        return mapToResponseDTO(updated);
    }

    @Override
    public void deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.delete(category);
    }

    @Override
    public CategoryResponseDto getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return mapToResponseDTO(category);
    }

    @Override
    public List<CategoryResponseDto> getAllActiveCategories() {
        return categoryRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponseDto> getAllActiveCategoriesOrderByName() {
        return categoryRepository.findAllActiveCategoriesOrderByName()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private CategoryResponseDto mapToResponseDTO(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .isActive(category.getIsActive())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}
