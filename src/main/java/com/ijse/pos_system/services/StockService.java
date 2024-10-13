package com.ijse.pos_system.services;

import java.util.List;

import com.ijse.pos_system.entities.Stock;

public interface StockService {
    Stock saveStock(Stock stock);
    Stock updateStock(Long id,Stock stock);
    Stock getStockById(Long id);
    void deleteStock(Long id);
    List<Stock> getAllStock(); 
} 
