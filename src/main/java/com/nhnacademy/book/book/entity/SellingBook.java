package com.nhnacademy.book.book.entity;

import jakarta.persistence.*;
import jakarta.ws.rs.DELETE;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "selling_book")
@Getter
@Setter
@AllArgsConstructor
public class SellingBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sellingBookId;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal sellingBookPrice;

    @Column(nullable = false)
    private Boolean sellingBookPackageable;

    @Column(name = "stock", nullable = false)
    private Integer sellingBookStock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SellingBookStatus sellingBookStatus;

    @Column(nullable = false)
    private Boolean used;

    @Column(nullable = false)
    private Long sellingBookViewCount;

    public enum SellingBookStatus {
        SELLING,
        SELLEND,
        DELETEBOOK

    }
    public SellingBook() {
    }
}

