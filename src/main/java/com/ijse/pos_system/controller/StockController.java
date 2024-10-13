package com.ijse.pos_system.controller;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.ijse.pos_system.entities.Stock;
import com.ijse.pos_system.services.StockService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;




@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/stocks")
public class StockController {
    
    @Autowired
    private StockService stockService;

    @PostMapping("/add")
    public ResponseEntity<?> addStock(@RequestBody Stock stock) {
        stockService.saveStock(stock);
        return ResponseEntity.ok("Stock added successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStock(@PathVariable Long id, @RequestBody Stock stock) {
        stockService.updateStock(id, stock);
        return ResponseEntity.ok("Stock updated successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Stock>> getAllStock() {
        List<Stock> stockList=stockService.getAllStock();
        return ResponseEntity.ok(stockList);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Stock> getStockById(@PathVariable Long id) {
        Stock stockDto=stockService.getStockById(id);
        return ResponseEntity.ok(stockDto);
    }
    
}
