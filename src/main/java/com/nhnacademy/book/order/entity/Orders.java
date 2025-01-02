//package com.nhnacademy.book.order.entity;
//
//import com.nhnacademy.book.order.dto.OrderUpdateRequestDto;
//import com.nhnacademy.book.order.dto.orderRequests.OrderProductAppliedCouponDto;
//import com.nhnacademy.book.order.dto.orderRequests.OrderProductRequestDto;
//import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
//import com.nhnacademy.book.order.enums.OrderStatus;
//import com.nhnacademy.book.orderProduct.entity.OrderProduct;
//import jakarta.persistence.*;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@NoArgsConstructor
//@Getter
//@Entity
//public class Orders {
//    @Id
//    @Column(name = "order_id", nullable = false)
//    private String id;
//    private String number;
//    private String name;
//    private LocalDateTime orderedAt;
//    private LocalDate deliveryWishDate;
//    private Integer usedPoint;
//    private BigDecimal orderPrice;
//    private BigDecimal couponDiscount;
//    @Enumerated(EnumType.STRING)
//    private OrderStatus status;
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL)
//    private List<OrderProduct> orderProducts;
//
//    @Builder
//    public Orders(String id, String number, String name, LocalDate deliveryWishDate, Integer usedPoint,
//                  LocalDateTime orderedAt, BigDecimal orderPrice, OrderStatus status, BigDecimal couponDiscount) {
//        this.id = id;
//        this.number = number;
//        this.name = name;
//        this.orderedAt = orderedAt;
//        this.deliveryWishDate = deliveryWishDate;
//        this.usedPoint = usedPoint;
//        this.orderPrice = orderPrice;
//        this.status = status;
//        this.couponDiscount = couponDiscount;
//        this.orderProducts = new ArrayList<>();
//    }
//
//    public void addOrderProduct(OrderProduct orderProduct) {
//        orderProduct.setOrder(this);
//        orderProducts.add(orderProduct);
//    }
//
//    public void update(OrderUpdateRequestDto updateRequest) {
//
//        //TODO: 업데이트 로직
//    }
//
//    public void updateOrderStatus(OrderStatus status) {
//        this.status = status;
//    }
//
//    public BigDecimal getPaymentPrice() {
//        return orderPrice.subtract(BigDecimal.valueOf(usedPoint)).subtract(couponDiscount);
//    }
//}
package com.nhnacademy.book.order.entity;

import com.nhnacademy.book.order.dto.OrderUpdateRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductAppliedCouponDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.order.enums.OrderStatus;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column(name = "order_id", nullable = false)
    private String id;
    private String number;
    private String name;
    private LocalDateTime orderedAt;
    private LocalDate deliveryWishDate;
    private Integer usedPoint;
    @Setter
    private BigDecimal orderPrice;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts;

    @Builder
    public Orders(String id, String number, String name, LocalDate deliveryWishDate, Integer usedPoint,
                  LocalDateTime orderedAt, OrderStatus status, BigDecimal orderPrice) {
        this.id = id;
        this.number = number;
        this.name = name;
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

    public void updateOrderStatus(OrderStatus status) {
        this.status = status;
    }
}
