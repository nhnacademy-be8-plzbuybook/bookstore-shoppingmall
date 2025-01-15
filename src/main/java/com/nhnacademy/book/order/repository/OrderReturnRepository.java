package com.nhnacademy.book.order.repository;

import com.nhnacademy.book.order.dto.OrderReturnDto;
import com.nhnacademy.book.order.entity.OrderReturn;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderReturnRepository extends JpaRepository<OrderReturn, Long> {
    Optional<OrderReturn> findByOrderId(String orderId);

    @Query("SELECT new com.nhnacademy.book.order.dto.OrderReturnDto(o.id, o.reason, o.trackingNumber, o.requestedAt, o.completedAt, o.order.id) " +
            "FROM OrderReturn o WHERE o.trackingNumber = :trackingNumber")
    Optional<OrderReturnDto> findByTrackingNumber(@Param("trackingNumber") String trackingNumber);

}
