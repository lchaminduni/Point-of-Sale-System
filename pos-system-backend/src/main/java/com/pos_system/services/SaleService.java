package com.pos_system.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pos_system.dto.CreateSaleRequest;
import com.pos_system.entities.Sale;
import com.pos_system.entities.User;


public interface SaleService {
    Sale createSale(CreateSaleRequest request);

    Optional<Sale> findById(Integer id);

    Optional<Sale> findBySaleNumber(String saleNumber);

    Page<Sale> findAllSales(Pageable pageable);

    Page<Sale> findSalesByCashier(User cashier, Pageable pageable);

    Page<Sale> findSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<Sale> findSalesByCashierAndDateRange(User cashier, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Sale refundSale(Integer saleId, String reason);

    Long countSalesBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    Double getTotalRevenueBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    Double getAverageOrderValueBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    List<Object[]> getCashierPerformance(LocalDateTime startDate, LocalDateTime endDate);

    List<Object[]> getDailySalesData(LocalDateTime startDate, LocalDateTime endDate);

    List<Object[]> getWeeklySalesData(LocalDateTime startDate, LocalDateTime endDate);

    List<Object[]> getMonthlySalesData(LocalDateTime startDate, LocalDateTime endDate);

    List<Sale> getAllSales();

    byte[] generateSaleReceiptPdf(Integer saleId);

    
}
