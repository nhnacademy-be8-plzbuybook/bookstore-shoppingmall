package com.nhnacademy.book.order.repository;

import com.nhnacademy.book.order.dto.OrderProductReturnDto;
import com.nhnacademy.book.order.dto.OrderReturnSearchRequestDto;
import com.nhnacademy.book.order.dto.QOrderProductReturnDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.nhnacademy.book.order.entity.QOrderProductReturn.orderProductReturn;
import static com.nhnacademy.book.orderProduct.entity.QOrderProduct.orderProduct;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Repository
public class OrderReturnQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Page<OrderProductReturnDto> findOrderProductReturnPage(OrderReturnSearchRequestDto searchRequest, Pageable pageable) {
        List<OrderProductReturnDto> orderProductReturns = queryFactory
                .select(new QOrderProductReturnDto(
                        orderProductReturn.id,
                        orderProductReturn.reason,
                        orderProductReturn.quantity,
                        orderProductReturn.trackingNumber,
                        orderProductReturn.requestedAt,
                        orderProductReturn.completedAt,
                        orderProduct.order.id,
                        orderProduct.orderProductId
                        ))
                .from(orderProductReturn)
                .join(orderProduct).on(orderProduct.eq(orderProductReturn.orderProduct))
                .where(
                        eqTrackingNumber(searchRequest.getTrackingNumber()),
                        eqStatus(searchRequest.getStatus())
                )
                .orderBy(orderProductReturn.requestedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long countResult = queryFactory
                .select(orderProductReturn.count())
                .from(orderProductReturn)
                .where(
                        eqTrackingNumber(searchRequest.getTrackingNumber()),
                        eqStatus(searchRequest.getStatus())
                )
                .fetchOne();

        long total = countResult != null ? countResult : 0L;
        return new PageImpl<>(orderProductReturns, pageable, total);
    }

    private BooleanExpression eqTrackingNumber(String trackingNumber) {
        if (trackingNumber == null || trackingNumber.isBlank()) {
            return null;
        }
        return orderProductReturn.trackingNumber.eq(trackingNumber);
    }

    private BooleanExpression eqStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        } else if (status.equals("RETURN_COMPLETED")) {
            return orderProductReturn.completedAt.isNotNull();
        } else if (status.equals("RETURN_REQUESTED")) {
            return orderProductReturn.completedAt.isNull();
        }
        return null;
    }


}
