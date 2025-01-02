package com.nhnacademy.book.order.repository;

import com.nhnacademy.book.order.entity.NonMemberOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NonMemberOrderRepository extends JpaRepository<NonMemberOrder, Long> {
}
