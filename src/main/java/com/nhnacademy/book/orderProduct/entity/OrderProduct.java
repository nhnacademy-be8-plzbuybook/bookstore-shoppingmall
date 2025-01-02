package com.nhnacademy.book.orderProduct.entity;

import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.order.entity.Orders;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "order_product")
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id")
    private Long orderProductId;

    @OneToOne
    @JoinColumn(name = "op_selling_book_id", referencedColumnName = "sellingBookId") //TODO: 컬럼명 명시적으로 지정필요
    private SellingBook sellingBook;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "op_order_id", referencedColumnName = "order_id")
    private Orders order;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer quantity;

    private BigDecimal couponDiscount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private OrderProductStatus status;

    @Builder
    public OrderProduct(Long id, SellingBook sellingBook, Orders order, BigDecimal price, Integer quantity,
                        BigDecimal couponDiscount, OrderProductStatus status) {
        this.orderProductId = id;
        this.sellingBook = sellingBook;
        this.order = order;
        this.price = price;
        this.quantity = quantity;
        this.couponDiscount = couponDiscount;
        this.status = status;
    }
}
