package com.ijse.pos_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ijse.pos_system.dto.InvoiceDto;
import com.ijse.pos_system.dto.ResponseDto;
import com.ijse.pos_system.services.InvoiceService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/invoice")
@Api(tags = "Invoice Management")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;

    @ApiOperation(value = "Generate Invoice for an Order")
    @GetMapping("/{orderId}")
    public ResponseEntity<ResponseDto<InvoiceDto>> getInvoice(@ApiParam(value = "ID of the order to generate the invoice for", required = true) 
            @PathVariable Long orderId) {
        try {
            // Generate invoice for the order
            InvoiceDto invoice = invoiceService.generateInvoice(orderId);
            return ResponseEntity.ok(new ResponseDto<>("Invoice generated successfully", invoice));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new ResponseDto<>(ex.getMessage(), null));
        }
    }
}
