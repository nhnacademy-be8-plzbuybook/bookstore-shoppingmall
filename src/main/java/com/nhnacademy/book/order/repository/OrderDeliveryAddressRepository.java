package com.nhnacademy.book.order.repository;

import com.nhnacademy.book.order.entity.OrderDeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDeliveryAddressRepository extends JpaRepository<OrderDeliveryAddress, Long> {
}
