package com.nhnacademy.book.orderProduct.repository;

import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
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


    @Query("SELECT m.memberId AS memberId, SUM(op.price * op.quantity) AS totalAmount " +
            "FROM OrderProduct op " +
            "JOIN op.order o " +
            "JOIN MemberOrder mo ON mo.order = o " +
            "JOIN mo.member m " +
            "WHERE op.status = com.nhnacademy.book.orderProduct.entity.OrderProductStatus.PURCHASE_CONFIRMED " +
            "AND o.orderedAt >= :threeMonthsAgo " +
            "GROUP BY m.memberId")
    List<Object[]> findTotalAmountByMemberAndRecentOrders(LocalDateTime threeMonthsAgo);
}
