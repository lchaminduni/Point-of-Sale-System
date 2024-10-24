package com.ijse.pos_system.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ijse.pos_system.dto.OrderDto;
import com.ijse.pos_system.dto.ResponseDto;
import com.ijse.pos_system.dto.StockDto;
import com.ijse.pos_system.entities.Item;
import com.ijse.pos_system.entities.Order;
import com.ijse.pos_system.entities.Stock;
import com.ijse.pos_system.services.ItemService;
import com.ijse.pos_system.services.OrderService;
import com.ijse.pos_system.services.StockService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

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
    public ResponseEntity<ResponseDto<?>> createOrder(@ApiParam(value = "Order data", required = true) 
                                            @RequestBody OrderDto orderDto) {

        try {
            // Convert OrderDto to Order entity
            Order order = new Order();
            order.setTotalPrice(BigDecimal.ZERO); 

            // Process item quantities
            Map<Long, Integer> itemQuantities = orderDto.getItemQuantities();
            List<Item> orderedItems = new ArrayList<>();

            for (Map.Entry<Long, Integer> entry : itemQuantities.entrySet()) {
                Long itemId = entry.getKey();
                Integer quantity = entry.getValue();

                // Fetch the item by ID
                Item item = itemService.getItemById(itemId);
                if (item == null) {
                    return ResponseEntity.badRequest().body(new ResponseDto<>(String.format("Item with ID %d not found.", itemId), null));
                }

                // Fetch stock by item ID and check availability
                Stock stock = stockService.getStockByItemId(itemId);
                if (stock == null || stock.getQuantity() < quantity) {
                    return ResponseEntity.badRequest()
                            .body(new ResponseDto<>(String.format("Insufficient stock for item ID: %d", itemId), null));
                }

                // Update stock
                stock.setQuantity(stock.getQuantity() - quantity);
                stockService.saveStock(new StockDto(stock.getId(), stock.getQuantity(), item.getId()));

                // Add the item to the order and update total price
                orderedItems.add(item);
                order.setTotalPrice(order.getTotalPrice().add(item.getPrice().multiply(BigDecimal.valueOf(quantity))));
            }

            // Set ordered items and save the order
            order.setOrderedItems(orderedItems);
            Order savedOrder = orderService.createOrder(order,itemQuantities);

            // Prepare the response
            ResponseDto<Order> response = new ResponseDto<>("Order created successfully.", savedOrder);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            ResponseDto<String> errorResponse = new ResponseDto<>(ex.getMessage(), null);
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            ResponseDto<String> errorResponse = new ResponseDto<>("An unexpected error occurred.", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
}

    @ApiOperation(value = "Get all orders")
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders(){
        List<Order> orders=orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
}
