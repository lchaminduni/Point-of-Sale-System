package com.ijse.pos_system.services;

import java.util.List;
import java.util.Map;

import com.ijse.pos_system.entities.Order;

public interface OrderService {
    Order createOrder(Order order, Map<Long, Integer> itemQuantities);
    boolean deleteOrder(Long orderId);
    List<Order> getAllOrders();
    Order getOrderById(Long id);
}
