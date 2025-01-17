package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.OrderProductReturnDto;
import com.nhnacademy.book.order.dto.OrderProductReturnRequestDto;
import com.nhnacademy.book.order.dto.OrderReturnSearchRequestDto;
import com.nhnacademy.book.order.entity.OrderProductReturn;
import com.nhnacademy.book.order.repository.OrderProductReturnRepository;
import com.nhnacademy.book.order.repository.OrderRepository;
import com.nhnacademy.book.order.repository.OrderReturnQueryRepository;
import com.nhnacademy.book.order.service.OrderDeliveryService;
import com.nhnacademy.book.order.service.OrderReturningService;
import com.nhnacademy.book.order.service.OrderStatusService;
import com.nhnacademy.book.order.service.OrderValidationService;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
import com.nhnacademy.book.orderProduct.repository.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderReturningServiceImpl implements OrderReturningService {
    private final OrderRepository orderRepository;
    private final OrderReturnQueryRepository orderReturnQueryRepository;
    private final OrderDeliveryService orderDeliveryService;
    private final OrderProductRepository orderProductRepository;
    private final OrderProductReturnRepository orderProductReturnRepository;
    private final OrderStatusService orderStatusService;
    private final OrderValidationService orderValidationService;

    @Transactional
    @Override
    public void requestOrderProductReturn(String orderId, Long orderProductId, OrderProductReturnRequestDto orderProductReturnRequest) {
        OrderProduct orderProduct = orderProductRepository.findById(orderProductId).orElseThrow(() -> new NotFoundException("주문상품정보를 찾을 수 없습니다."));
        // 반품요청 조건 검증
        orderValidationService.validateOrderProductForReturning(orderProduct);

        // 주문상품반품 저장
        orderProductReturnRepository.save(orderProductReturnRequest.toEntity(orderProduct));

        // 주문상품상태 변경
        orderStatusService.modifyOrderProductStatus(orderProductId, OrderProductStatus.RETURN_REQUESTED);
    }




    @Transactional
    @Override
    public Long completeOrderProductReturn(Long orderProductId) {
        OrderProduct orderProduct = orderProductRepository.findById(orderProductId).orElseThrow(() -> new NotFoundException("주문상품정보를 찾을 수 없습니다."));
        OrderProductReturn orderProductReturn = orderProductReturnRepository.findByOrderProduct(orderProduct).orElseThrow(()
                -> new NotFoundException("주문상품반품 정보를 찾을 수 없습니다."));
        orderValidationService.validateOrderProductForReturnCompletion(orderProduct);

        //TODO: 반품 포인트적립 (결제금액 - 반품 택배비)
        //TODO: 재고 복구

        // 주문상품상태 변경
        orderStatusService.modifyOrderProductStatus(orderProductId, OrderProductStatus.RETURN_COMPLETED);

        // 주문상품반품 완료시간 업데이트
        orderProductReturn.complete();
        return orderProductId;
    }



    @Override
    @Transactional(readOnly = true)
    public Page<OrderProductReturnDto> getAllOrderProductReturns(OrderReturnSearchRequestDto searchRequest, Pageable pageable) {
        return orderReturnQueryRepository.findOrderProductReturnPage(searchRequest, pageable);
    }



//    private void validateOrderProductForReturning(OrderProduct orderProduct) {
//        int statusCode = orderProduct.getStatus().getCode();
//        // 발송완료 <= statusCode <= 구매확정
//        if (!(statusCode >= 2 && statusCode <= 5)) {
//            throw new ConflictException("반품이 불가능한 주문상품입니다. (사유: 반품가능 상태가 아님)");
//        }
//        Orders order = orderRepository.findById(orderProduct.getOrder().getId()).orElseThrow(() -> new NotFoundException("주문정보를 찾을 수 없습니다."));
//        boolean isReturnable = orderDeliveryService.isInReturnablePeriod(order);
//        if (!isReturnable) {
//            throw new ConflictException("반품이 불가능한 주문입니다. (사유: 반품기간 지남)");
//        }
//    }
//    private void validateOrderProductForReturnCompletion(OrderProduct orderProduct) {
//        if (orderProduct.getStatus() != OrderProductStatus.RETURN_REQUESTED) {
//            throw new ConflictException("반품요청된 주문상품이 아닙니다.");
//        }
//    }
}
