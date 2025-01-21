package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.OrderProductReturnDto;
import com.nhnacademy.book.order.dto.OrderProductReturnRequestDto;
import com.nhnacademy.book.order.dto.OrderReturnSearchRequestDto;
import com.nhnacademy.book.order.entity.OrderProductReturn;
import com.nhnacademy.book.order.exception.OrderReturnBadRequestException;
import com.nhnacademy.book.order.repository.OrderProductReturnRepository;
import com.nhnacademy.book.order.repository.OrderReturnQueryRepository;
import com.nhnacademy.book.order.service.OrderStatusService;
import com.nhnacademy.book.order.service.OrderValidationService;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
import com.nhnacademy.book.orderProduct.repository.OrderProductRepository;
import com.nhnacademy.book.orderProduct.service.OrderProductService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderReturningServiceImplTest {
    @Mock
    private OrderReturnQueryRepository orderReturnQueryRepository;
    @Mock
    private OrderProductRepository orderProductRepository;
    @Mock
    private OrderProductReturnRepository orderProductReturnRepository;
    @Mock
    private OrderStatusService orderStatusService;
    @Mock
    private OrderValidationService orderValidationService;
    @Mock
    private OrderProductService orderProductService;
    @Mock
    private ReturnPointServiceImpl returnPointService;
    @InjectMocks
    private OrderReturningServiceImpl orderReturningService;

    @DisplayName("주문상품 반품요청")
    @Test
    void requestOrderProductReturn() {
        String orderId = "orderId";
        Long orderProductId = 1L;
        String reason = "returnReason";
        Integer quantity = 1;
        Integer orderedQuantity = 3;
        String trackingNumber = "123123123";
        OrderProductReturnRequestDto returnRequest = new OrderProductReturnRequestDto(reason, quantity, trackingNumber);
        OrderProduct mockOrderProduct = mock(OrderProduct.class);
        when(orderProductRepository.findById(orderProductId)).thenReturn(Optional.of(mockOrderProduct));
        when(mockOrderProduct.getQuantity()).thenReturn(orderedQuantity);
        doNothing().when(orderValidationService).validateOrderProductForReturning(mockOrderProduct);
        when(orderProductReturnRepository.save(any(OrderProductReturn.class))).thenReturn(mock(OrderProductReturn.class));
        doNothing().when(orderStatusService).modifyOrderProductStatus(orderProductId, OrderProductStatus.RETURN_REQUESTED);

        //when
        orderReturningService.requestOrderProductReturn(orderId, orderProductId, returnRequest);

        //then
        verify(orderValidationService).validateOrderProductForReturning(mockOrderProduct);
        verify(orderProductReturnRepository).save(any(OrderProductReturn.class));
        verify(orderStatusService).modifyOrderProductStatus(orderProductId, OrderProductStatus.RETURN_REQUESTED);
    }

    @DisplayName("주문상품 반품요청: 주문상품 없음")
    @Test
    void requestOrderProductReturn_orderProduct_not_found() {
        String orderId = "orderId";
        Long orderProductId = 1L;
        String reason = "returnReason";
        Integer quantity = 1;
        Integer orderedQuantity = 3;
        String trackingNumber = "123123123";
        OrderProductReturnRequestDto returnRequest = new OrderProductReturnRequestDto(reason, quantity, trackingNumber);
        OrderProduct mockOrderProduct = mock(OrderProduct.class);
        when(orderProductRepository.findById(orderProductId)).thenReturn(Optional.empty());

        //when
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> orderReturningService.requestOrderProductReturn(orderId, orderProductId, returnRequest));

        //then
        assertEquals("주문상품정보를 찾을 수 없습니다.", exception.getMessage());
        verify(orderValidationService, never()).validateOrderProductForReturning(mockOrderProduct);
        verify(orderProductReturnRepository, never()).save(any(OrderProductReturn.class));
        verify(orderStatusService, never()).modifyOrderProductStatus(orderProductId, OrderProductStatus.RETURN_REQUESTED);
    }

    @DisplayName("주문상품 반품요청: 반품수량이 주문수량 초과")
    @Test
    void requestOrderProductReturn_over_orderProduct_quantity() {
        String orderId = "orderId";
        Long orderProductId = 1L;
        String reason = "returnReason";
        Integer quantity = 5;
        Integer orderedQuantity = 3;
        String trackingNumber = "123123123";
        OrderProductReturnRequestDto returnRequest = new OrderProductReturnRequestDto(reason, quantity, trackingNumber);
        OrderProduct mockOrderProduct = mock(OrderProduct.class);
        when(orderProductRepository.findById(orderProductId)).thenReturn(Optional.of(mockOrderProduct));
        when(mockOrderProduct.getQuantity()).thenReturn(orderedQuantity);

        //when
        OrderReturnBadRequestException exception = assertThrows(OrderReturnBadRequestException.class,
                () -> orderReturningService.requestOrderProductReturn(orderId, orderProductId, returnRequest));

        //then
        assertEquals("반품 수량이 주문한 수량을 초과할 수 없다!", exception.getMessage());
        verify(orderValidationService, never()).validateOrderProductForReturning(mockOrderProduct);
        verify(orderProductReturnRepository, never()).save(any(OrderProductReturn.class));
        verify(orderStatusService, never()).modifyOrderProductStatus(orderProductId, OrderProductStatus.RETURN_REQUESTED);
    }

    @DisplayName("주문상품 반품요청 완료처리")
    @Test
    void completeOrderProductReturn() {
        Long orderProductId = 1L;
        OrderProduct mockOrderProduct= mock(OrderProduct.class);
        when(orderProductRepository.findById(orderProductId)).thenReturn(Optional.of(mockOrderProduct));
        OrderProductReturn orderProductReturn = spy(new OrderProductReturn(1L, "reason", "trackingNumber", 1, LocalDateTime.now(), null, mockOrderProduct));
        when(orderProductReturnRepository.findByOrderProduct(mockOrderProduct)).thenReturn(Optional.of(orderProductReturn));

        //when
        Long result = orderReturningService.completeOrderProductReturn(orderProductId);

        //then
        assertEquals(1L, result);
        assertNotNull(orderProductReturn.getCompletedAt());
        verify(orderProductService).addOrderProductStock(orderProductId, orderProductReturn.getQuantity());
        verify(returnPointService).returnPoint(orderProductId);
        verify(orderStatusService).modifyOrderProductStatus(orderProductId, OrderProductStatus.RETURN_COMPLETED);
        verify(orderProductReturn).complete();
    }

    @DisplayName("주문상품 반품요청 완료처리: 주문상품정보 찾을 수 없음")
    @Test
    void completeOrderProductReturn_orderProduct_not_found() {
        Long orderProductId = 1L;
        when(orderProductRepository.findById(orderProductId)).thenReturn(Optional.empty());

        //when then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> orderReturningService.completeOrderProductReturn(orderProductId));
        assertEquals("주문상품정보를 찾을 수 없습니다.", exception.getMessage());
        verify(orderProductService, never()).addOrderProductStock(anyLong(), anyInt());
        verify(returnPointService, never()).returnPoint(orderProductId);
    }

    @DisplayName("주문상품 반품요청 완료처리: 반품요청 찾을 수 없음")
    @Test
    void completeOrderProductReturn_return_request_not_found() {
        Long orderProductId = 1L;
        OrderProduct mockOrderProduct= mock(OrderProduct.class);
        when(orderProductRepository.findById(orderProductId)).thenReturn(Optional.of(mockOrderProduct));
        when(orderProductReturnRepository.findByOrderProduct(mockOrderProduct)).thenReturn(Optional.empty());

        //when then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> orderReturningService.completeOrderProductReturn(orderProductId));
        assertEquals("주문상품반품 정보를 찾을 수 없습니다.", exception.getMessage());
        verify(orderProductService, never()).addOrderProductStock(anyLong(), anyInt());
        verify(returnPointService, never()).returnPoint(orderProductId);
    }

    @Test
    void getAllOrderProductReturns() {
        // Given
        OrderReturnSearchRequestDto searchRequest = new OrderReturnSearchRequestDto("trackingNumber", "status");
        Pageable pageable = PageRequest.of(0, 10);

        // Mock 데이터 생성
        OrderProductReturnDto mockDto1 = new OrderProductReturnDto(1L, "reason1", 1, "trackingNumber1", LocalDateTime.now(), null, "orderId1", 1L);
        OrderProductReturnDto mockDto2 = new OrderProductReturnDto(2L, "reason2", 2, "trackingNumber2", LocalDateTime.now(), null, "orderId2", 2L);
        Page<OrderProductReturnDto> mockPage = new PageImpl<>(List.of(mockDto1, mockDto2), pageable, 2);

        // Mocking: orderReturnQueryRepository 동작 설정
        when(orderReturnQueryRepository.findOrderProductReturnPage(searchRequest, pageable)).thenReturn(mockPage);

        // When
        Page<OrderProductReturnDto> result = orderReturningService.getAllOrderProductReturns(searchRequest, pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals(mockDto1, result.getContent().get(0));
        assertEquals(mockDto2, result.getContent().get(1));

        verify(orderReturnQueryRepository).findOrderProductReturnPage(searchRequest, pageable);
    }
}