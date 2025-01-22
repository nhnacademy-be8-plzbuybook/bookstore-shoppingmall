package com.nhnacademy.book.order.repository;

import com.nhnacademy.book.order.entity.MemberOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberOrderRepository extends JpaRepository<MemberOrder, Long> {
//    Optional<MemberOrder> findByOrder_IdAndMember_memberId(String orderId, Long memberId);
}
