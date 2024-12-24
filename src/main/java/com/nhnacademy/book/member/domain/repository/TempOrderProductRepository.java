package com.nhnacademy.book.member.domain.repository;

import com.nhnacademy.book.member.domain.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TempOrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
