package com.nhnacademy.book.order.repository;

import com.nhnacademy.book.order.entity.MemberOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberOrderRepository extends JpaRepository<MemberOrder, Long> {
    List<MemberOrder> findByOrder_IdAndMember_memberId(String orderId, Long memberId);
}
