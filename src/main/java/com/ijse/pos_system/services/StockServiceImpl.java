package com.ijse.pos_system.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ijse.pos_system.dto.StockDto;
import com.ijse.pos_system.entities.Item;
import com.ijse.pos_system.entities.Stock;
import com.ijse.pos_system.repository.ItemRepository;
import com.ijse.pos_system.repository.StockRepository;

@Service
public class StockServiceImpl implements StockService{

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ItemRepository itemRepository;


    @Override
    public Stock getStockById(Long id) {
        return stockRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }

    @Override
    public List<Stock> getAllStock() {
        return stockRepository.findAll();
    }

    @Override
    public Stock saveStock(StockDto stockDto) {
        Stock stock = new Stock();
        stock.setId(stockDto.getId());
        stock.setQuantity(stockDto.getQuantity());
        
        if (stockDto.getItem_Id() != null) {
            Item item = itemRepository.findById(stockDto.getItem_Id())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid item ID: " + stockDto.getItem_Id()));
            stock.setItem(item);
        }
        
        return stockRepository.save(stock);
    }

    @Override
    public Stock updateStock(Long id, StockDto stockDto) {
        Stock existingStock = stockRepository.findById(id).orElse(null);

        if (existingStock != null) {
            existingStock.setQuantity(stockDto.getQuantity());

            if (stockDto.getItem_Id() != null) {
                Item item = itemRepository.findById(stockDto.getItem_Id())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid item ID: " + stockDto.getItem_Id()));
                existingStock.setItem(item);
            }

            return stockRepository.save(existingStock);
        } else {
            throw new IllegalArgumentException("Stock not found with ID: " + id);
        }
    }

    @Override
    public Stock getStockByItemId(Long itemId) {
        return stockRepository.findByItemId(itemId);
    }

}
