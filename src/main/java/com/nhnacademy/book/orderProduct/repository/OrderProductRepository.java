package com.nhnacademy.book.orderProduct.repository;

import com.nhnacademy.book.order.entity.OrderProductReturn;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
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

    @Query("SELECT SUM(op.price * op.quantity) " +
            "FROM OrderProduct op " +
            "JOIN op.order o " +
            "JOIN MemberOrder mo ON mo.order.id = o.id " +
            "JOIN mo.member m " +
            "WHERE m.memberId = :memberId " +
            "AND o.orderedAt = ( " +
            "    SELECT MAX(o2.orderedAt) " +
            "    FROM Orders o2 " +
            "    JOIN MemberOrder mo2 ON mo2.order.id = o2.id " +
            "    WHERE mo2.member.memberId = :memberId " +
            ")")
    BigDecimal findLatestOrderTotalPriceByMemberId(Long memberId);


    //orderProduct Id로 orders 의 사용 포인트를 가져오기 위함
    @Query("SELECT op.order FROM OrderProduct op WHERE op.orderProductId = :orderProductId")
    Orders findOrderByOrderProductId(Long orderProductId);

    //orderProductId로 회원 Id 구해온다
    @Query("SELECT mo.member.memberId " +
            "FROM OrderProduct op " +
            "JOIN op.order o " +
            "JOIN MemberOrder mo ON mo.order.id = o.id " +
            "WHERE op.orderProductId = :orderProductId")
    Long findMemberIdByOrderProductId(Long orderProductId);

    //주문서에서 가격 : 수량으로 있는거를 list로 가져온다
    @Query("SELECT op FROM OrderProduct op " +
            "JOIN op.order o " +
            "JOIN MemberOrder mo ON mo.order.id = o.id " +
            "WHERE mo.member.memberId = :memberId AND mo.id = :memberOrderId")
    List<OrderProduct> findByMemberOrderId(Long memberId, Long memberOrderId);


    @Query("SELECT opr FROM OrderProductReturn opr JOIN opr.orderProduct op WHERE op.orderProductId = :orderProductId")
    OrderProductReturn findByOrderProductOrderProductId(Long orderProductId);

    @Query("SELECT mo.id FROM MemberOrder mo " +
            "JOIN mo.order o " +
            "JOIN o.orderProducts op " +
            "WHERE op.orderProductId = :orderProductId")
    Long findMemberOrderIdByOrderProductId(Long orderProductId);
}
