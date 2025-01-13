package com.nhnacademy.book.orderProduct.repository;

import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    Optional<OrderProduct> findBySellingBook_SellingBookId(Long sellingBookId);
    List<OrderProduct> findByOrderId(String orderId);
    @Query("SELECT op FROM OrderProduct op " +
            "JOIN op.order o " +
            "JOIN MemberOrder mo ON mo.order = o " +
            "WHERE op.sellingBook.sellingBookId = :sellingBookId " +
            "AND mo.member.memberId = :memberId ")
    List<OrderProduct> findBySellingBookIdANdMemberId(Long sellingBookId, Long memberId);
}
