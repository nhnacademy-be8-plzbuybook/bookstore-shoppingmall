package com.nhnacademy.book.order.repository;

import com.nhnacademy.book.order.entity.OrderProductCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductCouponRepository extends JpaRepository<OrderProductCoupon, Long> {
}
