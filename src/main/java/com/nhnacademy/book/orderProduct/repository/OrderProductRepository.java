package com.nhnacademy.book.orderProduct.repository;

import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
