package com.nhnacademy.book.wrappingPaper.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class WrappingPaper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wrapping_paper_id", nullable = false)
    private Long id;

    @Column(length = 20, nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Long stock;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    private String imagePath;

    public WrappingPaper(String name, BigDecimal price, Long stock, LocalDateTime createdAt, String imagePath) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.createdAt = createdAt;
        this.imagePath = imagePath;
    }

    public void update(String name, BigDecimal price, Long stock, String imagePath) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.imagePath = imagePath;
    }
}
