package com.nhnacademy.book.order.repository;

import com.nhnacademy.book.order.dto.OrderReturnDto;
import com.nhnacademy.book.order.dto.OrderReturnSearchRequestDto;
import com.nhnacademy.book.order.dto.QOrderReturnDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.nhnacademy.book.order.entity.QOrderReturn.orderReturn;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Repository
public class OrderReturnQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Page<OrderReturnDto> findOrderReturnPage(OrderReturnSearchRequestDto searchRequest, Pageable pageable) {
        List<OrderReturnDto> orderReturns = queryFactory
                .select(new QOrderReturnDto(
                        orderReturn.id,
                        orderReturn.reason,
                        orderReturn.trackingNumber,
                        orderReturn.requestedAt,
                        orderReturn.completedAt,
                        orderReturn.order.id
                ))
                .from(orderReturn)
                .where(
                        eqTrackingNumber(searchRequest.getTrackingNumber()),
                        eqStatus(searchRequest.getStatus())
                )
                .orderBy(orderReturn.requestedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long countResult = queryFactory
                .select(orderReturn.count())
                .from(orderReturn)
                .where(
                        eqTrackingNumber(searchRequest.getTrackingNumber()),
                        eqStatus(searchRequest.getStatus())
                )
                .fetchOne();

        long total = countResult != null ? countResult : 0L;
        return new PageImpl<>(orderReturns, pageable, total);
    }

    private BooleanExpression eqTrackingNumber(String trackingNumber) {
        if (trackingNumber == null || trackingNumber.isBlank()) {
            return null;
        }
        return orderReturn.trackingNumber.eq(trackingNumber);
    }

    private BooleanExpression eqStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        else if (status.equals("RETURN_COMPLETED")) {
            return orderReturn.completedAt.isNotNull();
        } else if (status.equals("RETURN_REQUESTED")) {
            return orderReturn.completedAt.isNull();
        }
        return null;
    }
}
