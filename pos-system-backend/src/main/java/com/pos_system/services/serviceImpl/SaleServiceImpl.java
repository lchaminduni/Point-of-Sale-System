package com.pos_system.services.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pos_system.dto.CreateSaleRequest;
import com.pos_system.entities.PaymentStatus;
import com.pos_system.entities.Product;
import com.pos_system.entities.Sale;
import com.pos_system.entities.SaleItem;
import com.pos_system.entities.User;
import com.pos_system.repositories.ProductRepository;
import com.pos_system.repositories.SaleRepository;
import com.pos_system.repositories.UserRepository;
import com.pos_system.services.ProductService;
import com.pos_system.services.SaleService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService{
    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final UserRepository userRepository;

    @Override
    public Sale createSale(CreateSaleRequest request) {
        //Get the currently authenticated username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        //Fetch the cashier entity from your database
        User cashier = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Cashier not found: " + username));

        // Create a new Sale object
        Sale sale = new Sale();
        sale.setSaleNumber(generateSaleNumber());
        sale.setCashier(cashier);
        sale.setCustomerName(request.getCustomerName());
        sale.setCustomerPhone(request.getCustomerPhone());
        sale.setPaymentMethod(request.getPaymentMethod());
        sale.setDiscountAmount(request.getDiscountAmount() != null ? request.getDiscountAmount() : BigDecimal.ZERO);
        sale.setNotes(request.getNotes());

        BigDecimal subtotal = BigDecimal.ZERO;

        // Process each sale item
        for (CreateSaleRequest.SaleItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemRequest.getProductId()));

            // Stock check
            if (product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            // Create SaleItem
            SaleItem saleItem = new SaleItem();
            saleItem.setProduct(product);
            saleItem.setQuantity(itemRequest.getQuantity());
            saleItem.setUnitPrice(itemRequest.getUnitPrice());
            saleItem.calculateTotalPrice();
            sale.addSaleItem(saleItem);

            subtotal = subtotal.add(saleItem.getTotalPrice());

            // Reduce stock
            productService.reduceStock(product.getId(), itemRequest.getQuantity());
        }

        //Tax (8.5%)
        BigDecimal taxRate = new BigDecimal("0.085");
        BigDecimal taxAmount = subtotal.multiply(taxRate);

        // Calculate total
        BigDecimal totalAmount = subtotal.add(taxAmount).subtract(sale.getDiscountAmount());

        sale.setSubtotal(subtotal);
        sale.setTaxAmount(taxAmount);
        sale.setTotalAmount(totalAmount);
        sale.setSaleDate(LocalDateTime.now());
        sale.setPaymentStatus(PaymentStatus.COMPLETED);

        return saleRepository.save(sale);
    }

    @Override
    public Optional<Sale> findById(Integer id) {
        return saleRepository.findById(id);
    }

    @Override
    public Optional<Sale> findBySaleNumber(String saleNumber) {
        return saleRepository.findBySaleNumber(saleNumber);
    }

    @Override
    public Page<Sale> findAllSales(Pageable pageable) {
        return saleRepository.findAll(pageable);
    }

    @Override
    public Page<Sale> findSalesByCashier(User cashier, Pageable pageable) {
        return saleRepository.findByCashier(cashier, pageable);
    }

    @Override
    public Page<Sale> findSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return saleRepository.findBySaleDateBetween(startDate, endDate, pageable);
    }

    @Override
    public Page<Sale> findSalesByCashierAndDateRange(User cashier, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return saleRepository.findByCashierAndSaleDateBetween(cashier, startDate, endDate, pageable);
    }

    @Override
    public Sale refundSale(Integer saleId, String reason) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        if (sale.getPaymentStatus() == PaymentStatus.REFUNDED) {
            throw new RuntimeException("Sale is already refunded");
        }

        // Restore stock
        for (SaleItem item : sale.getSaleItems()) {
            productService.addStock(item.getProduct().getId(), item.getQuantity());
        }

        sale.setPaymentStatus(PaymentStatus.REFUNDED);
        sale.setNotes((sale.getNotes() != null ? sale.getNotes() + "\n" : "") + "Refund Reason: " + reason);

        return saleRepository.save(sale);
    }

    @Override
    public Long countSalesBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.countSalesBetweenDates(startDate, endDate);
    }

    @Override
    public Double getTotalRevenueBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        Double revenue = saleRepository.getTotalRevenueBetweenDates(startDate, endDate);
        return revenue != null ? revenue : 0.0;         
    }

    @Override
    public Double getAverageOrderValueBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        Double avgValue = saleRepository.getAverageOrderValueBetweenDates(startDate, endDate);
        return avgValue != null ? avgValue : 0.0;
    }

    @Override
    public List<Object[]> getCashierPerformance(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.getCashierPerformance(startDate, endDate);
    }

    @Override
    public List<Object[]> getDailySalesData(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.getDailySalesData(startDate, endDate);
    }

    @Override
    public List<Object[]> getWeeklySalesData(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.getWeeklySalesData(startDate, endDate);
    }

    @Override
    public List<Object[]> getMonthlySalesData(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.getMonthlySalesData(startDate, endDate);
    }

    // Generate unique sale number
    private String generateSaleNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String base = "SALE-" + timestamp;

        int counter = 1;
        String saleNumber = base;

        while (saleRepository.existsBySaleNumber(saleNumber)) {
            saleNumber = base + "-" + counter++;
        }

        return saleNumber;
    }
}
