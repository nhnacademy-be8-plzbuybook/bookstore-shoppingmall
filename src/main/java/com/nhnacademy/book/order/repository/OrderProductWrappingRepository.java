package com.nhnacademy.book.order.repository;

import com.nhnacademy.book.order.entity.OrderProductWrapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductWrappingRepository extends JpaRepository<OrderProductWrapping, Long> {
}
