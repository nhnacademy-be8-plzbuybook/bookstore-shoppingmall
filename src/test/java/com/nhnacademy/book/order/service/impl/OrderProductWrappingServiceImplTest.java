package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.entity.OrderProductWrapping;
import com.nhnacademy.book.order.repository.OrderProductWrappingRepository;
import com.nhnacademy.book.order.service.OrderCacheService;
import com.nhnacademy.book.orderProduct.dto.OrderProductWrappingDto;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.repository.OrderProductRepository;
import com.nhnacademy.book.wrappingPaper.entity.WrappingPaper;
import com.nhnacademy.book.wrappingPaper.repository.WrappingPaperRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderProductWrappingServiceImplTest {
    @Mock
    private OrderProductWrappingRepository orderProductWrappingRepository;
    @Mock
    private OrderCacheService orderCacheService;
    @Mock
    private OrderProductRepository orderProductRepository;
    @Mock
    private WrappingPaperRepository wrappingPaperRepository;
    @InjectMocks
    private OrderProductWrappingServiceImpl orderProductWrappingService;
    OrderProductWrappingDto orderProductWrappingDto;

    @BeforeEach
    void setup() {
        Long wrappingPaperId = 32L;
        Integer quantity = 1;
        BigDecimal price = BigDecimal.valueOf(1500);
        orderProductWrappingDto = new OrderProductWrappingDto(wrappingPaperId, quantity, price);
    }

    @DisplayName("주문상품포장 저장: 성공")
    @Test
    void saveOrderProductWrapping() {
        long orderProductId = 1L;
        Long wrappingPaperId = 32L;
        Long wrappingPaperStock = 100L;
        WrappingPaper wrappingPaper = spy(new WrappingPaper(wrappingPaperId, "name", BigDecimal.valueOf(3000), wrappingPaperStock, LocalDateTime.now(), "image/path"));
        when(orderProductRepository.findById(orderProductId)).thenReturn(Optional.of(mock(OrderProduct.class)));
        when(wrappingPaperRepository.findById(orderProductWrappingDto.getWrappingPaperId())).thenReturn(Optional.of(wrappingPaper));
        when(orderCacheService.getWrappingPaperStockCache(wrappingPaperId)).thenReturn(wrappingPaperStock - 1);
        when(orderProductWrappingRepository.save(any(OrderProductWrapping.class))).thenReturn(mock(OrderProductWrapping.class));

        //when
        orderProductWrappingService.saveOrderProductWrapping(orderProductId, orderProductWrappingDto);

        //then
        assertEquals(wrappingPaperStock - 1, wrappingPaper.getStock());
        verify(orderProductWrappingRepository).save(any(OrderProductWrapping.class));
        verify(wrappingPaper).setStock(wrappingPaperStock - 1);


    }

    @DisplayName("주문상품포장 저장: 주문상품 없음")
    @Test
    void saveOrderProductWrapping_orderProduct_not_found() {
        long orderProductId = 1L;
        when(orderProductRepository.findById(orderProductId)).thenReturn(Optional.empty());

        //when
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> orderProductWrappingService.saveOrderProductWrapping(orderProductId, orderProductWrappingDto));
        assertEquals("찾을 수 없는 주문상품입니다.", exception.getMessage());

    }

    @DisplayName("주문상품포장 저장: 포장지 없음")
    @Test
    void saveOrderProductWrapping_wrappingPaper_not_found() {
        long orderProductId = 1L;
        when(orderProductRepository.findById(orderProductId)).thenReturn(Optional.of(mock(OrderProduct.class)));
        when(wrappingPaperRepository.findById(orderProductWrappingDto.getWrappingPaperId())).thenReturn(Optional.empty());

        //when
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> orderProductWrappingService.saveOrderProductWrapping(orderProductId, orderProductWrappingDto));
        assertEquals("찾을 수 없는 포장지입니다.", exception.getMessage());
    }

    @DisplayName("주문상품포장 저장: 주문상품포장이 null")
    @Test
    void saveOrderProductWrapping_null() {
        long orderProductId = 1L;

        //when
        orderProductWrappingService.saveOrderProductWrapping(orderProductId, null);

        //then
        verify(orderProductRepository, never()).findById(anyLong());
        verify(wrappingPaperRepository, never()).findById(anyLong());
        verify(orderProductWrappingRepository, never()).save(any(OrderProductWrapping.class));

    }
}