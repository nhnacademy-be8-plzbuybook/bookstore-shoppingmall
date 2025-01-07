package com.nhnacademy.book.order.repository;

import com.nhnacademy.book.order.entity.OrderDelivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDeliveryRepository extends JpaRepository<OrderDelivery, String> {
}
