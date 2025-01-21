package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.*;
import com.nhnacademy.book.order.dto.orderRequests.OrderDeliveryAddressDto;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.enums.OrderStatus;
import com.nhnacademy.book.order.repository.OrderQueryRepository;
import com.nhnacademy.book.order.repository.OrderRepository;
import com.nhnacademy.book.orderProduct.dto.OrderProductDto;
import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
import com.nhnacademy.book.orderProduct.repository.OrderProductRepository;
import com.nhnacademy.book.payment.dto.PaymentDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private OrderQueryRepository orderQueryRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderProductRepository orderProductRepository;
    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void getOrders() {
        OrderSearchRequestDto searchRequest = new OrderSearchRequestDto("memberId", "productName",
                LocalDate.now(), OrderStatus.PAYMENT_COMPLETED, "orderNumber");
        Pageable pageable = PageRequest.of(0, 20);

        Page<OrderDto> mockPage = new PageImpl<>(List.of(mock(OrderDto.class))); // 적절한 반환 값 생성
        when(orderQueryRepository.findOrders(eq(searchRequest), eq(pageable))).thenReturn(mockPage);

        //when
        orderService.getOrders(searchRequest, pageable);

        verify(orderQueryRepository).findOrders(searchRequest, pageable);
    }

    @DisplayName("주문 상세조회")
    @Test
    void getOrderDetail() {
        String orderId = "orderId";
        OrderDetail orderDetail = spy(new OrderDetail("orderId", "orderNumber", OrderStatus.PAYMENT_COMPLETED,
                BigDecimal.ZERO, BigDecimal.valueOf(30_000), LocalDate.now(), LocalDateTime.now(), 0,
                mock(OrderDeliveryAddressDto.class), mock(OrderDeliveryDto.class), mock(PaymentDto.class)));
        OrderProductCouponDto orderProductCouponDto1 = new OrderProductCouponDto(1L, 1L, BigDecimal.valueOf(3_000));
        OrderProductCouponDto orderProductCouponDto2 = new OrderProductCouponDto(2L, 2L, BigDecimal.valueOf(3_000));
        OrderProductCouponDto orderProductCouponDto3 = new OrderProductCouponDto(3L, 3L, BigDecimal.valueOf(3_000));

        OrderProductDto orderProductDto1 = new OrderProductDto(1L, "image/url", 1L, "bookTitle1", 1, BigDecimal.valueOf(10_000), OrderProductStatus.PAYMENT_COMPLETED, null);
        OrderProductDto orderProductDto2 = new OrderProductDto(2L, "image/url", 2L, "bookTitle2", 1, BigDecimal.valueOf(10_000), OrderProductStatus.PAYMENT_COMPLETED, null);
        OrderProductDto orderProductDto3 = new OrderProductDto(3L, "image/url", 3L, "bookTitle3", 1, BigDecimal.valueOf(10_000), OrderProductStatus.PAYMENT_COMPLETED, null);

        orderProductDto1.setOrderProductCoupons(List.of(orderProductCouponDto1));
        orderProductDto2.setOrderProductCoupons(List.of(orderProductCouponDto2));
        orderProductDto3.setOrderProductCoupons(List.of(orderProductCouponDto3));

        List<OrderProductDto> orderProductDtos = List.of(orderProductDto1, orderProductDto2, orderProductDto3);

        when(orderQueryRepository.findOrderDetailById(orderId)).thenReturn(Optional.of(orderDetail));
        when(orderQueryRepository.findOrderProducts(orderId)).thenReturn(orderProductDtos);

        //when
        OrderDetail result = orderService.getOrderDetail(orderId);
        assertNotNull(result);
        assertNotNull(result.getOrderProducts());
        assertEquals(3, result.getOrderProducts().size());
        assertEquals(BigDecimal.valueOf(9_000), result.getCouponDiscount());
        verify(orderDetail).setOrderProducts(List.of(orderProductDto1, orderProductDto2, orderProductDto3));
    }

    @DisplayName("주문 상세조회: 주문정보 못 찾음")
    @Test
    void getOrderDetail_order_not_found() {
        String orderId = "orderId";
        when(orderQueryRepository.findOrderDetailById(orderId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class ,
                () -> orderService.getOrderDetail(orderId));

        assertEquals("주문정보를 찾을 수 없습니다. 주문아이디: " + orderId, exception.getMessage());
    }

    @DisplayName("비회원주문 상세조회")
    @Test
    void getNonMemberOrder() {
        String orderNumber = "orderNumber123";
        String password = "rawPassword";
        String encryptedPassword = "encryptedPassword";
        String orderId = "orderId";
        NonMemberOrderDetailAccessRequestDto nonMemberOrderDetailAccessRequestDto = new NonMemberOrderDetailAccessRequestDto(orderNumber, password);
        NonMemberOrderAccessResponseDto nonMemberOrderAccessResponseDto = new NonMemberOrderAccessResponseDto(orderId, encryptedPassword);
        when(orderQueryRepository.findNonMemberOrderByOrderNumber(nonMemberOrderDetailAccessRequestDto.getOrderNumber())).thenReturn(Optional.of(nonMemberOrderAccessResponseDto));
        when(passwordEncoder.matches(password, encryptedPassword)).thenReturn(true);

        String result = orderService.getNonMemberOrder(nonMemberOrderDetailAccessRequestDto);

    }

//    @DisplayName("비회원주문 상세조회: 주문 못 찾음")
//    @Test
//    void getNonMemberOrder_order_not_found() {
//        String orderNumber = "orderNumber123";
//        String password = "rawPassword";
//        String encryptedPassword = "encryptedPassword";
//        String orderId = "orderId";
//        NonMemberOrderDetailAccessRequestDto nonMemberOrderDetailAccessRequestDto = new NonMemberOrderDetailAccessRequestDto(orderNumber, password);
//        NonMemberOrderAccessResponseDto nonMemberOrderAccessResponseDto = new NonMemberOrderAccessResponseDto(orderId, encryptedPassword);
//        when(orderQueryRepository.findNonMemberOrderByOrderNumber(nonMemberOrderDetailAccessRequestDto.getOrderNumber())).thenReturn(Optional.of(nonMemberOrderAccessResponseDto));
//
//    }
//
//    @DisplayName("비회원주문 상세조회: 비밀번호 불일치")
//    @Test
//    void getNonMemberOrder_password_not_match() {
//        String orderNumber = "orderNumber123";
//        String password = "rawPassword";
//        String encryptedPassword = "encryptedPassword";
//        String orderId = "orderId";
//        NonMemberOrderDetailAccessRequestDto nonMemberOrderDetailAccessRequestDto = new NonMemberOrderDetailAccessRequestDto(orderNumber, password);
//        NonMemberOrderAccessResponseDto nonMemberOrderAccessResponseDto = new NonMemberOrderAccessResponseDto(orderId, encryptedPassword);
//        when(orderQueryRepository.findNonMemberOrderByOrderNumber(nonMemberOrderDetailAccessRequestDto.getOrderNumber())).thenReturn(Optional.of(nonMemberOrderAccessResponseDto));
//
//    }

    @Test
    void modifyStatus() {
    }

    @Test
    void orderDelivered() {
    }

    @Test
    void getNonMemberOrderDetail() {
    }
}