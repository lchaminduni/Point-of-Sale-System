package com.ijse.pos_system.controller;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ijse.pos_system.dto.StockDto;
import com.ijse.pos_system.entities.Stock;
import com.ijse.pos_system.services.StockService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/stocks")
@Api(value = "Stock Management System")
public class StockController {
    
    @Autowired
    private StockService stockService;

    @ApiOperation(value = "Add stock for an item", response = String.class)
    @PostMapping("/add")
    public ResponseEntity<?> addStock(@ApiParam(value = "Stock data", required = true) 
            @RequestBody StockDto stockDto) {
        stockService.saveStock(stockDto);
        return ResponseEntity.ok("Stock added successfully");
    }

    @ApiOperation(value = "Update stock for an item", response = String.class)
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStock( @ApiParam(value = "Stock ID", required = true) 
    @PathVariable Long id, 
    @ApiParam(value = "Updated stock data", required = true) 
    @RequestBody StockDto stockDto) {
        stockService.updateStock(id, stockDto);
        return ResponseEntity.ok("Stock updated successfully");
    }

    @ApiOperation(value = "Delete stock by ID", response = String.class)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteStock(
            @ApiParam(value = "Stock ID", required = true) 
            @PathVariable Long id) {
        stockService.deleteStock(id);
        return ResponseEntity.ok("Stock deleted successfully!");
    }

    @ApiOperation(value = "Get all stock", response = List.class)
    @GetMapping("/all")
    public ResponseEntity<List<Stock>> getAllStock() {
        List<Stock> stockList=stockService.getAllStock();
        return ResponseEntity.ok(stockList);
    }
    
    @ApiOperation(value = "Get stock by ID", response = Stock.class)
    @GetMapping("/{id}")
    public ResponseEntity<Stock> getStockById(@ApiParam(value = "Stock ID", required = true) 
    @PathVariable Long id) {
        Stock stockDto=stockService.getStockById(id);
        return ResponseEntity.ok(stockDto);
    }
    
}
