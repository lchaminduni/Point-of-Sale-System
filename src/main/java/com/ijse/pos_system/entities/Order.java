package com.ijse.pos_system.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_date", updatable = false)
    private LocalDateTime orderedDateTime;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @PrePersist
    protected void onCreate(){
        if (this.orderedDateTime==null) {
            this.orderedDateTime=LocalDateTime.now();
        }
    }

    @ManyToMany
    @JoinTable(name = "order_items", 
    joinColumns = @JoinColumn(name = "order_id"),
    inverseJoinColumns = @JoinColumn(name = "item_id")

    )
    private List<Item> orderedItems;
}
