package com.ijse.pos_system.services;

import com.ijse.pos_system.dto.InvoiceDto;

public interface InvoiceService {
    InvoiceDto generateInvoice(Long orderId);
}
