package com.nhnacademy.book.order.repository;

import com.nhnacademy.book.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, String> {
}
