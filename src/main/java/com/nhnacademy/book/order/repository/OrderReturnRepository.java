package com.nhnacademy.book.order.repository;

import com.nhnacademy.book.order.entity.OrderReturn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderReturnRepository extends JpaRepository<OrderReturn, Long> {
    Optional<OrderReturn> findByOrderId(String orderId);
}
