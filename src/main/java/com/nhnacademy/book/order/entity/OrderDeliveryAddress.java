package com.nhnacademy.book.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class OrderDeliveryAddress {
    @Id
    @GeneratedValue
    @Column(name = "order_delivery_address_id ")
    private Long id;
    private String locationAddress;
    private String zipCode;
    private String detailAddress;
    private String recipient;
    private String recipientPhone;
    @OneToOne
    @JoinColumn(name = "oda_order_id", referencedColumnName = "order_id")
    private Orders order;

    public OrderDeliveryAddress(String locationAddress, String zipCode, String detailAddress, String recipient,
                                String recipientPhone, Orders order) {
        this.locationAddress = locationAddress;
        this.zipCode = zipCode;
        this.detailAddress = detailAddress;
        this.recipient = recipient;
        this.recipientPhone = recipientPhone;
        this.order = order;
    }
}
