package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.OrderProductReturnDto;
import com.nhnacademy.book.order.dto.OrderProductReturnRequestDto;
import com.nhnacademy.book.order.dto.OrderReturnSearchRequestDto;
import com.nhnacademy.book.order.entity.OrderProductReturn;
import com.nhnacademy.book.order.exception.OrderReturnBadRequestException;
import com.nhnacademy.book.order.repository.OrderProductReturnRepository;
import com.nhnacademy.book.order.repository.OrderReturnQueryRepository;
import com.nhnacademy.book.order.service.OrderReturningService;
import com.nhnacademy.book.order.service.OrderStatusService;
import com.nhnacademy.book.order.service.OrderValidationService;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
import com.nhnacademy.book.orderProduct.repository.OrderProductRepository;
import com.nhnacademy.book.orderProduct.service.OrderProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderReturningServiceImpl implements OrderReturningService {
    private final OrderReturnQueryRepository orderReturnQueryRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderProductReturnRepository orderProductReturnRepository;
    private final OrderStatusService orderStatusService;
    private final OrderValidationService orderValidationService;
    private final OrderProductService orderProductService;
    private final ReturnPointServiceImpl returnPointService;

    @Transactional
    @Override
    public void requestOrderProductReturn(String orderId, Long orderProductId, OrderProductReturnRequestDto orderProductReturnRequest) {
        OrderProduct orderProduct = orderProductRepository.findById(orderProductId).orElseThrow(() -> new NotFoundException("주문상품정보를 찾을 수 없습니다."));

        if (orderProduct.getQuantity() < orderProductReturnRequest.getQuantity()) {
            throw new OrderReturnBadRequestException("반품 수량이 주문한 수량을 초과할 수 없다!");
        }

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

        //상품 재고 복구
        orderProductService.addOrderProductStock(orderProductId, orderProductReturn.getQuantity());

        //포인트 환불
        returnPointService.returnPoint(orderProductId);

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

}
