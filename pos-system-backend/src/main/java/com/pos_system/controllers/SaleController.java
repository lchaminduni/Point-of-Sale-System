package com.pos_system.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.pos_system.dto.CreateSaleRequest;
import com.pos_system.entities.Sale;
import com.pos_system.services.SaleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins = "*")
public class SaleController {
    @Autowired
    private SaleService saleService;

    @PostMapping
    @PreAuthorize("hasRole('CASHIER') or hasRole('ADMIN')")
    public ResponseEntity<?> createSale(@Valid @RequestBody CreateSaleRequest request) {
        try {
            Sale sale = saleService.createSale(request);
            return ResponseEntity.ok(sale);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSaleById(@PathVariable Integer id) {
        return saleService.findById(id)
            .<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(404).body(Map.of("message", "Sale not found")));

    }

    @GetMapping("/sale-number/{number}")
    public ResponseEntity<?> getBySaleNumber(@PathVariable String number) {
        return saleService.findBySaleNumber(number)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body(Map.of("message", "Sale not found")));
    }

    @PostMapping("/{saleId}/refund")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> refundSale(@PathVariable Integer saleId, @RequestParam String reason) {
        try {
            Sale refunded = saleService.refundSale(saleId, reason);
            return ResponseEntity.ok(refunded);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/report/revenue")
    public ResponseEntity<?> getRevenueReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX); // 23:59:59.999999

        Map<String, Object> report = new HashMap<>();
        report.put("totalRevenue", saleService.getTotalRevenueBetweenDates(startDateTime, endDateTime));
        report.put("avgOrderValue", saleService.getAverageOrderValueBetweenDates(startDateTime, endDateTime));
        report.put("totalSales", saleService.countSalesBetweenDates(startDateTime, endDateTime));
        return ResponseEntity.ok(report);
    }

    @GetMapping("/report/cashier-performance")
public ResponseEntity<?> getCashierPerformance(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
) {
    LocalDateTime startDateTime = start.atStartOfDay();
    LocalDateTime endDateTime = end.atTime(LocalTime.MAX); // 23:59:59.999999

    return ResponseEntity.ok(saleService.getCashierPerformance(startDateTime, endDateTime));
}

}
