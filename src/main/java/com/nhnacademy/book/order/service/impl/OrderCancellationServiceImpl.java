package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.book.repository.SellingBookRepository;
import com.nhnacademy.book.coupon.service.CouponService;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.OrderCancelRequestDto;
import com.nhnacademy.book.order.dto.OrderProductCancelRequestDto;
import com.nhnacademy.book.order.entity.OrderProductCoupon;
import com.nhnacademy.book.order.entity.OrderProductWrapping;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.enums.OrderStatus;
import com.nhnacademy.book.order.repository.OrderProductCancelRepository;
import com.nhnacademy.book.order.repository.OrderRepository;
import com.nhnacademy.book.order.service.OrderCacheService;
import com.nhnacademy.book.order.service.OrderCancellationService;
import com.nhnacademy.book.order.service.OrderValidationService;
import com.nhnacademy.book.order.service.ReturnPointService;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
import com.nhnacademy.book.orderProduct.repository.OrderProductRepository;
import com.nhnacademy.book.payment.dto.PaymentCancelRequestDto;
import com.nhnacademy.book.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class OrderCancellationServiceImpl implements OrderCancellationService {
    private final OrderCacheService orderCacheService;
    private final PaymentService paymentService;
    private final OrderProductRepository orderProductRepository;
    private final SellingBookRepository sellingBookRepository;
    private final OrderProductCancelRepository orderProductCancelRepository;
    private final OrderRepository orderRepository;
    private final OrderValidationService orderValidationService;
    private final ReturnPointService returnPointService;
    private final CouponService couponService;

    @Transactional
    @Override
    public void cancelOrderProducts(String orderId, OrderCancelRequestDto cancelRequest) {


        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("주문정보를 찾을 수 없습니다."));
        BigDecimal totalCancelAmount = BigDecimal.ZERO;

        for (OrderProductCancelRequestDto orderProductCancelRequest : cancelRequest.getCancelProducts()) {
            OrderProduct orderProduct = orderProductRepository.findById(orderProductCancelRequest.getOrderProductId()).orElseThrow(() -> new NotFoundException("주문상품을 찾을 수 없습니다."));
            orderValidationService.validateOrderProductForCanceling(orderProduct);

            // TODO: 쿠폰복구
            //couponService.cancelCoupon(couponId) //쿠폰사용취소인 경우 (couponId)
            //couponService.refundCoupon(new RefundCouponRequestDto(couponId, memberId)); //쿠폰환불인 경우 (couponId, memberId)

            // 포인트 복구
            returnPointService.returnPoint(orderProduct.getOrderProductId());

            // 재고 복구
            restoreOrderProductStock(orderProduct);

            // 결제취소금액 계산
            totalCancelAmount = totalCancelAmount.add(calculateCancelAmount(orderProduct, orderProductCancelRequest));

            // 주문상품취소 저장
            orderProductCancelRepository.save(orderProductCancelRequest.toEntity(cancelRequest.getReason(), orderProduct));

            // 주문상품 상태변경
            orderProduct.updateStatus(OrderProductStatus.ORDER_CANCELLED);
        }

        // 주문상태변경
        order.updateOrderStatus((OrderStatus.fromOrderProductStatus(order.getOrderProducts().stream().map(OrderProduct::getStatus).toList())));
        // 결제취소
        paymentService.cancelPayment(new PaymentCancelRequestDto(cancelRequest.getReason(), totalCancelAmount, orderId));
    }

    private BigDecimal calculateCancelAmount(OrderProduct orderProduct, OrderProductCancelRequestDto orderProductCancelRequest) {
        // 결제취소금액에 주문상품금액 추가
        BigDecimal cancelAmount = orderProduct.getPrice().multiply(new BigDecimal(orderProductCancelRequest.getQuantity()));
        // 결제취소 금액에 주문상품포장 금액 추가
        if (orderProduct.getOrderProductWrappings() != null && !orderProduct.getOrderProductWrappings().isEmpty()) {
            for (OrderProductWrapping orderProductWrapping : orderProduct.getOrderProductWrappings()) {
                cancelAmount = cancelAmount.add(orderProductWrapping.getPrice().multiply(new BigDecimal(orderProductWrapping.getQuantity())));
            }
        }
        // 결제취소 금액에 주문상품쿠폰 할인금액 차감
        if (orderProduct.getOrderProductCoupons() != null && !orderProduct.getOrderProductCoupons().isEmpty()) {
            for (OrderProductCoupon orderProductCoupon : orderProduct.getOrderProductCoupons()) {
                cancelAmount = cancelAmount.subtract(orderProductCoupon.getDiscount());
            }
        }
        return cancelAmount;
    }

    private void restoreOrderProductStock(OrderProduct orderProduct) {
        Long sellingBookId = orderProduct.getSellingBook().getSellingBookId();
        SellingBook sellingBook = sellingBookRepository.findById(sellingBookId).orElseThrow(() -> new NotFoundException("판매책을 찾을 수 없습니다."));
        sellingBook.setSellingBookStock(sellingBook.getSellingBookStock() + orderProduct.getQuantity());
        orderCacheService.addStockCache(orderProduct.getOrderProductId(), Long.valueOf(orderProduct.getQuantity()));
    }

}
