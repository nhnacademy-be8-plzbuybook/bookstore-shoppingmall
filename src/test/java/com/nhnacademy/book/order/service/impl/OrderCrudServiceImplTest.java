package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.book.dto.response.BookDetailResponseDto;
import com.nhnacademy.book.book.service.Impl.SellingBookService;
import com.nhnacademy.book.order.dto.orderRequests.OrderDeliveryAddressDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.order.dto.orderResponse.OrderResponseDto;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.enums.OrderStatus;
import com.nhnacademy.book.order.enums.OrderType;
import com.nhnacademy.book.order.repository.OrderRepository;
import com.nhnacademy.book.order.service.OrderProductCouponService;
import com.nhnacademy.book.order.service.impl.OrderCrudServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderCrudServiceImplTest {

    @InjectMocks
    private OrderCrudServiceImpl orderCrudService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private SellingBookService sellingBookService;

    @Mock
    private OrderProductCouponService orderProductCouponService;

    private OrderRequestDto orderRequest;

    @BeforeEach
    void setUp() {
        OrderType orderType = OrderType.NON_MEMBER_ORDER;
        LocalDate deliveryWishDate = LocalDate.now();
        Integer usedPoint = 0;
        List<OrderProductRequestDto> orderProducts = List.of(mock(OrderProductRequestDto.class), mock(OrderProductRequestDto.class));
        OrderDeliveryAddressDto orderDeliveryAddress = mock(OrderDeliveryAddressDto.class);
        BigDecimal deliveryFee = BigDecimal.valueOf(3_000);
        BigDecimal orderPrice = BigDecimal.valueOf(20_000);
        String nonMemberPassword = "password123";

        orderRequest = new OrderRequestDto(orderType, deliveryWishDate, usedPoint, orderProducts,
                orderDeliveryAddress, deliveryFee, orderPrice, null, nonMemberPassword);
    }

    @Test
    void createOrder_success() {
        // Arrange
        String mockOrderId = UUID.randomUUID().toString();
        String mockOrderName = "수학의 정석 외 1 건";
        BigDecimal mockOrderPrice = BigDecimal.valueOf(23_000);
        BigDecimal mockCouponDiscount = BigDecimal.valueOf(2_000);

        // Mocking repository save behavior
        Orders mockOrder = Orders.builder()
                .id(mockOrderId)
                .name(mockOrderName)
                .orderPrice(mockOrderPrice)
                .status(OrderStatus.PAYMENT_PENDING)
                .build();

        when(orderRepository.save(any(Orders.class))).thenReturn(mockOrder);
        when(orderProductCouponService.calculateCouponDiscounts(orderRequest.getOrderProducts())).thenReturn(mockCouponDiscount);

        BookDetailResponseDto bookDetail = new BookDetailResponseDto();
        bookDetail.setBookTitle("Book Title");
        when(sellingBookService.getSellingBook(anyLong())).thenReturn(bookDetail);

        // when
        OrderResponseDto response = orderCrudService.createOrder(orderRequest);

        // then
        assertNotNull(response);
        assertEquals(mockOrderId, response.getOrderId());
        assertEquals(mockOrderName, response.getOrderName());
        assertEquals(mockOrderPrice.subtract(mockCouponDiscount).subtract(BigDecimal.valueOf(orderRequest.getUsedPoint())), response.getAmount());

        verify(orderRepository).save(any(Orders.class));
        verify(orderProductCouponService).calculateCouponDiscounts(orderRequest.getOrderProducts());
        verify(sellingBookService).getSellingBook(anyLong());
    }
}