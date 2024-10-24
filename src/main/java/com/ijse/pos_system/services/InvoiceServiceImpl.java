package com.ijse.pos_system.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ijse.pos_system.dto.InvoiceDto;
import com.ijse.pos_system.dto.ItemDto;
import com.ijse.pos_system.entities.Order;
import com.ijse.pos_system.repository.OrderRepository;

@Service
public class InvoiceServiceImpl implements InvoiceService{

    @Autowired
    private OrderRepository orderRepository;

    public InvoiceDto generateInvoice(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found for ID: " + orderId));

        // Create the InvoiceDto
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setOrderId(order.getId());
        invoiceDto.setTotalAmount(order.getTotalPrice());
        invoiceDto.setOrderDate(order.getOrderedDateTime());
        
        List<ItemDto> itemDtos = order.getOrderedItems().stream()
                .map(item -> {
                    ItemDto itemDto = new ItemDto();
                    itemDto.setId(item.getId());
                    itemDto.setName(item.getName());
                    itemDto.setPrice(item.getPrice());
                    itemDto.setQuantity(item.getQuantity()); 
                    itemDto.setCategoryId(item.getCategory().getId());
                    return itemDto;
                })
                .collect(Collectors.toList());

        invoiceDto.setOrderedItems(itemDtos); 

    
        return invoiceDto;
    }
}
