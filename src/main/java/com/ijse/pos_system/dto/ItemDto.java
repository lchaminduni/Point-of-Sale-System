package com.ijse.pos_system.dto;

import java.math.BigDecimal;

import jakarta.persistence.Column;
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
    private BigDecimal price;  
    private Integer quantity;

    @Column(name = "category_id")
    private Long categoryId;  
    
}
