package com.nhnacademy.book.order.repository;

import com.nhnacademy.book.order.dto.orderRequests.OrderDeliveryAddressDto;
import com.nhnacademy.book.order.entity.OrderDeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderDeliveryAddressRepository extends JpaRepository<OrderDeliveryAddress, Long> {
    Optional<OrderDeliveryAddressDto> findByOrder_Id(String orderId);

}
