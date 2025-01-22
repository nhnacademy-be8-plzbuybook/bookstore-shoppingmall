package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.coupon.dto.ValidationCouponCalculationRequestDto;
import com.nhnacademy.book.coupon.service.CouponService;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductAppliedCouponDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductRequestDto;
import com.nhnacademy.book.order.entity.OrderProductCoupon;
import com.nhnacademy.book.order.repository.OrderProductCouponRepository;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.repository.OrderProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderProductCouponServiceImplTest {
    @Mock
    private OrderProductRepository orderProductRepository;
    @Mock
    private OrderProductCouponRepository orderProductCouponRepository;
    @InjectMocks
    private OrderProductCouponServiceImpl orderProductCouponService;
    private List<OrderProductAppliedCouponDto> appliedCoupons;
    @Mock
    private CouponService couponService;

    @BeforeEach
    void setup() {
        Long couponId1 = 123L;
        Long couponId2 = 13L;
        BigDecimal discount1 = BigDecimal.valueOf(3_000);
        BigDecimal discount2 = BigDecimal.valueOf(3_000);
        OrderProductAppliedCouponDto appliedCoupon1 = new OrderProductAppliedCouponDto(couponId1, discount1);
        OrderProductAppliedCouponDto appliedCoupon2 = new OrderProductAppliedCouponDto(couponId2, discount2);
        appliedCoupons = List.of(appliedCoupon1, appliedCoupon2);
    }

    @DisplayName("적용쿠폰 저장")
    @Test
    void saveOrderProductCoupon() {
        Long orderProductId = 1L;
        OrderProduct mockOrderProduct = mock(OrderProduct.class);
        when(orderProductRepository.findById(orderProductId)).thenReturn(Optional.of(mockOrderProduct));
        when(orderProductCouponRepository.save(any(OrderProductCoupon.class))).thenReturn(mock(OrderProductCoupon.class));

        //when
        Long result = orderProductCouponService.saveOrderProductCoupon(orderProductId, appliedCoupons);
        assertNotNull(result);
        assertEquals(orderProductId, result);

        for (OrderProductAppliedCouponDto orderProductAppliedCouponDto : appliedCoupons) {
            verify(couponService, times(1))
                    .validateCouponCalculation(orderProductAppliedCouponDto.getCouponId(), new ValidationCouponCalculationRequestDto(orderProductAppliedCouponDto.getDiscount()));
            verify(couponService, times(1)).useCoupon(orderProductAppliedCouponDto.getCouponId());
        }

        verify(orderProductCouponRepository, times(appliedCoupons.size())).save(any(OrderProductCoupon.class));
    }

    @DisplayName("적용쿠폰 저장: 적용쿠폰 없음")
    @Test
    void saveOrderProductCoupon_applied_coupon_null() {
        Long orderProductId = 1L;
        when(orderProductRepository.findById(orderProductId)).thenReturn(Optional.of(mock(OrderProduct.class)));

        //when
        Long result = orderProductCouponService.saveOrderProductCoupon(orderProductId, Collections.emptyList());

        assertNotNull(result);
        assertEquals(orderProductId, result);

        verify(couponService, never()).validateCouponCalculation(anyLong(), any());
        verify(couponService, never()).useCoupon(anyLong());
        verify(orderProductCouponRepository, never()).save(any(OrderProductCoupon.class));
    }

    @DisplayName("적용쿠폰 저장: 주문상품 없음")
    @Test
    void saveOrderProductCoupon_orderProduct_not_found() {
        Long orderProductId = 1L;
        Long couponId = 123L;
        BigDecimal discount = BigDecimal.valueOf(3_000);
        OrderProductAppliedCouponDto appliedCoupon = new OrderProductAppliedCouponDto(couponId, discount);

        when(orderProductRepository.findById(orderProductId)).thenReturn(Optional.empty());

        //when
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> orderProductCouponService.saveOrderProductCoupon(orderProductId, List.of(appliedCoupon)));
        assertEquals("찾을 수 없는 주문상품입니다.", exception.getMessage());
    }

    @DisplayName("주문상품 쿠폰할인액 계산")
    @Test
    void calculateCouponDiscount() {
        Long productId = 1L;
        BigDecimal price = BigDecimal.valueOf(30_000);
        Integer quantity = 1;
        OrderProductRequestDto orderProductRequestDto = new OrderProductRequestDto(productId, price, quantity, appliedCoupons, null);

        //when
        BigDecimal result = orderProductCouponService.calculateCouponDiscount(orderProductRequestDto);

        //then
        assertNotNull(result);
        assertEquals(0, BigDecimal.valueOf(6_000).compareTo(result));
    }


    @DisplayName("주문상품 리스트 쿠폰할인액 계산")
    @Test
    void calculateCouponDiscounts() {
        OrderProductRequestDto orderProductRequestDto1 = new OrderProductRequestDto(1L, BigDecimal.valueOf(30_000), 1, appliedCoupons, null);
        OrderProductRequestDto orderProductRequestDto2 = new OrderProductRequestDto(2L, BigDecimal.valueOf(30_000), 1, appliedCoupons, null);

        BigDecimal result = orderProductCouponService.calculateCouponDiscounts(List.of(orderProductRequestDto1, orderProductRequestDto2));
        assertNotNull(result);
        assertEquals(0, BigDecimal.valueOf(12_000).compareTo(result));
    }
}