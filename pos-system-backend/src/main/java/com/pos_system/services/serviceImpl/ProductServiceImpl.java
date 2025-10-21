package com.pos_system.services.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pos_system.dto.ProductDto;
import com.pos_system.entities.Category;
import com.pos_system.entities.Product;
import com.pos_system.repositories.CategoryRepository;
import com.pos_system.repositories.ProductRepository;
import com.pos_system.services.ProductService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Product createProduct(ProductDto dto) {
        // Generate barcode automatically if not provided
        if (dto.getBarcode() == null || dto.getBarcode().isEmpty()) {
            String generatedBarcode;
            do {
                generatedBarcode = generateBarcode();
            } while (productRepository.existsByBarcode(generatedBarcode));
            dto.setBarcode(generatedBarcode);
        } else if (productRepository.existsByBarcode(dto.getBarcode())) {
            throw new RuntimeException("Product with this barcode already exists");
        }
    
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setBarcode(dto.getBarcode());
        product.setPrice(dto.getPrice());
        product.setCostPrice(dto.getCostPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setMinStockLevel(dto.getMinStockLevel());
        product.setIsActive(dto.getIsActive());
    
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }
    
        return productRepository.save(product);
    }
    
    // Helper method to generate random 12-digit barcode
    private String generateBarcode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            int digit = (int) (Math.random() * 10);
            sb.append(digit);
        }
        return sb.toString();
    }
    

    @Override
    public Product updateProduct(Integer id, ProductDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (dto.getBarcode() != null &&
            !dto.getBarcode().equals(product.getBarcode()) &&
            productRepository.existsByBarcode(dto.getBarcode())) {
            throw new RuntimeException("Product with this barcode already exists");
        }

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setBarcode(dto.getBarcode());
        product.setPrice(dto.getPrice());
        product.setCostPrice(dto.getCostPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setMinStockLevel(dto.getMinStockLevel());
        product.setIsActive(dto.getIsActive());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }

    @Override
    public Optional<Product> findById(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    public Optional<Product> findByBarcode(String barcode) {
        return productRepository.findByBarcode(barcode);
    }

    @Override
    public List<Product> findAllActiveProducts() {
        return productRepository.findByIsActiveTrue();
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> searchActiveProducts(String search) {
        return productRepository.searchActiveProducts(search);
    }

    @Override
    public List<Product> findLowStockProducts() {
        return productRepository.findLowStockProducts();
    }

    @Override
    public boolean existsByBarcode(String barcode) {
        return productRepository.existsByBarcode(barcode);
    }

    @Override
    public void reduceStock(Integer productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.reduceStock(quantity);
        productRepository.save(product);
    }

    @Override
    public void addStock(Integer productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.addStock(quantity);
        productRepository.save(product);
    }
}
