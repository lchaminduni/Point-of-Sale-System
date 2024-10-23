package com.ijse.pos_system.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ijse.pos_system.dto.ItemRequestDto;
import com.ijse.pos_system.dto.OrderDto;
import com.ijse.pos_system.dto.ResponseDto;
import com.ijse.pos_system.entities.Item;
import com.ijse.pos_system.entities.Order;
import com.ijse.pos_system.services.ItemService;
import com.ijse.pos_system.services.OrderService;
import com.ijse.pos_system.services.StockService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/orders")
@Api("Order Management System")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private StockService stockService;

    @ApiOperation(value = "Place an order for an item")
    @PostMapping("/add")
    public ResponseEntity<ResponseDto<Order>> createOrder(@ApiParam(value = "Order data", required = true) 
                                            @RequestBody OrderDto orderDto) {

        Order order=new Order();
        order.setTotalPrice(BigDecimal.valueOf(0.0));

        
        Map<Long, Integer> itemQuantities = orderDto.getItemQuantities(); // Get item quantities
        List<Item> orderedItems = new ArrayList<>();

        itemQuantities.forEach((itemId, quantity) -> {
            Item item = itemService.getItemById(itemId);
    

            if (item != null) {
                
                
                orderedItems.add(item);

                
                order.setTotalPrice(order.getTotalPrice().add(item.getPrice().multiply(BigDecimal.valueOf(quantity))));

            }
        });

        order.setOrderedItems(orderedItems);

        Order saveOrder=orderService.createOrder(order);
        ResponseDto<Order> responseDto=new ResponseDto<>();
        responseDto.setOrderId(saveOrder.getId());
        responseDto.setTotalPrice(saveOrder.getTotalPrice());
        responseDto.setItems(orderedItems.stream().map(item -> {
            return new ItemRequestDto(item.getName(), item.getPrice(), item.getQuantity(), 
                                       item.getCategory() != null ? item.getCategory().getId() : null);
        

        }).collect(Collectors.toList()));

        return ResponseEntity.ok(responseDto);
    }

    @ApiOperation(value = "Delete an order by ID")
    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<String> deleteOrder(@ApiParam(value = "Order ID to delete", required = true) 
                                             @PathVariable Long orderId) {
        boolean isDeleted = orderService.deleteOrder(orderId);
        
        if (isDeleted) {
            return ResponseEntity.ok("Order deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found.");
        }
    }

    @ApiOperation(value = "Get all orders")
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders(){
        List<Order> orders=orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
    
}
