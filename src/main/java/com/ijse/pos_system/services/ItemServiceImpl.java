package com.ijse.pos_system.services;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ijse.pos_system.dto.ItemDto;
import com.ijse.pos_system.entities.Item;
import com.ijse.pos_system.entities.ItemCategory;
import com.ijse.pos_system.repository.ItemCategoryRepository;
import com.ijse.pos_system.repository.ItemRepository;
@Service
public class ItemServiceImpl implements ItemService{
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemCategoryRepository categoryRepository;

    @Override
    public Item saveItem (ItemDto itemDto) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setPrice(itemDto.getPrice());
        item.setQuantity(itemDto.getQuantity());
        if (itemDto.getCategoryId() != null) {
            ItemCategory category = categoryRepository.findById(itemDto.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + itemDto.getCategoryId()));
            item.setCategory(category);
        }
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(Long id, ItemDto itemDto) {
        // TODO Auto-generated method stub
        Item existingItem=itemRepository.findById(id).orElse(null);
        existingItem.setName(itemDto.getName());
        existingItem.setPrice(itemDto.getPrice());
        if (itemDto.getCategoryId() != null) {
            ItemCategory category = categoryRepository.findById(itemDto.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + itemDto.getCategoryId()));
            existingItem.setCategory(category);
        }
        return itemRepository.save(existingItem);
    }

    @Override
    public void deleteItem(Long id) {
        // TODO Auto-generated method stub
        itemRepository.deleteById(id);
    }

    @Override
    public List<Item> getAllItems() {
        // TODO Auto-generated method stub
        return itemRepository.findAll();
    }

    @Override
    public Item getItemById(Long id) {
        // TODO Auto-generated method stub
        return itemRepository.findById(id).orElse(null);
    }

    
}
