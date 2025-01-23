package com.nhnacademy.book.order.repository;

import com.nhnacademy.book.order.dto.*;
import com.nhnacademy.book.order.dto.orderRequests.QOrderDeliveryAddressDto;
import com.nhnacademy.book.order.enums.OrderStatus;
import com.nhnacademy.book.orderProduct.dto.OrderProductDto;
import com.nhnacademy.book.orderProduct.dto.QOrderProductDto;
import com.nhnacademy.book.orderProduct.dto.QOrderProductWrapping;
import com.nhnacademy.book.payment.dto.QPaymentDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.nhnacademy.book.book.entity.QBook.book;
import static com.nhnacademy.book.book.entity.QBookImage.bookImage;
import static com.nhnacademy.book.book.entity.QSellingBook.sellingBook;
import static com.nhnacademy.book.member.domain.QMember.member;
import static com.nhnacademy.book.order.entity.QMemberOrder.memberOrder;
import static com.nhnacademy.book.order.entity.QNonMemberOrder.nonMemberOrder;
import static com.nhnacademy.book.order.entity.QOrderDelivery.orderDelivery;
import static com.nhnacademy.book.order.entity.QOrderDeliveryAddress.orderDeliveryAddress;
import static com.nhnacademy.book.order.entity.QOrderProductCoupon.orderProductCoupon;
import static com.nhnacademy.book.order.entity.QOrderProductWrapping.orderProductWrapping;
import static com.nhnacademy.book.order.entity.QOrders.orders;
import static com.nhnacademy.book.orderProduct.entity.QOrderProduct.orderProduct;
import static com.nhnacademy.book.payment.entity.QPayment.payment;
import static com.nhnacademy.book.wrappingPaper.entity.QWrappingPaper.wrappingPaper;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Repository
public class OrderQueryRepository {
    private final JPAQueryFactory queryFactory;

