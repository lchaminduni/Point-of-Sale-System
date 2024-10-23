package com.ijse.pos_system.services;

import java.util.List;

import com.ijse.pos_system.dto.StockDto;
import com.ijse.pos_system.entities.Stock;

public interface StockService {
    Stock saveStock(StockDto stockDto);
    Stock updateStock(Long id,StockDto stockDto);
    Stock getStockById(Long id);
    void deleteStock(Long id);
    List<Stock> getAllStock();
    //new adding
    //void updateStockAfterOrder(Long itemId,Integer quantity);
    //void updateStockAfterDelete(Long itemId,Integer quantity); 
    //Stock findStockByItemId(Long itemId);

} 
