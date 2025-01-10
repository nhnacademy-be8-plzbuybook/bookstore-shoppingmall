package com.nhnacademy.book.orderProduct.repository;

import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    Optional<OrderProduct> findBySellingBook_SellingBookId(Long sellingBookId);
    Optional<List<OrderProduct>> findByOrderId(String orderId);
}
