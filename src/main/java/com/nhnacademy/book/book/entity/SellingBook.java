package com.nhnacademy.book.book.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "selling_book")
@Getter
@Setter
@NoArgsConstructor
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

    enum SellingBookStatus {
        판매중,
        판매종료,
        삭제도서
    }
}

