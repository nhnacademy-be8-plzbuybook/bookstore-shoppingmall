package com.nhnacademy.book.order.entity;

import com.nhnacademy.book.payment.entity.Payment;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Orders {
    @Id
    @Column(name = "order_id", nullable = false)
    private String id;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "orders")
    private Payment payment;

}
