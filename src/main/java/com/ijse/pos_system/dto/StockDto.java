package com.ijse.pos_system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StockDto {
    private Long id;
    private Integer quantity;
    private Long item_Id;  // This will store the ID of the associated Item

}
