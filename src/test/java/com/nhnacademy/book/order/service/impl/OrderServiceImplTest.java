package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.*;
import com.nhnacademy.book.order.dto.orderRequests.OrderDeliveryAddressDto;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.enums.OrderStatus;
import com.nhnacademy.book.order.exception.NonMemberPasswordNotMatchException;
import com.nhnacademy.book.order.repository.OrderQueryRepository;
import com.nhnacademy.book.order.repository.OrderRepository;
import com.nhnacademy.book.orderProduct.dto.OrderProductDto;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
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
    void getNonMemberOrderId() {
        String orderNumber = "orderNumber123";
        String password = "rawPassword";
        String encryptedPassword = "encryptedPassword";
        String orderId = "orderId";
        NonMemberOrderDetailAccessRequestDto nonMemberOrderDetailAccessRequestDto = new NonMemberOrderDetailAccessRequestDto(orderNumber, password);
        NonMemberOrderAccessResponseDto nonMemberOrderAccessResponseDto = new NonMemberOrderAccessResponseDto(orderId, encryptedPassword);
        when(orderQueryRepository.findNonMemberOrderByOrderNumber(nonMemberOrderDetailAccessRequestDto.getOrderNumber())).thenReturn(Optional.of(nonMemberOrderAccessResponseDto));
        when(passwordEncoder.matches(password, encryptedPassword)).thenReturn(true);

        String result = orderService.getNonMemberOrderId(nonMemberOrderDetailAccessRequestDto);

        assertNotNull(result);
        assertEquals(orderId, result);
    }

    @DisplayName("비회원주문 상세조회: 주문 못 찾음")
    @Test
    void getNonMemberOrder_order_Id_not_found() {
        String orderNumber = "orderNumber123";
        String password = "rawPassword";
        NonMemberOrderDetailAccessRequestDto nonMemberOrderDetailAccessRequestDto = new NonMemberOrderDetailAccessRequestDto(orderNumber, password);
        when(orderQueryRepository.findNonMemberOrderByOrderNumber(nonMemberOrderDetailAccessRequestDto.getOrderNumber())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> orderService.getNonMemberOrderId(nonMemberOrderDetailAccessRequestDto));
        assertEquals("주문 정보를 찾을 수 없습니다.", exception.getMessage());

    }

    @DisplayName("비회원주문 상세조회: 비밀번호 불일치")
    @Test
    void getNonMemberOrder_Id_password_not_match() {
        String orderNumber = "orderNumber123";
        String password = "rawPassword";
        String encryptedPassword = "encryptedPassword";
        String orderId = "orderId";
        NonMemberOrderDetailAccessRequestDto nonMemberOrderDetailAccessRequestDto = new NonMemberOrderDetailAccessRequestDto(orderNumber, password);
        NonMemberOrderAccessResponseDto nonMemberOrderAccessResponseDto = new NonMemberOrderAccessResponseDto(orderId, encryptedPassword);
        when(orderQueryRepository.findNonMemberOrderByOrderNumber(nonMemberOrderDetailAccessRequestDto.getOrderNumber())).thenReturn(Optional.of(nonMemberOrderAccessResponseDto));
        when(passwordEncoder.matches(password, encryptedPassword)).thenReturn(false);

        NonMemberPasswordNotMatchException exception = assertThrows(NonMemberPasswordNotMatchException.class,
                () -> orderService.getNonMemberOrderId(nonMemberOrderDetailAccessRequestDto));
        assertEquals("비회원주문 비밀번호가 일치하지 않습니다.", exception.getMessage());
    }


    @DisplayName("주문상태 변경")
    @Test
    void modifyStatus() {
        String orderId = "orderId";
        OrderStatus orderStatus = OrderStatus.PAYMENT_PENDING;
        OrderStatusModifyRequestDto modifyRequest = new OrderStatusModifyRequestDto(OrderStatus.PAYMENT_COMPLETED);
        String mockOrderName = "수학의 정석 외 1 건";
        BigDecimal mockOrderPrice = BigDecimal.valueOf(30_000);

        Orders order = Orders.builder()
                .id(orderId)
                .name(mockOrderName)
                .orderPrice(mockOrderPrice)
                .status(orderStatus)
                .build();
        OrderProduct orderProduct1 = new OrderProduct(1L, mock(SellingBook.class), order, BigDecimal.valueOf(10_000), 1, null, OrderProductStatus.PAYMENT_PENDING);
        OrderProduct orderProduct2 = new OrderProduct(2L, mock(SellingBook.class), order, BigDecimal.valueOf(10_000), 1, null, OrderProductStatus.PAYMENT_PENDING);
        OrderProduct orderProduct3 = new OrderProduct(3L, mock(SellingBook.class), order, BigDecimal.valueOf(10_000), 1, null, OrderProductStatus.PAYMENT_PENDING);
        order.addOrderProduct(orderProduct1);
        order.addOrderProduct(orderProduct2);
        order.addOrderProduct(orderProduct3);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        //when
        orderService.modifyStatus(orderId, modifyRequest);

        //then
        assertEquals(OrderStatus.PAYMENT_COMPLETED, order.getStatus());
        assertAll(() -> {
            for(OrderProduct orderProduct: order.getOrderProducts()) {
                assertEquals(OrderProductStatus.PAYMENT_COMPLETED, orderProduct.getStatus());
            }
        });
    }

    @DisplayName("주문상태 변경: 주문정보 못찾음")
    @Test
    void modifyStatus_order_not_found() {
        String orderId = "orderId";
        OrderStatusModifyRequestDto modifyRequest = new OrderStatusModifyRequestDto(OrderStatus.DELIVERED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        //when
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> orderService.modifyStatus(orderId, modifyRequest));
        assertEquals("주문정보를 찾을 수 없습니다.", exception.getMessage());
    }

    @DisplayName("주문전체 배송완료처리")
    @Test
    void orderDelivered() {
        String orderId = "orderId";
        OrderStatus orderStatus = OrderStatus.SHIPPED;
        String mockOrderName = "수학의 정석 외 1 건";
        BigDecimal mockOrderPrice = BigDecimal.valueOf(30_000);

        // Mocking repository save behavior
        Orders order = Orders.builder()
                .id(orderId)
                .name(mockOrderName)
                .orderPrice(mockOrderPrice)
                .status(orderStatus)
                .build();
        OrderProduct orderProduct1 = new OrderProduct(1L, mock(SellingBook.class), order, BigDecimal.valueOf(10_000), 1, null, OrderProductStatus.SHIPPED);
        OrderProduct orderProduct2 = new OrderProduct(2L, mock(SellingBook.class), order, BigDecimal.valueOf(10_000), 1, null, OrderProductStatus.SHIPPED);
        OrderProduct orderProduct3 = new OrderProduct(3L, mock(SellingBook.class), order, BigDecimal.valueOf(10_000), 1, null, OrderProductStatus.SHIPPED);
        order.addOrderProduct(orderProduct1);
        order.addOrderProduct(orderProduct2);
        order.addOrderProduct(orderProduct3);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        //when
        orderService.orderDelivered(orderId);

        //then
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
        assertAll(() -> {
            for(OrderProduct orderProduct: order.getOrderProducts()) {
                assertEquals(OrderProductStatus.DELIVERED, orderProduct.getStatus());
            }
        });
    }

    @DisplayName("주문전체 배송완료처리: 주문정보 못찾음")
    @Test
    void orderDelivered_order_not_found() {
        String orderId = "orderId";

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        //when
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> orderService.orderDelivered(orderId));
        assertEquals("주문정보를 찾을 수 없습니다.", exception.getMessage());
    }
}