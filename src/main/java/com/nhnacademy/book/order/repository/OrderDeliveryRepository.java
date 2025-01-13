package com.nhnacademy.book.order.repository;

import com.nhnacademy.book.order.entity.OrderDelivery;
import com.nhnacademy.book.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderDeliveryRepository extends JpaRepository<OrderDelivery, String> {
    Optional<OrderDelivery> findByOrder(Orders order);
}
