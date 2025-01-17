package com.nhnacademy.book.order.repository;

import com.nhnacademy.book.order.entity.OrderProductReturn;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderProductReturnRepository extends JpaRepository<OrderProductReturn, Long> {
    Optional<OrderProductReturn> findByOrderProduct(OrderProduct orderProduct);
}
