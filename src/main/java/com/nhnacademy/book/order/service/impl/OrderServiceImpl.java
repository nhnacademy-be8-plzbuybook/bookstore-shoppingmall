package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.NonMemberOrderDetail;
import com.nhnacademy.book.order.dto.OrderDetail;
import com.nhnacademy.book.order.dto.OrderDto;
import com.nhnacademy.book.order.dto.OrderSearchRequestDto;
import com.nhnacademy.book.order.exception.NonMemberPasswordNotMatchException;
import com.nhnacademy.book.order.repository.OrderDeliveryAddressRepository;
import com.nhnacademy.book.order.repository.OrderQueryRepository;
import com.nhnacademy.book.order.service.OrderService;
import com.nhnacademy.book.orderProduct.dto.OrderProductDto;
import com.nhnacademy.book.payment.repository.PaymentRepository;
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
    private final OrderDeliveryAddressRepository orderDeliveryAddressRepository;
    private final PaymentRepository paymentRepository;
    private final PasswordEncoder passwordEncoder;

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
        return orderQueryRepository.findOrders(searchRequest.getMemberId(), searchRequest.getProductName(),
                searchRequest.getOrderDate(), searchRequest.getOrderStatus(), pageable);
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


    /**
     * 비회원주문 상세 조회
     *
     * @param orderNumber 주문번호
     * @param password    비회원주문 조회용 비밀번호
     * @return 비회원주문상세 DTO
     */
    @Transactional(readOnly = true)
    @Override
    public NonMemberOrderDetail getNonMemberOrderDetail(String orderNumber, String password) {
        NonMemberOrderDetail nonMemberOrderDetail = orderQueryRepository.findNonMemberOrderByNumber(orderNumber).orElseThrow(() -> new NotFoundException("주문정보를 찾을 수 없습니다. 주문번호: " + orderNumber));
        validateNonMemberOrderPassword(password, nonMemberOrderDetail.getPassword());

        List<OrderProductDto> orderProducts = orderQueryRepository.findOrderProducts(nonMemberOrderDetail.getOrderId());
        nonMemberOrderDetail.setOrderProducts(orderProducts);

        return nonMemberOrderDetail;
    }

    private void validateNonMemberOrderPassword(String rawPassword, String targetPassword) {
        if (!passwordEncoder.matches(rawPassword, targetPassword)) {
            throw new NonMemberPasswordNotMatchException("비회원주문 비밀번호가 일치하지 않습니다.");
        }
    }

}
