package com.pos_system.services;

import java.util.List;
import java.util.Optional;

import com.pos_system.dto.ProductDto;
import com.pos_system.entities.Product;

public interface ProductService {
    Product createProduct(ProductDto dto);
    Product updateProduct(Integer id, ProductDto dto);
    void deleteProduct(Integer id);
    Optional<Product> findById(Integer id);
    Optional<Product> findByBarcode(String barcode);
    List<Product> findAllActiveProducts();
    List<Product> findAllProducts();
    List<Product> searchActiveProducts(String search);
    List<Product> findLowStockProducts();
    boolean existsByBarcode(String barcode);
    void reduceStock(Integer productId, Integer quantity);
    void addStock(Integer productId, Integer quantity);
}
