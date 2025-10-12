package com.pos_system.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pos_system.dto.ProductDto;
import com.pos_system.entities.Product;
import com.pos_system.services.serviceImpl.ProductServiceImpl;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto dto) {
        Product product = productService.createProduct(dto);
        return ResponseEntity.ok(convertToDto(product));
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Integer id,
                                                    @RequestBody ProductDto dto) {
        Product updated = productService.updateProduct(id, dto);
        return ResponseEntity.ok(convertToDto(updated));
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Integer id) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return ResponseEntity.ok(convertToDto(product));
    }

    // ================= GET ALL =================
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<Product> products = productService.findAllProducts();
        List<ProductDto> dtos = products.stream()
                                        .map(this::convertToDto)
                                        .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // ================= GET ACTIVE =================
    @GetMapping("/active")
    public ResponseEntity<List<ProductDto>> getAllActiveProducts() {
        List<Product> products = productService.findAllActiveProducts();
        List<ProductDto> dtos = products.stream()
                                        .map(this::convertToDto)
                                        .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // ================= SEARCH =================
    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProducts(@RequestParam String keyword) {
        List<Product> products = productService.searchActiveProducts(keyword);
        List<ProductDto> dtos = products.stream()
                                        .map(this::convertToDto)
                                        .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // ================= LOW STOCK =================
    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductDto>> getLowStockProducts() {
        List<Product> products = productService.findLowStockProducts();
        List<ProductDto> dtos = products.stream()
                                        .map(this::convertToDto)
                                        .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // ================== HELPER ==================
    private ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setBarcode(product.getBarcode());
        dto.setPrice(product.getPrice());
        dto.setCostPrice(product.getCostPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setMinStockLevel(product.getMinStockLevel());
        dto.setIsActive(product.getIsActive());
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
        }
        return dto;
    }
}
