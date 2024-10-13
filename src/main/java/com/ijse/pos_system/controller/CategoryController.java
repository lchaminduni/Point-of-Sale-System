package com.ijse.pos_system.controller;

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
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/addcategories")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDto categoryDto) {
        categoryService.saveCategory(categoryDto);
        
        return ResponseEntity.ok("Category added successfully");
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        categoryService.updateCategory(id, categoryDto);
        
        return ResponseEntity.ok("Category updated successfully!");
    }

    @DeleteMapping("/deletecategories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category daleted successfully!");
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemCategory>> getAllCategories() {
        List<ItemCategory> categories=categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemCategory> getCategoryById(@PathVariable Long id) {
        ItemCategory categoryDto=categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryDto);
    }

}
