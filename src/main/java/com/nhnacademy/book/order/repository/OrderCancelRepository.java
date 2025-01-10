package com.nhnacademy.book.order.repository;

import com.nhnacademy.book.order.entity.OrderCancel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderCancelRepository extends JpaRepository<OrderCancel, Long> {
}