    // 기본 날짜/내림차순 정렬
    // 전체 주문목록 조회
    public Page<OrderDto> findOrders(OrderSearchRequestDto searchRequest, Pageable pageable) {
        // with @QueryProjection
        List<OrderDto> orderDtos = queryFactory
                .select(
                        new QOrderDto(
                                orders.id,
                                orders.number,
                                orders.orderedAt,
                                orders.status,
                                orders.name,
                                orders.orderPrice.subtract(orders.usedPoint),
                                getMemberEmail()
                        )).distinct()
                .from(orders)
                .leftJoin(memberOrder).on(memberOrder.order.eq(orders))
                .leftJoin(nonMemberOrder).on(nonMemberOrder.order.eq(orders))
                .leftJoin(member).on(memberOrder.member.eq(member))
                .innerJoin(orders.orderProducts, orderProduct)
                .innerJoin(orderProduct.sellingBook, sellingBook)
                .innerJoin(sellingBook.book, book)
                .where(
                        eqOrderNumber(searchRequest.getOrderNumber()),
                        eqMemberId(searchRequest.getMemberId()),
                        eqProductName(searchRequest.getProductName()),
                        eqOrderDate(searchRequest.getOrderDate()),
                        eqOrderStatus(searchRequest.getOrderStatus())
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
                .leftJoin(nonMemberOrder).on(nonMemberOrder.order.eq(orders))
                .leftJoin(member).on(memberOrder.member.eq(member))
                .innerJoin(orders.orderProducts, orderProduct)
                .innerJoin(orderProduct.sellingBook, sellingBook)
                .innerJoin(sellingBook.book, book)
                .where(
                        eqOrderNumber(searchRequest.getOrderNumber()),
                        eqMemberId(searchRequest.getMemberId()),
                        eqProductName(searchRequest.getProductName()),
                        eqOrderDate(searchRequest.getOrderDate()),
                        eqOrderStatus(searchRequest.getOrderStatus())
                )
                .fetchOne();

        long total = (countResult != null) ? countResult : 0L;
        return new PageImpl<>(orderDtos, pageable, total);
    }

    public Optional<OrderDetail> findOrderDetailById(String orderId) {
        return Optional.ofNullable(
                queryFactory
                        .select(new QOrderDetail(
                                orders.id,
                                orders.number,
                                orders.status,
                                orders.deliveryFee,
                                orders.orderPrice,
                                orders.deliveryWishDate,
                                orders.orderedAt,
                                orders.usedPoint,
                                new QOrderDeliveryAddressDto(
                                        orderDeliveryAddress.locationAddress,
                                        orderDeliveryAddress.zipCode,
                                        orderDeliveryAddress.detailAddress,
                                        orderDeliveryAddress.recipient,
                                        orderDeliveryAddress.recipientPhone
                                ),
                                new QOrderDeliveryDto(
                                        orderDelivery.deliveryCompany,
                                        orderDelivery.trackingNumber,
                                        orderDelivery.registeredAt
                                ),
                                new QPaymentDto(
                                        payment.amount,
                                        payment.method,
                                        payment.easyPayProvider,
                                        payment.recordedAt
                                )
                        ))
                        .from(orders)
                        .innerJoin(orderDeliveryAddress).on(orderDeliveryAddress.order.eq(orders))
                        .leftJoin(payment).on(payment.orders.eq(orders))
                        .leftJoin(orderDelivery).on(orderDelivery.order.eq(orders))
                        .orderBy(payment.recordedAt.desc())
                        .where(
                                orders.id.eq(orderId)
                        )
                        .fetchFirst());
    }

    public Optional<NonMemberOrderDetail> findNonMemberOrderByNumber(String orderNumber) {
        return Optional.ofNullable(
                queryFactory
                        .select(new QNonMemberOrderDetail(
                                orders.id,
                                orders.number,
                                orders.status,
                                orders.deliveryFee,
                                orders.orderPrice,
                                orders.deliveryWishDate,
                                orders.orderedAt,
                                nonMemberOrder.password,
                                new QOrderDeliveryAddressDto(
                                        orderDeliveryAddress.locationAddress,
                                        orderDeliveryAddress.zipCode,
                                        orderDeliveryAddress.detailAddress,
                                        orderDeliveryAddress.recipient,
                                        orderDeliveryAddress.recipientPhone
                                ),
                                new QOrderDeliveryDto(
                                        orderDelivery.deliveryCompany,
                                        orderDelivery.trackingNumber,
                                        orderDelivery.registeredAt
                                ),
                                new QPaymentDto(
                                        payment.amount,
                                        payment.method,
                                        payment.easyPayProvider,
                                        payment.recordedAt
                                )
                        ))
                        .from(orders)
                        .innerJoin(nonMemberOrder).on(nonMemberOrder.order.eq(orders))
                        .innerJoin(orderDeliveryAddress).on(orderDeliveryAddress.order.eq(orders))
                        .leftJoin(payment).on(payment.orders.eq(orders))
                        .leftJoin(orderDelivery).on(orderDelivery.order.eq(orders))
                        .where(orders.number.eq(orderNumber))
                        .orderBy(orders.orderedAt.desc())
                        .fetchFirst());
    }

    public Optional<NonMemberOrderAccessResponseDto> findNonMemberOrderByOrderNumber(String orderNumber) {
        NonMemberOrderAccessResponseDto result = queryFactory
                .select(
                        new QNonMemberOrderAccessResponseDto(
                                orders.id,
                                nonMemberOrder.password
                        )
                )
                .from(orders)
                .innerJoin(nonMemberOrder).on(nonMemberOrder.order.eq(orders))
                .where(orders.number.eq(orderNumber))
                .fetchFirst();

        return Optional.ofNullable(result);
    }


    /**
     * 주문상품 조회
     *
     * @param orderId 주문 ID
     * @return 주문상품 DTO 리스트
     */
    public List<OrderProductDto> findOrderProducts(String orderId) {
        List<OrderProductDto> orderProductDtos = queryFactory
                .select(
                        new QOrderProductDto(
                                orderProduct.orderProductId,
                                bookImage.imageUrl.as("imageUrl"),
                                orderProduct.sellingBook.sellingBookId.as("bookId"),
                                orderProduct.sellingBook.book.bookTitle,
                                orderProduct.quantity,
                                orderProduct.price,
                                orderProduct.status,
                                new QOrderProductWrapping(
                                        wrappingPaper.name,
                                        orderProductWrapping.quantity,
                                        wrappingPaper.price// orderProductWrapping에도 가격 저장필요
                                )
                        )
                )
                .from(orderProduct)
                .innerJoin(bookImage).on(bookImage.book.eq(orderProduct.sellingBook.book))
                .leftJoin(orderProductWrapping).on(orderProductWrapping.orderProduct.eq(orderProduct))
                .leftJoin(wrappingPaper).on(wrappingPaper.id.eq(orderProductWrapping.wrappingPaper.id))
                .where(orderProduct.order.id.eq(orderId))
                .fetch();

        Map<Long, List<OrderProductCouponDto>> orderProductCouponMap = queryFactory
                .select(
                        new QOrderProductCouponDto(
                                orderProductCoupon.couponId,
                                orderProduct.orderProductId,
                                orderProductCoupon.discount
                        )
                )
                .from(orderProductCoupon)
                .innerJoin(orderProduct).on(orderProduct.order.id.eq(orderId))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(
                        OrderProductCouponDto::getOrderProductId// orderProductId를 기준으로 그룹화
                ));

        orderProductDtos.forEach(op -> {
            List<OrderProductCouponDto> orderProductCouponDtos = orderProductCouponMap.get(op.getOrderProductId());
            op.setOrderProductCoupons(orderProductCouponDtos != null ? orderProductCouponDtos : Collections.emptyList());
        });


        return orderProductDtos;
    }


    private StringExpression getMemberEmail() {
        return member.email.coalesce("비회원"); // member.email이 null일 때 "비회원을 반환"
    }

    private BooleanExpression eqOrderNumber(String orderNumber) {
        if (orderNumber == null) {
            return null;
        }
        return orders.number.eq(orderNumber);
    }


    private BooleanExpression eqMemberId(String memberEmail) {
        if (memberEmail == null) {
            return null;
        }
        return member.email.eq(memberEmail);
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
