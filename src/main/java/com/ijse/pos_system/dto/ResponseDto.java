package com.ijse.pos_system.dto;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class ResponseDto <T>  {
    private String message;
    private T data;
}
