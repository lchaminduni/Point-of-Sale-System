package com.ijse.pos_system.dto;


import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {
    
    //private List<Long> itemIds;
    private Map<Long, Integer> itemQuantities;
}
