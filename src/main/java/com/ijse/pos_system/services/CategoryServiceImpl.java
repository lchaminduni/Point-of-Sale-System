package com.ijse.pos_system.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ijse.pos_system.dto.CategoryDto;
import com.ijse.pos_system.entities.ItemCategory;
import com.ijse.pos_system.repository.ItemCategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private ItemCategoryRepository categoryRepository;

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<ItemCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public ItemCategory getCategoryById(Long id) {
        // TODO Auto-generated method stub
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public ItemCategory updateCategory(Long id, CategoryDto categoryDto) {
        ItemCategory existingCategory=categoryRepository.findById(id).orElse(null);
        existingCategory.setName(categoryDto.getName());
        return categoryRepository.save(existingCategory);
    }

    @Override
    public ItemCategory saveCategory(CategoryDto categoryDto) {
        ItemCategory category = new ItemCategory();
        category.setName(categoryDto.getName());
        return categoryRepository.save(category);
    }
}
