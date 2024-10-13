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




@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/items")
public class ItemController {
    @Autowired
    public ItemService itemService;

    //add new item
    @PostMapping("/additem")
    public ResponseEntity<?> addItem(@RequestBody ItemDto itemDto) {
        itemService.saveItem(itemDto);
        
        return ResponseEntity.ok("Item added succefully");
        
    }

    //update an item
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Long id, @RequestBody ItemDto itemDto) {
        itemService.updateItem(id, itemDto);
        return ResponseEntity.ok("Item updated successfully");
    }

    //delete an item
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id){
        itemService.deleteItem(id);
        return ResponseEntity.ok("Item deleted susccessfully");
    }

    //get all item
    @GetMapping("/all")
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> items=itemService.getAllItems();
        return ResponseEntity.ok(items);
    }
    
    //Get item by id
    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        Item item=itemService.getItemById(id);
        return ResponseEntity.ok(item);
    }
    

    
}
