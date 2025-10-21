package com.pos_system.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pos_system.entities.Sale;
import com.pos_system.entities.User;

@Repository
public interface SaleRepository extends JpaRepository<Sale,Integer>{
    Optional<Sale> findBySaleNumber(String saleNumber);

    boolean existsBySaleNumber(String saleNumber);

    Page<Sale> findByCashier(User cashier, Pageable pageable);

    Page<Sale> findBySaleDateBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Sale> findByCashierAndSaleDateBetween(User cashier, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.saleDate BETWEEN :start AND :end")
    Long countSalesBetweenDates(LocalDateTime start, LocalDateTime end);

    @Query("SELECT SUM(s.totalAmount) FROM Sale s WHERE s.saleDate BETWEEN :start AND :end")
    Double getTotalRevenueBetweenDates(LocalDateTime start, LocalDateTime end);

    @Query("SELECT AVG(s.totalAmount) FROM Sale s WHERE s.saleDate BETWEEN :start AND :end")
    Double getAverageOrderValueBetweenDates(LocalDateTime start, LocalDateTime end);

    @Query("SELECT s.cashier.username, COUNT(s), SUM(s.totalAmount) FROM Sale s WHERE s.saleDate BETWEEN :start AND :end GROUP BY s.cashier.username")
    java.util.List<Object[]> getCashierPerformance(LocalDateTime start, LocalDateTime end);

    @Query("SELECT FUNCTION('DATE', s.saleDate), SUM(s.totalAmount) FROM Sale s WHERE s.saleDate BETWEEN :start AND :end GROUP BY FUNCTION('DATE', s.saleDate)")
    java.util.List<Object[]> getDailySalesData(LocalDateTime start, LocalDateTime end);

    @Query("SELECT FUNCTION('YEARWEEK', s.saleDate), SUM(s.totalAmount) " +
       "FROM Sale s WHERE s.saleDate BETWEEN :start AND :end " +
       "GROUP BY FUNCTION('YEARWEEK', s.saleDate)")
    List<Object[]> getWeeklySalesData(LocalDateTime start, LocalDateTime end);

    @Query("SELECT FUNCTION('MONTH', s.saleDate), SUM(s.totalAmount) " +
       "FROM Sale s WHERE s.saleDate BETWEEN :start AND :end " +
       "GROUP BY FUNCTION('MONTH', s.saleDate)")
    List<Object[]> getMonthlySalesData(LocalDateTime start, LocalDateTime end);

    @Query("SELECT p.category.name AS category, SUM(si.quantity) AS totalSales " +
       "FROM SaleItem si JOIN si.product p " +
       "GROUP BY p.category.name")
    List<Object[]> getSalesByCategory();


}
