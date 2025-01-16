package com.nhnacademy.book.order.repository;

import com.nhnacademy.book.order.entity.OrderProductCancel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductCancelRepository extends JpaRepository<OrderProductCancel, Long> {
}
