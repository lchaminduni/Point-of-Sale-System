package com.ijse.pos_system.services;

import java.util.List;

import com.ijse.pos_system.dto.ItemDto;
import com.ijse.pos_system.entities.Item;

public interface ItemService {
    Item saveItem (ItemDto itemDto);
    Item updateItem(Long id, ItemDto itemDto);
    void deleteItem(Long id);
    List<Item> getAllItems();
    Item getItemById(Long id);
}
   