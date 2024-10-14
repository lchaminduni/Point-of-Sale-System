package com.ijse.pos_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import com.ijse.pos_system.dto.ItemDto;
import com.ijse.pos_system.entities.Item;
import com.ijse.pos_system.services.ItemService;
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
@RequestMapping("/items")
@Api(value = "Item Management System")
public class ItemController {
    @Autowired
    public ItemService itemService;

    //add new item
    @ApiOperation(value = "Add a new item", response = String.class)
    @PostMapping("/additem")
    public ResponseEntity<?> addItem(@ApiParam(value = "Item data", required = true) 
    @RequestBody ItemDto itemDto) {
        itemService.saveItem(itemDto);
        
        return ResponseEntity.ok("Item added succefully");
        
    }

    //update an item
    @ApiOperation(value = "Update an existing item", response = String.class)
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateItem(@ApiParam(value = "Item ID", required = true) 
    @PathVariable Long id, 
    @ApiParam(value = "Updated item data", required = true) 
    @RequestBody ItemDto itemDto) {
        itemService.updateItem(id, itemDto);
        return ResponseEntity.ok("Item updated successfully");
    }

    //delete an item
    @ApiOperation(value = "Delete an item by ID", response = String.class)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteItem(@ApiParam(value = "Item ID", required = true) 
    @PathVariable Long id){
        itemService.deleteItem(id);
        return ResponseEntity.ok("Item deleted susccessfully");
    }

    //get all item
    @ApiOperation(value = "Get all items", response = List.class)
    @GetMapping("/all")
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> items=itemService.getAllItems();
        return ResponseEntity.ok(items);
    }
    
    //Get item by id
    @ApiOperation(value = "Get an item by ID", response = Item.class)
    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@ApiParam(value = "Item ID", required = true) 
    @PathVariable Long id) {
        Item item=itemService.getItemById(id);
        return ResponseEntity.ok(item);
    }
    

    
}
