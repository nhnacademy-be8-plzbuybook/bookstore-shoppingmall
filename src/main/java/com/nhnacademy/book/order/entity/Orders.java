package com.nhnacademy.book.order.entity;

import com.nhnacademy.book.converter.OrderStatusConverter;
import com.nhnacademy.book.order.dto.OrderUpdateRequestDto;
import com.nhnacademy.book.order.enums.OrderStatus;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Orders {
    @Id
    @Column(name = "order_id", length = 36, nullable = false)
    private String id;

    @Column(nullable = false, unique = true)
    private String number;

    @Column(nullable = false)
    private String name;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime orderedAt;

    private LocalDate deliveryWishDate;

    @Setter
    private Integer usedPoint;

    @Column(nullable = false)
    private BigDecimal deliveryFee;

    @Setter
    @Column(nullable = false)
    private BigDecimal orderPrice;

    @Convert(converter = OrderStatusConverter.class)
    @Column(nullable = false)
    private OrderStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts;

    @Builder
    public Orders(String id, String number, String name, LocalDate deliveryWishDate, Integer usedPoint,
                  BigDecimal deliveryFee, LocalDateTime orderedAt, OrderStatus status, BigDecimal orderPrice) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.deliveryFee = deliveryFee;
        this.orderedAt = orderedAt;
        this.deliveryWishDate = deliveryWishDate;
        this.usedPoint = usedPoint;
        this.status = status;
        this.orderPrice = orderPrice;
        this.orderProducts = new ArrayList<>();
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        orderProduct.setOrder(this);
        orderProducts.add(orderProduct);
    }

    public void update(OrderUpdateRequestDto updateRequest) {

        //TODO: 업데이트 로직
    }

    public void updateOrderStatus(OrderStatus statusCode) {
        this.status = statusCode;
    }
}
