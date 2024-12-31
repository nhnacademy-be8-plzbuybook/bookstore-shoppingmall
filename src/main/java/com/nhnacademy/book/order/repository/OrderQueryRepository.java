package com.nhnacademy.book.order.repository;

import com.nhnacademy.book.order.dto.OrderDto;
import com.nhnacademy.book.order.dto.QOrderDto;
import com.nhnacademy.book.order.enums.OrderStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.nhnacademy.book.book.entity.QBook.book;
import static com.nhnacademy.book.book.entity.QSellingBook.sellingBook;
import static com.nhnacademy.book.member.domain.QMember.member;
import static com.nhnacademy.book.order.entity.QMemberOrder.memberOrder;
import static com.nhnacademy.book.order.entity.QOrders.orders;
import static com.nhnacademy.book.orderProduct.entity.QOrderProduct.orderProduct;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Repository
public class OrderQueryRepository {
    private final JPAQueryFactory queryFactory;

    // 기본 날짜/내림차순 정렬
    // 전체 주문목록 조회
    public Page<OrderDto> findAllOrders(String memberId, String productName, LocalDate orderDate, OrderStatus orderStatus, Pageable pageable) {
        // with @QueryProjection
        List<OrderDto> orderDtos = queryFactory
                .select(new QOrderDto(
                        orders.orderedAt,
                        orders.status,
                        orders.name,
                        orders.orderPrice)
                )
                .from(orders)
                .leftJoin(memberOrder).on(memberOrder.order.eq(orders))
                .innerJoin(orders.orderProducts, orderProduct)
                .innerJoin(orderProduct.sellingBook, sellingBook)
                .innerJoin(sellingBook.book, book)
                .where(
                        eqMemberId(memberId),
                        eqProductName(productName),
                        eqOrderDate(orderDate),
                        eqOrderStatus(orderStatus)
                )
                .orderBy(orders.orderedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 개수 조회 쿼리
        Long countResult = queryFactory
                .select(orders.count())
                .from(orders)
                .leftJoin(memberOrder).on(memberOrder.order.eq(orders))
                .innerJoin(orders.orderProducts, orderProduct)
                .innerJoin(orderProduct.sellingBook, sellingBook)
                .innerJoin(sellingBook.book, book)
                .where(
                        eqMemberId(memberId),
                        eqProductName(productName),
                        eqOrderDate(orderDate),
                        eqOrderStatus(orderStatus)
                )
                .fetchOne();

        long total = (countResult != null) ? countResult : 0L;
        Page<OrderDto> orderPage = new PageImpl<>(orderDtos, pageable, total);

        return orderPage;
    }

    private BooleanExpression eqMemberId(String memberId) {
        if (memberId == null) {
            return null;
        }
        return member.email.eq(memberId);
    }

    private BooleanExpression eqProductName(String bookTitle) {
        if (bookTitle == null) {
            return null;
        }
        return book.bookTitle.contains(bookTitle);
    }

    private BooleanExpression eqOrderDate(LocalDate orderDate) {
        if (orderDate == null) {
            return null;
        }
        LocalDateTime startOfDay = orderDate.atStartOfDay();
        LocalDateTime endOfDay = orderDate.atTime(23, 59, 59, 999999999);

        return orders.orderedAt.between(startOfDay, endOfDay);
    }

    private BooleanExpression eqOrderStatus(OrderStatus status) {
        if (status == null) {
            return null;
        }
        return orders.status.eq(status);
    }

}