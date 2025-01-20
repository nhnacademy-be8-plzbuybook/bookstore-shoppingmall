package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.book.repository.SellingBookRepository;
import com.nhnacademy.book.coupon.dto.CouponCalculationRequestDto;
import com.nhnacademy.book.coupon.dto.ValidationCouponCalculation;
import com.nhnacademy.book.coupon.service.CouponService;
import com.nhnacademy.book.deliveryFeePolicy.dto.DeliveryFeeCalculateRequestDto;
import com.nhnacademy.book.deliveryFeePolicy.exception.ConflictException;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.deliveryFeePolicy.exception.StockNotEnoughException;
import com.nhnacademy.book.deliveryFeePolicy.service.DeliveryFeePolicyService;
import com.nhnacademy.book.order.dto.orderRequests.OrderDeliveryAddressDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductAppliedCouponDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.enums.OrderType;
import com.nhnacademy.book.order.exception.PriceMismatchException;
import com.nhnacademy.book.order.service.OrderCacheService;
import com.nhnacademy.book.order.service.OrderDeliveryService;
import com.nhnacademy.book.order.service.OrderValidationService;
import com.nhnacademy.book.orderProduct.dto.OrderProductWrappingDto;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
import com.nhnacademy.book.wrappingPaper.entity.WrappingPaper;
import com.nhnacademy.book.wrappingPaper.repository.WrappingPaperRepository;
import org.joda.time.Days;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderValidationServiceImplTest {
    @Mock
    private SellingBookRepository sellingBookRepository;
    @Mock
    private WrappingPaperRepository wrappingPaperRepository;
    @Mock
    private OrderCacheService orderCacheService;
    @Mock
    private DeliveryFeePolicyService deliveryFeePolicyService;
    @Mock
    private OrderDeliveryService orderDeliveryService;
    @Mock
    private CouponService couponService;
    @InjectMocks
    private OrderValidationServiceImpl orderValidationService;

    private OrderValidationServiceImpl spyOrderValidationService;

    @BeforeEach
    void setup() {
        spyOrderValidationService = spy(orderValidationService);
    }

    @DisplayName("주문 전체 검증")
    @Test
    void validateOrder() {
        OrderType orderType = OrderType.NON_MEMBER_ORDER;
        LocalDate deliveryWishDate = LocalDate.now();
        Integer usedPoint = 0;
        List<OrderProductRequestDto> orderProducts = List.of(mock(OrderProductRequestDto.class), mock(OrderProductRequestDto.class));
        OrderDeliveryAddressDto orderDeliveryAddress = mock(OrderDeliveryAddressDto.class);
        BigDecimal deliveryFee = BigDecimal.valueOf(3_000);
        BigDecimal orderPrice = BigDecimal.valueOf(20_000);
        String nonMemberPassword = "password123";
        OrderRequestDto orderRequestDto = new OrderRequestDto(orderType, deliveryWishDate, usedPoint, orderProducts,
                orderDeliveryAddress, deliveryFee, orderPrice, null, nonMemberPassword);
        doNothing().when(spyOrderValidationService).validateOrderProduct(any(OrderProductRequestDto.class));
        doNothing().when(spyOrderValidationService).validatePoint(usedPoint);
        doNothing().when(spyOrderValidationService).validateDeliveryFee(orderRequestDto);
        doNothing().when(spyOrderValidationService).validateDeliveryWishDate(deliveryWishDate);

        //when
        spyOrderValidationService.validateOrder(orderRequestDto);

        //then
        verify(spyOrderValidationService, times(orderProducts.size())).validateOrderProduct(any(OrderProductRequestDto.class));
        verify(spyOrderValidationService).validatePoint(usedPoint);
        verify(spyOrderValidationService).validateDeliveryFee(orderRequestDto);
        verify(spyOrderValidationService).validateDeliveryWishDate(deliveryWishDate);
    }


    @DisplayName("주문상품 검증: 전체 검증 성공")
    @Test
    void validateOrderProduct_success() {
        // Arrange
        Long productId = 1L;
        BigDecimal price = BigDecimal.valueOf(10_000);
        Integer quantity = 1;
        Long wrappingPaperId = 2L;
        Integer wrappingPaperQuantity = 1;
        BigDecimal wrappingPaperPrice = BigDecimal.valueOf(3_000);
        BigDecimal couponDiscount = BigDecimal.valueOf(1_000);
        Long couponId = 3L;

        OrderProductWrappingDto wrappingDto = spy(new OrderProductWrappingDto(wrappingPaperId, wrappingPaperQuantity, wrappingPaperPrice));
        OrderProductAppliedCouponDto couponDto = spy(new OrderProductAppliedCouponDto(couponId, couponDiscount));
        OrderProductRequestDto orderProductRequestDto = spy(new OrderProductRequestDto(productId, price, quantity, List.of(couponDto), wrappingDto));

        SellingBook mockSellingBook = mock(SellingBook.class);

        WrappingPaper mockWrappingPaper = mock(WrappingPaper.class);
        ValidationCouponCalculation validationCouponCalculation = new ValidationCouponCalculation(price.subtract(couponDiscount));

        when(mockSellingBook.getSellingBookId()).thenReturn(productId);
        when(sellingBookRepository.findById(productId)).thenReturn(Optional.of(mockSellingBook));
        when(orderCacheService.getProductStockCache(productId)).thenReturn(100);
        when(mockSellingBook.getSellingBookPrice()).thenReturn(price);

        when(wrappingPaperRepository.findById(wrappingPaperId)).thenReturn(Optional.of(mockWrappingPaper));
        when(orderCacheService.getWrappingPaperStockCache(wrappingPaperId)).thenReturn(100L);
        when(mockWrappingPaper.getPrice()).thenReturn(wrappingPaperPrice);

        when(couponService.validateCouponCalculation(eq(couponId), any(CouponCalculationRequestDto.class))).thenReturn(validationCouponCalculation);

        // Act
        orderValidationService.validateOrderProduct(orderProductRequestDto);

        // Assert
        verify(orderProductRequestDto).getQuantity();
        verify(wrappingDto).getQuantity();
        verify(couponService).validateCouponCalculation(eq(couponId), any(CouponCalculationRequestDto.class));
    }

    @DisplayName("주문상품 검증: 판매책 없음")
    @Test
    void validateOrderProduct_no_selling_book() {
        // Arrange
        Long productId = 1L;
        BigDecimal price = BigDecimal.valueOf(10_000);
        Integer quantity = 1;

        OrderProductRequestDto orderProductRequestDto = spy(new OrderProductRequestDto(productId, price, quantity, null, null));

        when(sellingBookRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> orderValidationService.validateOrderProduct(orderProductRequestDto));

        assertEquals("찾을 수 없는 상품입니다.", exception.getMessage());
    }

    @DisplayName("배송비검증: 배송비 일치")
    @Test
    void validateDeliveryFee() {
        OrderRequestDto orderRequestDto = mock(OrderRequestDto.class);
        BigDecimal deliveryFee = BigDecimal.valueOf(3_000);
        when(orderRequestDto.getDeliveryFee()).thenReturn(deliveryFee);
        when(deliveryFeePolicyService.getCalculatedDeliveryFee(anyLong(), any(DeliveryFeeCalculateRequestDto.class))).thenReturn(deliveryFee);

        //when
        orderValidationService.validateDeliveryFee(orderRequestDto);

        //then
        verify(deliveryFeePolicyService).getCalculatedDeliveryFee(anyLong(), any(DeliveryFeeCalculateRequestDto.class));
    }

    @DisplayName("배송비검증: 배송비 불일치")
    @Test
    void validateDeliveryFee_fee_mismatch() {
        OrderRequestDto orderRequestDto = mock(OrderRequestDto.class);
        BigDecimal deliveryFee = BigDecimal.valueOf(3_000);
        when(orderRequestDto.getDeliveryFee()).thenReturn(deliveryFee);
        when(deliveryFeePolicyService.getCalculatedDeliveryFee(anyLong(), any(DeliveryFeeCalculateRequestDto.class))).thenReturn(BigDecimal.ZERO);

        //when
        PriceMismatchException exception = assertThrows(PriceMismatchException.class,
                () -> orderValidationService.validateDeliveryFee(orderRequestDto));

        //then
        assertEquals("배송비가 변동되었습니다.", exception.getMessage());
        verify(deliveryFeePolicyService).getCalculatedDeliveryFee(anyLong(), any(DeliveryFeeCalculateRequestDto.class));
    }

    @Test
    void validatePoint() {
        //TODO: 테스트 코드 작성
    }

    @DisplayName("반품 요청: 성공")
    @Test
    void validateOrderProductForReturning() {
        Long orderProductId = 1L;
        SellingBook sellingBook = new SellingBook();
        Orders order = new Orders();
        BigDecimal price = BigDecimal.valueOf(30_000);
        Integer quantity = 1;
        BigDecimal couponDiscount = BigDecimal.valueOf(3_000);
        OrderProductStatus status = OrderProductStatus.DELIVERED;
        OrderProduct orderProduct = new OrderProduct(orderProductId, sellingBook, order, price, quantity, couponDiscount, status);

        when(orderDeliveryService.isInReturnablePeriod(orderProduct.getOrder())).thenReturn(true);

        //when
        orderValidationService.validateOrderProductForReturning(orderProduct);

        //then
        verify(orderDeliveryService).isInReturnablePeriod(order);
    }

    @DisplayName("반품 요청: 반품가능 상태가 아님")
    @Test
    void validateOrderProductForReturning_wrong_status() {
        Long orderProductId = 1L;
        SellingBook sellingBook = new SellingBook();
        Orders order = new Orders();
        BigDecimal price = BigDecimal.valueOf(30_000);
        Integer quantity = 1;
        BigDecimal couponDiscount = BigDecimal.valueOf(3_000);
        OrderProductStatus status = OrderProductStatus.PAYMENT_PENDING;
        OrderProduct orderProduct = new OrderProduct(orderProductId, sellingBook, order, price, quantity, couponDiscount, status);

        //when
        ConflictException exception = assertThrows(ConflictException.class,
                () -> orderValidationService.validateOrderProductForReturning(orderProduct));

        //then
        assertEquals("반품이 불가능한 주문상품입니다. (사유: 반품가능 상태가 아님)", exception.getMessage());
    }

    @DisplayName("반품 요청: 반품기간 지남")
    @Test
    void validateOrderProductForReturning_wrong_period() {
        Long orderProductId = 1L;
        SellingBook sellingBook = new SellingBook();
        Orders order = new Orders();
        BigDecimal price = BigDecimal.valueOf(30_000);
        Integer quantity = 1;
        BigDecimal couponDiscount = BigDecimal.valueOf(3_000);
        OrderProductStatus status = OrderProductStatus.DELIVERED;
        OrderProduct orderProduct = new OrderProduct(orderProductId, sellingBook, order, price, quantity, couponDiscount, status);

        when(orderDeliveryService.isInReturnablePeriod(orderProduct.getOrder())).thenReturn(false);

        //when
        ConflictException exception = assertThrows(ConflictException.class,
                () -> orderValidationService.validateOrderProductForReturning(orderProduct));

        //then
        assertEquals("반품이 불가능한 주문입니다. (사유: 반품기간 지남)", exception.getMessage());
    }

    @DisplayName("반품요청 완료처리: 성공")
    @Test
    void validateOrderProductForReturnCompletion_success() {
        OrderProduct mockOrderProduct = mock(OrderProduct.class);
        when(mockOrderProduct.getStatus()).thenReturn(OrderProductStatus.RETURN_REQUESTED);

        //when
        orderValidationService.validateOrderProductForReturnCompletion(mockOrderProduct);

        //then
        verify(mockOrderProduct).getStatus();
    }

    @DisplayName("반품요청 완료처리: 실패")
    @Test
    void validateOrderProductForReturnCompletion_fail() {
        OrderProductStatus purchaseConfirmedStatus = OrderProductStatus.PURCHASE_CONFIRMED;
        OrderProduct mockOrderProduct = mock(OrderProduct.class);
        when(mockOrderProduct.getStatus()).thenReturn(purchaseConfirmedStatus);

        //when
        ConflictException exception = assertThrows(ConflictException.class,
                () -> orderValidationService.validateOrderProductForReturnCompletion(mockOrderProduct));
        assertEquals("반품요청된 주문상품이 아닙니다.", exception.getMessage());

    }

    @DisplayName("주문취소 가능여부 검증: 성공")
    @Test
    void validateOrderProductForCanceling_success() {
        OrderProductStatus paymentCompletedStatus = OrderProductStatus.PAYMENT_COMPLETED;
        OrderProduct mockOrderProduct = mock(OrderProduct.class);
        OrderProductStatus mockOrderProductStatus = mock(OrderProductStatus.class);
        when(mockOrderProduct.getStatus()).thenReturn(mockOrderProductStatus);
        when(mockOrderProductStatus.getCode()).thenReturn(paymentCompletedStatus.getCode());

        //when
        orderValidationService.validateOrderProductForCanceling(mockOrderProduct);

        verify(mockOrderProductStatus).getCode();
    }

    @DisplayName("주문취소 가능여부 검증: 실패")
    @Test
    void validateOrderProductForCanceling() {
        OrderProductStatus purchaseConfirmedStatus = OrderProductStatus.PURCHASE_CONFIRMED;
        OrderProduct mockOrderProduct = mock(OrderProduct.class);
        OrderProductStatus mockOrderProductStatus = mock(OrderProductStatus.class);
        when(mockOrderProduct.getStatus()).thenReturn(mockOrderProductStatus);
        when(mockOrderProductStatus.getCode()).thenReturn(purchaseConfirmedStatus.getCode());

        //when
        ConflictException exception = assertThrows(ConflictException.class,
                () -> orderValidationService.validateOrderProductForCanceling(mockOrderProduct));

        assertTrue(exception.getMessage().contains("주문취소가 불가능합니다."));
        verify(mockOrderProductStatus).getCode();
    }

    @DisplayName("주문상품 검증")
    @Test
    void validateSellingBook() {
        Long productId = 1L;
        BigDecimal price = spy(BigDecimal.valueOf(10_000));
        Integer quantity = 1;
        int currentStock = 100;
        OrderProductRequestDto orderProductRequestDto = spy(new OrderProductRequestDto(productId, price, quantity, null, null));
        SellingBook mockSellingBook = mock(SellingBook.class);

        when(sellingBookRepository.findById(productId)).thenReturn(Optional.of(mockSellingBook));
        when(orderCacheService.getProductStockCache(anyLong())).thenReturn(currentStock);
        when(mockSellingBook.getSellingBookPrice()).thenReturn(price);

        //when
        orderValidationService.validateSellingBook(orderProductRequestDto);

        verify(orderProductRequestDto).getQuantity();
        verify(price).compareTo(any(BigDecimal.class));
    }

    @DisplayName("주문상품 검증: 상품 못 찾음")
    @Test
    void validateSellingBook_product_not_found() {
        Long productId = 1L;
        BigDecimal price = BigDecimal.valueOf(10_000);
        Integer quantity = 1;
        OrderProductRequestDto orderProductRequestDto = new OrderProductRequestDto(productId, price, quantity, null, null);
        when(sellingBookRepository.findById(productId)).thenReturn(Optional.empty());

        //when then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                orderValidationService.validateSellingBook(orderProductRequestDto));
        assertEquals("찾을 수 없는 상품입니다.", exception.getMessage());
    }

    @DisplayName("주문상품 검증: 재고 부족")
    @Test
    void validateSellingBook_stock_not_enough() {
        Long productId = 1L;
        BigDecimal price = BigDecimal.valueOf(10_000);
        Integer quantity = 100;
        OrderProductRequestDto orderProductRequestDto = new OrderProductRequestDto(productId, price, quantity, null, null);
        SellingBook mockSellingBook = mock(SellingBook.class);
        int currentStock = 10;

        when(sellingBookRepository.findById(productId)).thenReturn(Optional.of(mockSellingBook));
        when(orderCacheService.getProductStockCache(anyLong())).thenReturn(currentStock);

        //when then
        StockNotEnoughException exception = assertThrows(StockNotEnoughException.class,
                () -> orderValidationService.validateSellingBook(orderProductRequestDto));
        assertEquals("재고가 부족합니다.", exception.getMessage());
    }

    @DisplayName("주문상품 검증: 금액 불일치")
    @Test
    void validateSellingBook_price_mismatch() {
        Long productId = 1L;
        BigDecimal price = BigDecimal.valueOf(10_000);
        Integer quantity = 1;
        OrderProductRequestDto orderProductRequestDto = new OrderProductRequestDto(productId, price, quantity, null, null);
        SellingBook mockSellingBook = mock(SellingBook.class);
        int currentStock = 10;

        when(sellingBookRepository.findById(productId)).thenReturn(Optional.of(mockSellingBook));
        when(orderCacheService.getProductStockCache(anyLong())).thenReturn(currentStock);
        when(mockSellingBook.getSellingBookPrice()).thenReturn(BigDecimal.valueOf(100000000));

        //when
        PriceMismatchException exception = assertThrows(PriceMismatchException.class,
                () -> orderValidationService.validateSellingBook(orderProductRequestDto));
        assertTrue(exception.getMessage().contains("가격이 변동되었습니다."));
    }

    @DisplayName("포장지 검증")
    @Test
    void validateWrappingPaper() {
        Long wrappingPaperId = 1L;
        Integer quantity = 1;
        BigDecimal price = spy(BigDecimal.valueOf(3_000));
        Long currentStock = 100L;
        OrderProductWrappingDto orderProductWrapping = spy(new OrderProductWrappingDto(wrappingPaperId, quantity, price));
        WrappingPaper mockWrappingPaper = mock(WrappingPaper.class);

        when(wrappingPaperRepository.findById(wrappingPaperId)).thenReturn(Optional.of(mockWrappingPaper));
        when(orderCacheService.getWrappingPaperStockCache(wrappingPaperId)).thenReturn(currentStock);
        when(mockWrappingPaper.getPrice()).thenReturn(price);
        //when
        orderValidationService.validateWrappingPaper(orderProductWrapping);

        // 재고검증
        verify(orderProductWrapping).getQuantity();
        // 금액 검증
        verify(price).compareTo(any(BigDecimal.class));
    }

    @DisplayName("포장지 검증: 재고부족")
    @Test
    void validateWrappingPaper_out_of_stock() {
        Long wrappingPaperId = 1L;
        Integer quantity = 1;
        BigDecimal price = spy(BigDecimal.valueOf(3_000));
        Long currentStock = 0L;
        OrderProductWrappingDto orderProductWrapping = spy(new OrderProductWrappingDto(wrappingPaperId, quantity, price));
        WrappingPaper mockWrappingPaper = mock(WrappingPaper.class);

        when(wrappingPaperRepository.findById(wrappingPaperId)).thenReturn(Optional.of(mockWrappingPaper));
        when(orderCacheService.getWrappingPaperStockCache(wrappingPaperId)).thenReturn(currentStock);

        //when
        StockNotEnoughException exception = assertThrows(StockNotEnoughException.class,
                () -> orderValidationService.validateWrappingPaper(orderProductWrapping));

        // 재고검증
        verify(orderProductWrapping).getQuantity();
        assertTrue(exception.getMessage().contains("재고가 부족합니다."));
    }

    @DisplayName("포장지 검증: 금액 불일치")
    @Test
    void validateWrappingPaper_price_mismatch() {
        Long wrappingPaperId = 1L;
        Integer quantity = 1;
        BigDecimal price = spy(BigDecimal.valueOf(3_000));
        Long currentStock = 100L;
        OrderProductWrappingDto orderProductWrapping = spy(new OrderProductWrappingDto(wrappingPaperId, quantity, price));
        WrappingPaper mockWrappingPaper = mock(WrappingPaper.class);

        when(wrappingPaperRepository.findById(wrappingPaperId)).thenReturn(Optional.of(mockWrappingPaper));
        when(orderCacheService.getWrappingPaperStockCache(wrappingPaperId)).thenReturn(currentStock);
        when(mockWrappingPaper.getPrice()).thenReturn(BigDecimal.valueOf(300000000));

        //when
        PriceMismatchException exception = assertThrows(PriceMismatchException.class,
                () -> orderValidationService.validateWrappingPaper(orderProductWrapping));

        // 재고검증
        verify(orderProductWrapping).getQuantity();
        assertTrue(exception.getMessage().contains("가격이 변동되었습니다."));
    }

    @DisplayName("포장지 검증: 포장지 못찾읍")
    @Test
    void validateWrappingPaper_wrappingPaper_not_found() {
        Long wrappingPaperId = 1L;
        Integer quantity = 1;
        BigDecimal price = spy(BigDecimal.valueOf(3_000));
        OrderProductWrappingDto orderProductWrapping = spy(new OrderProductWrappingDto(wrappingPaperId, quantity, price));

        when(wrappingPaperRepository.findById(wrappingPaperId)).thenReturn(Optional.empty());

        //when
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> orderValidationService.validateWrappingPaper(orderProductWrapping));

        assertEquals("포장지를 찾을 수 없습니다.", exception.getMessage());
    }

    @DisplayName("쿠폰 할인액 검증")
    @Test
    void validateCoupon() {
        Long couponId = 1L;
        BigDecimal discount = BigDecimal.valueOf(3_000);
        BigDecimal discountedPrice = BigDecimal.valueOf(27_000);
        OrderProductAppliedCouponDto appliedCoupon = new OrderProductAppliedCouponDto(couponId, discount);
        ValidationCouponCalculation validationCouponCalculation = new ValidationCouponCalculation(discountedPrice);
        when(couponService.validateCouponCalculation(eq(couponId), any(CouponCalculationRequestDto.class))).thenReturn(validationCouponCalculation);

        //when
        orderValidationService.validateCoupon(appliedCoupon);

        verify(couponService).validateCouponCalculation(eq(couponId), any(CouponCalculationRequestDto.class));
    }

    @Test
    void validateDeliveryWishDate() {
        //TODO: 테스트코드 작성
    }
}