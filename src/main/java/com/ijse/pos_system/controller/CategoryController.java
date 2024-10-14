package com.ijse.pos_system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


//import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import com.ijse.pos_system.dto.CategoryDto;
import com.ijse.pos_system.entities.ItemCategory;
import com.ijse.pos_system.services.CategoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/categories")
@Api(value = "Category Management System")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @ApiOperation("Add a new category")
    @PostMapping("/addcategories")
    public ResponseEntity<?> addCategory(@ApiParam(value = "Category data", required = true) @RequestBody CategoryDto categoryDto) {
        categoryService.saveCategory(categoryDto);
        
        return ResponseEntity.ok("Category added successfully");
    }
    
    @ApiOperation(value = "Update an existing category")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@ApiParam(value = "Category Id", required = true) @PathVariable Long id, 
    @ApiParam(value = "Updated category data", required = true) @RequestBody CategoryDto categoryDto) {
        categoryService.updateCategory(id, categoryDto);
        
        return ResponseEntity.ok("Category updated successfully!");
    }

    @ApiOperation(value = "Delete a category by ID")
    @DeleteMapping("/deletecategories/{id}")
    public ResponseEntity<?> deleteCategory(@ApiParam(value = "Category Id", required = true) @PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category daleted successfully!");
    }

    @ApiOperation(value = "Get all categories")
    @GetMapping("/all")
    public ResponseEntity<List<ItemCategory>> getAllCategories() {
        List<ItemCategory> categories=categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @ApiOperation(value = "Get a category by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ItemCategory> getCategoryById(@ApiParam(value = "Category Id", required = true) @PathVariable Long id) {
        ItemCategory categoryDto=categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryDto);
    }

}
