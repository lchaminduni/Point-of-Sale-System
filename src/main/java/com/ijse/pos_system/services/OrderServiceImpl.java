package com.ijse.pos_system.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ijse.pos_system.dto.StockDto;
import com.ijse.pos_system.entities.Item;
import com.ijse.pos_system.entities.Order;
import com.ijse.pos_system.entities.Stock;
import com.ijse.pos_system.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StockService stockService;

    
    @Override
    public Order createOrder(Order order, Map<Long, Integer> itemQuantities) {
        for (Item item : order.getOrderedItems()) {
            Long itemId=item.getId();
            Integer orderedQuantity = itemQuantities.get(itemId);  

            if (orderedQuantity == null) {
                throw new IllegalArgumentException("Ordered quantity for item: " + item.getName() + " is missing.");
            }
            
            // Find stock by item ID
            Stock stock = stockService.getStockByItemId(item.getId());
            
            if (stock != null && stock.getQuantity() >= orderedQuantity) {

                System.out.println("Stock available: " + stock.getQuantity() + " for item: " + item.getName());
                System.out.println("Ordered quantity: " + orderedQuantity);
                // Update stock quantity
                stock.setQuantity(stock.getQuantity() - orderedQuantity);
                stockService.saveStock(new StockDto(stock.getId(), stock.getQuantity(), item.getId()));
            } else {
                throw new IllegalArgumentException("Not enough stock for item: " + item.getName());
            }
        }
        return orderRepository.save(order);
        
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public boolean deleteOrder(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        
        if (orderOptional.isPresent()) {
           
            orderRepository.delete(orderOptional.get()); 
            return true;
        }
        return false;
    }

}
