package com.nhnacademy.book.orderProduct.service.impl;

import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.book.exception.SellingBookNotFoundException;
import com.nhnacademy.book.book.repository.SellingBookRepository;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.OrderProductStatusPatchRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductAppliedCouponDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductRequestDto;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.service.OrderCacheService;
import com.nhnacademy.book.order.service.OrderProductCouponService;
import com.nhnacademy.book.orderProduct.dto.OrderProductWrappingDto;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
import com.nhnacademy.book.orderProduct.repository.OrderProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderProductServiceImplTest {
    @Mock
    private OrderProductRepository orderProductRepository;
    @Mock
    private SellingBookRepository sellingBookRepository;
    @Mock
    private OrderCacheService orderCacheService;
    @Mock
    private OrderProductCouponService orderProductCouponService;
    @InjectMocks
    private OrderProductServiceImpl orderProductService;

    @DisplayName("주문상품 저장")
    @Test
    void saveOrderProduct() {
        Long productId = 1L;
        BigDecimal price = BigDecimal.valueOf(30_000);
        Integer quantity = 1;
        Long couponId = 1L;
        BigDecimal couponDiscount = BigDecimal.valueOf(3_000);
        List<OrderProductAppliedCouponDto> appliedCoupons = List.of(new OrderProductAppliedCouponDto(couponId, couponDiscount));
        Long wrappingPaperId = 1L;
        Integer wrappingPaperQuantity = 1;
        BigDecimal wrappingPaperPrice = BigDecimal.valueOf(1_500);
        OrderProductWrappingDto wrapping = new OrderProductWrappingDto(wrappingPaperId, wrappingPaperQuantity, wrappingPaperPrice);
        OrderProductRequestDto orderProductRequest = new OrderProductRequestDto(productId, price, quantity, appliedCoupons, wrapping);
        Orders mockOrder = mock(Orders.class);
        SellingBook mockSellingBook = mock(SellingBook.class);
        when(mockSellingBook.getSellingBookId()).thenReturn(productId);
        OrderProduct savedOrderProduct = OrderProduct.builder()
                .sellingBook(mockSellingBook)
                .quantity(quantity)
                .price(price)
                .status(OrderProductStatus.PAYMENT_COMPLETED)
                .couponDiscount(couponDiscount)
                .order(mockOrder)
                .build();
        when(sellingBookRepository.findById(productId)).thenReturn(Optional.of(mockSellingBook));
        when(orderCacheService.getProductStockCache(productId)).thenReturn(10);
        when(orderProductCouponService.calculateCouponDiscount(orderProductRequest)).thenReturn(couponDiscount);
        when(orderProductRepository.save(any(OrderProduct.class))).thenReturn(savedOrderProduct);

        //when
        OrderProduct result = orderProductService.saveOrderProduct(mockOrder, orderProductRequest);

        //then
        assertNotNull(result);
        assertEquals(mockSellingBook, result.getSellingBook());
        assertEquals(orderProductRequest.getQuantity(), result.getQuantity());
        assertEquals(orderProductRequest.getPrice(), result.getPrice());
        assertEquals(OrderProductStatus.PAYMENT_COMPLETED, result.getStatus());
        assertEquals(couponDiscount, result.getCouponDiscount());
        assertEquals(mockOrder, result.getOrder());

        // Verify interactions
        verify(sellingBookRepository).findById(orderProductRequest.getProductId());
        verify(orderCacheService).getProductStockCache(mockSellingBook.getSellingBookId());
        verify(orderProductCouponService).calculateCouponDiscount(orderProductRequest);
        verify(orderProductRepository).save(any(OrderProduct.class));
    }

    @DisplayName("주문상품 저장: 상품 없음")
    @Test
    void saveOrderProduct_cant_find_product() {
        Long productId = 1L;
        BigDecimal price = BigDecimal.valueOf(30_000);
        Integer quantity = 1;
        Long couponId = 1L;
        BigDecimal couponDiscount = BigDecimal.valueOf(3_000);
        List<OrderProductAppliedCouponDto> appliedCoupons = List.of(new OrderProductAppliedCouponDto(couponId, couponDiscount));
        Long wrappingPaperId = 1L;
        Integer wrappingPaperQuantity = 1;
        BigDecimal wrappingPaperPrice = BigDecimal.valueOf(1_500);
        OrderProductWrappingDto wrapping = new OrderProductWrappingDto(wrappingPaperId, wrappingPaperQuantity, wrappingPaperPrice);
        OrderProductRequestDto orderProductRequest = new OrderProductRequestDto(productId, price, quantity, appliedCoupons, wrapping);
        Orders mockOrder = mock(Orders.class);

        when(sellingBookRepository.findById(productId)).thenThrow(new SellingBookNotFoundException("찾을 수 없는 상품입니다."));

        //when then
        SellingBookNotFoundException exception = assertThrows(SellingBookNotFoundException.class, ()
                -> orderProductService.saveOrderProduct(mockOrder, orderProductRequest));
        assertEquals("찾을 수 없는 상품입니다.", exception.getMessage());
    }

    @DisplayName("주문상품 단건조회: 판매책 아이디 있음")
    @Test
    void findOrderProductBySellingBookId_present() {
        Long productId = 1L;

        OrderProduct mockOrderProduct = mock(OrderProduct.class);
        when(orderProductRepository.findBySellingBook_SellingBookId(productId)).thenReturn(Optional.ofNullable(mockOrderProduct));

        //when
        Optional<OrderProduct> orderProduct = orderProductService.findOrderProductBySellingBookId(productId);

        assertTrue(orderProduct.isPresent());
        verify(orderProductRepository).findBySellingBook_SellingBookId(productId);
    }

    @DisplayName("주문상품 단건조회: 판매책 아이디 없음")
    @Test
    void findOrderProductBySellingBookId_empty() {
        Long productId = 1L;

        when(orderProductRepository.findBySellingBook_SellingBookId(productId)).thenReturn(Optional.empty());

        //when
        Optional<OrderProduct> orderProduct = orderProductService.findOrderProductBySellingBookId(productId);

        assertTrue(orderProduct.isEmpty());
        verify(orderProductRepository).findBySellingBook_SellingBookId(productId);
    }

    @DisplayName("주문상품상태 수정")
    @Test
    void patchStatus() {
        Long orderProductId = 1L;
        OrderProductStatusPatchRequestDto patchRequest = new OrderProductStatusPatchRequestDto(OrderProductStatus.ORDER_CANCELLED);
        OrderProduct mockOrderProduct = mock(OrderProduct.class);
        doNothing().when(mockOrderProduct).updateStatus(patchRequest.getStatus());

        when(orderProductRepository.findById(orderProductId)).thenReturn(Optional.of(mockOrderProduct));

        //when
        orderProductService.patchStatus(orderProductId, patchRequest);

        verify(mockOrderProduct).updateStatus(patchRequest.getStatus());
    }

    @DisplayName("주문상품상태 수정: 주문상품이 없음")
    @Test
    void patchStatus_cant_find_orderProduct() {
        Long orderProductId = 1L;
        OrderProductStatusPatchRequestDto patchRequest = new OrderProductStatusPatchRequestDto(OrderProductStatus.ORDER_CANCELLED);
        OrderProduct mockOrderProduct = mock(OrderProduct.class);

        when(orderProductRepository.findById(orderProductId)).thenThrow(new NotFoundException("주문상품을 찾을 수 없습니다. 주문상품 아이디: " + orderProductId));

        //when then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> orderProductService.patchStatus(orderProductId, patchRequest));

        assertEquals("주문상품을 찾을 수 없습니다. 주문상품 아이디: " + orderProductId, exception.getMessage());
        verify(mockOrderProduct, never()).updateStatus(any());
    }

    @DisplayName("주문상품 구매확정")
    @Test
    void purchaseConfirmOrderProduct() {
        Long orderProductId = 1L;
        OrderProduct mockOrderProduct = mock(OrderProduct.class);
        doNothing().when(mockOrderProduct).updateStatus(OrderProductStatus.PURCHASE_CONFIRMED);

        when(orderProductRepository.findById(orderProductId)).thenReturn(Optional.of(mockOrderProduct));

        //when
        orderProductService.purchaseConfirmOrderProduct(orderProductId);

        verify(mockOrderProduct).updateStatus(OrderProductStatus.PURCHASE_CONFIRMED);
    }

    @DisplayName("주문상품 구매확정: 주문상품 없음")
    @Test
    void purchaseConfirmOrderProduct_cant_find_orderProduct() {
        Long orderProductId = 1L;
        OrderProduct mockOrderProduct = mock(OrderProduct.class);

        when(orderProductRepository.findById(orderProductId)).thenThrow(new NotFoundException("주문상품을 찾을 수 없습니다. 주문상품 아이디: " + orderProductId));

        //when
        NotFoundException exception = assertThrows(NotFoundException.class, () -> orderProductService.purchaseConfirmOrderProduct(orderProductId));

        assertEquals("주문상품을 찾을 수 없습니다. 주문상품 아이디: " + orderProductId, exception.getMessage());
        verify(mockOrderProduct, never()).updateStatus(any());
    }


    @DisplayName("주문상품 재고 추가")
    @Test
    void addOrderProductStock() {
        Long orderProductId = 1L;
        int quantity = 1;
        OrderProduct mockOrderProduct = mock(OrderProduct.class);
        SellingBook mockSellingBook = mock(SellingBook.class);

        when(mockOrderProduct.getSellingBook()).thenReturn(mockSellingBook);
        when(orderProductRepository.findById(orderProductId)).thenReturn(Optional.of(mockOrderProduct));
        when(mockSellingBook.getSellingBookStock()).thenReturn(10);

        //when then
        orderProductService.addOrderProductStock(orderProductId, quantity);

        verify(mockSellingBook).setSellingBookStock(10 + quantity);
    }

    @DisplayName("주문상품 재고 추가: 주문상품이 없음")
    @Test
    void addOrderProductStock_cant_find_orderProduct() {
        Long orderProductId = 1L;
        int quantity = 1;
        SellingBook mockSellingBook = mock(SellingBook.class);

        when(orderProductRepository.findById(orderProductId)).thenThrow(new NotFoundException("주문상품을 찾을 수 없습니다. 주문상품 아이디: " + orderProductId));

        //when then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> orderProductService.addOrderProductStock(orderProductId, quantity));

        assertEquals("주문상품을 찾을 수 없습니다. 주문상품 아이디: " + orderProductId, exception.getMessage());
        verify(mockSellingBook, never()).setSellingBookStock(anyInt());
    }
}