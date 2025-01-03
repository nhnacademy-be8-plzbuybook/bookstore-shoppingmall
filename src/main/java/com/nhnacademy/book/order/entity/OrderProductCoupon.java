package com.nhnacademy.book.order.entity;

import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class OrderProductCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_coupon_id")
    private Long id;

    private Long couponId;
    @ManyToOne
    @JoinColumn(name = "opc_order_product_id", referencedColumnName = "order_product_id")
    private OrderProduct orderProduct;

    public OrderProductCoupon(Long couponId, OrderProduct orderProduct) {
        this.couponId = couponId;
        this.orderProduct = orderProduct;
    }
}
