package com.nhnacademy.book.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class NonMemberOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "non_member_order_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "nmo_order_id", referencedColumnName = "order_id")
    private Orders order;

    private String password;

    public NonMemberOrder(Orders order, String password) {
        this.order = order;
        this.password = password;
    }
}
