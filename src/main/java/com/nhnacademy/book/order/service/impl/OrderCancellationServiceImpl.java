package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.book.repository.SellingBookRepository;
import com.nhnacademy.book.deliveryFeePolicy.exception.ConflictException;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.OrderCancelRequestDto;
import com.nhnacademy.book.order.dto.OrderProductCancelRequestDto;
import com.nhnacademy.book.order.entity.OrderCancel;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.enums.OrderStatus;
import com.nhnacademy.book.order.repository.OrderCancelRepository;
import com.nhnacademy.book.order.repository.OrderProductCancelRepository;
import com.nhnacademy.book.order.repository.OrderRepository;
import com.nhnacademy.book.order.service.OrderCacheService;
import com.nhnacademy.book.order.service.OrderCancellationService;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
import com.nhnacademy.book.orderProduct.repository.OrderProductRepository;
import com.nhnacademy.book.payment.dto.PaymentCancelRequestDto;
import com.nhnacademy.book.payment.entity.Payment;
import com.nhnacademy.book.payment.repository.PaymentRepository;
import com.nhnacademy.book.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderCancellationServiceImpl implements OrderCancellationService {
    private final OrderRepository orderRepository;
    private final OrderCancelRepository orderCancelRepository;
    private final OrderCacheService orderCacheService;
    private final PaymentService paymentService;
    private final OrderProductRepository orderProductRepository;
    private final SellingBookRepository sellingBookRepository;
    private final OrderProductCancelRepository orderProductCancelRepository;


//    @Transactional
//    @Override
//    public void cancelOrder(String orderId, OrderCancelRequestDto orderCancelRequest) {
//        // 주문확인
//        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("찾을 수 없는 주문입니다."));
//        // 주문상태확인 - 배송이전만 가능
//        validateOrderForCanceling(order);
//
//        // 결제취소 요청
////        Payment payment = paymentRepository.findByOrdersId(orderId).orElseThrow(() -> new NotFoundException("찾을 수 없는 결제정보입니다."));
////        PaymentCancelRequestDto paymentCancelRequest = new PaymentCancelRequestDto(orderCancelRequest.getCancelReason(), null, orderId);
////        paymentService.cancelPayment(payment.getPaymentKey(), paymentCancelRequest);
//
//        //TODO: 포인트 복구
//        //TODO: 쿠폰 복구
//        //TODO: 재고 복구 (캐시, db 둘 다 ?)
//        List<OrderProduct> orderProducts = orderProductRepository.findByOrderId(orderId);
//        restoreOrderProductsStock(orderProducts);
//
//
//        // 주문, 주문상품 상태 변경
//        order.updateOrderStatus(OrderStatus.ORDER_CANCELLED);
//        for (OrderProduct orderProduct : orderProducts) {
//            orderProduct.updateStatus(OrderProductStatus.ORDER_CANCELLED);
//        }
//        // 주문취소 저장
//        orderCancelRepository.save(new OrderCancel(LocalDateTime.now(), orderCancelRequest.getCancelReason(), order));
//    }

    @Transactional
    @Override
    public void cancelOrderProduct(String orderId, Long orderProductId, OrderProductCancelRequestDto orderProductCancelRequest) {
        OrderProduct orderProduct = orderProductRepository.findById(orderProductId).orElseThrow(() -> new NotFoundException("주문상품을 찾을 수 없습니다."));

        // 결제취소
        paymentService.cancelPayment(orderId, orderProductCancelRequest);

        // TODO: 쿠폰복구

        // 재고 복구
        restoreOrderProductStock(orderProduct);

        // 주문상품취소 저장
        orderProductCancelRepository.save(orderProductCancelRequest.toEntity(orderProduct));

        // 상태변경
        orderProduct.updateStatus(OrderProductStatus.ORDER_CANCELLED);
    }


//    private void restoreOrderProductsStock(List<OrderProduct> orderProducts) {
//        for (OrderProduct orderProduct : orderProducts) {
//            Long sellingBookId = orderProduct.getSellingBook().getSellingBookId();
//            SellingBook sellingBook = sellingBookRepository.findById(sellingBookId).orElseThrow(() -> new NotFoundException("판매책을 찾을 수 없습니다."));
//            sellingBook.setSellingBookStock(sellingBook.getSellingBookStock() + orderProduct.getQuantity());
//            orderCacheService.addStockCache(orderProduct.getOrderProductId(), Long.valueOf(orderProduct.getQuantity()));
//        }
//    }

    private void restoreOrderProductStock(OrderProduct orderProduct) {
        Long sellingBookId = orderProduct.getSellingBook().getSellingBookId();
        SellingBook sellingBook = sellingBookRepository.findById(sellingBookId).orElseThrow(() -> new NotFoundException("판매책을 찾을 수 없습니다."));
        sellingBook.setSellingBookStock(sellingBook.getSellingBookStock() + orderProduct.getQuantity());
        orderCacheService.addStockCache(orderProduct.getOrderProductId(), Long.valueOf(orderProduct.getQuantity()));
    }


    private void validateOrderForCanceling(Orders order) {
        if (order.getStatus().getCode() > 1) { // 상태가 배송 이후일때
            throw new ConflictException("주문상태가 " + order.getStatus().getStatus() + "일때는 주문취소가 불가능합니다.");
        }
    }

    private void validateOrderProductForCanceling(OrderProduct orderProduct) {
        // 주문상품의 상태가 "발송완료" 이상이면 주문취소 불가능
        if (orderProduct.getStatus().getCode() > 1) {
            throw new ConflictException("주문상품의 상태가 " + orderProduct.getStatus().getStatus() + "일 때는 주문취소가 불가능합니다.");
        }
    }
}
