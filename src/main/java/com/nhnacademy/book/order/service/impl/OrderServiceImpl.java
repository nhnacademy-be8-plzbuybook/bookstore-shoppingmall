package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.*;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.enums.OrderStatus;
import com.nhnacademy.book.order.exception.NonMemberPasswordNotMatchException;
import com.nhnacademy.book.order.repository.OrderQueryRepository;
import com.nhnacademy.book.order.repository.OrderRepository;
import com.nhnacademy.book.order.service.OrderService;
import com.nhnacademy.book.orderProduct.dto.OrderProductDto;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderQueryRepository orderQueryRepository;
    private final PasswordEncoder passwordEncoder;
    private final OrderRepository orderRepository;

    /**
     * 전체 주문목록 조회
     *
     * @param searchRequest 주문 검색 DTO
     * @param pageable      페이징
     * @return 주문 DTO 페이지
     */
    @Transactional(readOnly = true)
    @Override
    public Page<OrderDto> getOrders(OrderSearchRequestDto searchRequest, Pageable pageable) {
        return orderQueryRepository.findOrders(searchRequest, pageable);
    }


    /**
     * 주문 상세조회
     *
     * @param orderId 주문 ID
     * @return 주문상세 DTO
     */
    @Transactional(readOnly = true)
    @Override
    public OrderDetail getOrderDetail(String orderId) {
        OrderDetail orderDetail = orderQueryRepository.findOrderDetailById(orderId).orElseThrow(() -> new NotFoundException("주문정보를 찾을 수 없습니다. 주문아이디: " + orderId));
        List<OrderProductDto> orderProducts = orderQueryRepository.findOrderProducts(orderId);
        orderDetail.setOrderProducts(orderProducts);

        return orderDetail;
    }

    @Override
    public String getNonMemberOrderId(NonMemberOrderDetailAccessRequestDto accessRequest) {
        NonMemberOrderAccessResponseDto nonMemberOrderAccessResponseDto = orderQueryRepository.findNonMemberOrderByOrderNumber(accessRequest.getOrderNumber())
                .orElseThrow(() -> new NotFoundException("주문 정보를 찾을 수 없습니다."));
        validateNonMemberOrderPassword(accessRequest.getPassword(), nonMemberOrderAccessResponseDto.getPassword());

        return nonMemberOrderAccessResponseDto.getOrderId();
    }

    @Transactional
    @Override
    public void modifyStatus(String orderId, OrderStatusModifyRequestDto modifyRequest) {

        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("주문정보를 찾을 수 없습니다."));
        // 주문상태변경
        order.updateOrderStatus(modifyRequest.getStatus());
        // 주문상품 상태변경
        List<OrderProduct> orderProducts = order.getOrderProducts();
        if (orderProducts != null) {
            for (OrderProduct orderProduct: orderProducts) {
                orderProduct.updateStatus(OrderProductStatus.fromStatus(modifyRequest.getStatus().getStatus()));
            }
        }
    }

    @Transactional
    @Override
    public void orderDelivered(String orderId) {
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("주문정보를 찾을 수 없습니다."));
        order.updateOrderStatus(OrderStatus.DELIVERED);
        for (OrderProduct orderProduct: order.getOrderProducts()) {
            orderProduct.updateStatus(OrderProductStatus.DELIVERED);
        }
    }

    private void validateNonMemberOrderPassword(String rawPassword, String targetPassword) {
        if (!passwordEncoder.matches(rawPassword, targetPassword)) {
            throw new NonMemberPasswordNotMatchException("비회원주문 비밀번호가 일치하지 않습니다.");
        }
    }

}
