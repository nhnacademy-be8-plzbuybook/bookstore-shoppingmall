package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.order.dto.MemberOrderSaveRequestDto;
import com.nhnacademy.book.order.dto.NonMemberOrderSaveRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.*;
import com.nhnacademy.book.order.entity.MemberOrder;
import com.nhnacademy.book.order.entity.NonMemberOrder;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.enums.OrderType;
import com.nhnacademy.book.order.repository.MemberOrderRepository;
import com.nhnacademy.book.order.repository.NonMemberOrderRepository;
import com.nhnacademy.book.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerOrderServiceImplTest {
    @Mock
    private NonMemberOrderRepository nonMemberOrderRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private MemberOrderRepository memberOrderRepository;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private CustomerOrderServiceImpl  customerOrderService;

    private OrderRequestDto memberOrderRequest;
    private OrderRequestDto nonMemberOrderRequest;

    @BeforeEach
    void setUp() {
        Integer usedPoint = 1000;
        BigDecimal couponDiscount = BigDecimal.valueOf(3_000);
        BigDecimal deliveryFee = BigDecimal.valueOf(3_000);
        BigDecimal orderPrice = BigDecimal.valueOf(30_000);
        OrderProductRequestDto orderProductRequest = new OrderProductRequestDto(1L, orderPrice, 1, List.of(new OrderProductAppliedCouponDto(1L, couponDiscount)), null);
        memberOrderRequest = new OrderRequestDto(
                OrderType.MEMBER_ORDER,
                LocalDate.of(2025, 3, 6),
                usedPoint,
                List.of(orderProductRequest),
                new OrderDeliveryAddressDto("필문대로 123번길", "12345", "조선대학교 1층", "NHN", "010-1234-5678"),
                deliveryFee,
                orderPrice,
                "test@email.com",
                null
        );

        nonMemberOrderRequest = new OrderRequestDto(
                OrderType.NON_MEMBER_ORDER,
                LocalDate.of(2025, 3, 6),
                usedPoint,
                List.of(orderProductRequest),
                new OrderDeliveryAddressDto("필문대로 123번길", "12345", "조선대학교 1층", "NHN", "010-1234-5678"),
                deliveryFee,
                orderPrice,
                null,
                "password123"
        );
    }



    @Test
    void placeMemberOrder() {
        String memberEmail = "test@email.com";
        String orderId = "testOrderId";
        Long memberOrderId = 1L;
        MemberOrderSaveRequestDto saveRequest = new MemberOrderSaveRequestDto(memberEmail, orderId);
        Orders mockOrder = mock(Orders.class);
        Member mockMember = mock(Member.class);
        MemberOrder mockMemberOrder = mock(MemberOrder.class);
        when(orderRepository.findById(saveRequest.getOrderId())).thenReturn(Optional.of(mockOrder));
        when(memberRepository.findByEmail(memberEmail)).thenReturn(Optional.of(mockMember));
        when(memberOrderRepository.save(any(MemberOrder.class))).thenReturn(mockMemberOrder);
        when(mockMemberOrder.getId()).thenReturn(memberOrderId);
        //when
        Long result = customerOrderService.placeMemberOrder(saveRequest);
        assertNotNull(result);
        assertEquals(memberOrderId, result);
        verify(memberOrderRepository).save(any(MemberOrder.class));
    }

    @Test
    void placeMemberOrder_order_not_found() {
        String memberEmail = "test@email.com";
        String orderId = "testOrderId";
        MemberOrderSaveRequestDto saveRequest = new MemberOrderSaveRequestDto(memberEmail, orderId);
        when(orderRepository.findById(saveRequest.getOrderId())).thenReturn(Optional.empty());

        //when then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> customerOrderService.placeMemberOrder(saveRequest));
        assertEquals("존재하지 않는 주문입니다.", exception.getMessage());
    }

    @Test
    void placeMemberOrder_member_not_found() {
        String memberEmail = "test@email.com";
        String orderId = "testOrderId";
        MemberOrderSaveRequestDto saveRequest = new MemberOrderSaveRequestDto(memberEmail, orderId);
        Orders mockOrder = mock(Orders.class);

        when(orderRepository.findById(saveRequest.getOrderId())).thenReturn(Optional.of(mockOrder));
        when(memberRepository.findByEmail(memberEmail)).thenReturn(Optional.empty());

        //when then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> customerOrderService.placeMemberOrder(saveRequest));
        assertEquals("존재하지 않는 회원입니다.", exception.getMessage());
    }

    @Test
    void placeNonMemberOrder() {
        String orderId = "testOrderId";
        String password = "testPassword";
        Long nonMemberOrderId = 1L;
        NonMemberOrderSaveRequestDto saveRequest = new NonMemberOrderSaveRequestDto(orderId, password);
        Orders mockOrder = mock(Orders.class);
        NonMemberOrder mockNonMemberOrder = mock(NonMemberOrder.class);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));
        when(nonMemberOrderRepository.save(any(NonMemberOrder.class))).thenReturn(mockNonMemberOrder);
        when(mockNonMemberOrder.getId()).thenReturn(nonMemberOrderId);

        //when
        Long result = customerOrderService.placeNonMemberOrder(saveRequest);
        assertNotNull(result);
        assertEquals(nonMemberOrderId, result);
    }

    @Test
    void placeNonMemberOrder_order_not_found() {
        String orderId = "testOrderId";
        String password = "testPassword";
        NonMemberOrderSaveRequestDto saveRequest = new NonMemberOrderSaveRequestDto(orderId, password);
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        //when
        NotFoundException exception = assertThrows(NotFoundException.class, () -> customerOrderService.placeNonMemberOrder(saveRequest));
        assertEquals("존재하지 않는 주문입니다.", exception.getMessage());
    }

    @Test
    void placeCustomerOrder_member_order() {
        String memberEmail = "test@email.com";
        String orderId = "testOrderId";
        Long memberOrderId = 1L;
        MemberOrderSaveRequestDto saveRequest = new MemberOrderSaveRequestDto(memberEmail, orderId);
        Orders mockOrder = mock(Orders.class);
        Member mockMember = mock(Member.class);
        MemberOrder mockMemberOrder = mock(MemberOrder.class);
        when(orderRepository.findById(saveRequest.getOrderId())).thenReturn(Optional.of(mockOrder));
        when(memberRepository.findByEmail(memberEmail)).thenReturn(Optional.of(mockMember));
        when(memberOrderRepository.save(any(MemberOrder.class))).thenReturn(mockMemberOrder);
        when(mockMemberOrder.getId()).thenReturn(memberOrderId);

        //when
        Long result = customerOrderService.placeCustomerOrder(orderId, memberOrderRequest);
        assertNotNull(result);
        assertEquals(memberOrderId, result);
        verify(memberOrderRepository).save(any(MemberOrder.class));
    }

    @Test
    void placeCustomerOrder_non_member_order() {
        String orderId = "testOrderId";
        String password = "testPassword";
        Long nonMemberOrderId = 1L;
        NonMemberOrderSaveRequestDto saveRequest = new NonMemberOrderSaveRequestDto(orderId, password);
        Orders mockOrder = mock(Orders.class);
        NonMemberOrder mockNonMemberOrder = mock(NonMemberOrder.class);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));
        when(nonMemberOrderRepository.save(any(NonMemberOrder.class))).thenReturn(mockNonMemberOrder);
        when(mockNonMemberOrder.getId()).thenReturn(nonMemberOrderId);

        //when
        Long result = customerOrderService.placeCustomerOrder(orderId, nonMemberOrderRequest);
        assertNotNull(result);
        assertEquals(nonMemberOrderId, result);
        verify(nonMemberOrderRepository).save(any(NonMemberOrder.class));
    }

    @Test
    void placeCustomerOrder_orderType_mismatch() {
        String orderId = "testOrderId";
        OrderRequestDto orderRequest = mock(OrderRequestDto.class);
        when(orderRequest.getOrderType()).thenReturn(null);

        //when then
        RuntimeException exception = assertThrows(RuntimeException.class, ()
                -> customerOrderService.placeCustomerOrder(orderId, orderRequest));
        assertEquals("잘못된 요청파라미터입니다.", exception.getMessage());
    }
}