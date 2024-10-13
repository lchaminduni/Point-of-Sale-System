package com.ijse.pos_system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private Double price;  
    private Integer quantity;

    private Long categoryId;  // We use the category ID to avoid transferring the whole entity
    
}
