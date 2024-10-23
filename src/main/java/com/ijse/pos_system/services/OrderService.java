package com.ijse.pos_system.services;

import java.util.List;


import com.ijse.pos_system.entities.Order;

public interface OrderService {
    Order createOrder(Order order);
    boolean deleteOrder(Long orderId);
    List<Order> getAllOrders();
    
}
