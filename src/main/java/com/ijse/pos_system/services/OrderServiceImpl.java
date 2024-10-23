package com.ijse.pos_system.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ijse.pos_system.entities.Order;

import com.ijse.pos_system.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderRepository orderRepository;

    
     

    
    @Override
    public Order createOrder(Order order) {
        order.getOrderedItems().forEach(item -> {
            Integer quantity = item.getQuantity();  // Assuming item.getQuantity() gives the quantity ordered
            
        });
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
           
            orderRepository.delete(orderOptional.get()); // Delete the order
            return true;
        }
        return false;
    }

    
    
}
