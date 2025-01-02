package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.book.dto.response.BookDetailResponseDto;
import com.nhnacademy.book.book.service.Impl.SellingBookService;
import com.nhnacademy.book.deliveryFeePolicy.dto.DeliveryFeeCalculateRequestDto;
import com.nhnacademy.book.deliveryFeePolicy.service.DeliveryFeePolicyService;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductAppliedCouponDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.order.dto.validatedDtos.ValidateCouponDto;
import com.nhnacademy.book.order.dto.validatedDtos.ValidatedOrderDto;
import com.nhnacademy.book.order.dto.validatedDtos.ValidatedOrderProductDto;
import com.nhnacademy.book.order.dto.validatedDtos.ValidatedWrappingPaperDto;
import com.nhnacademy.book.order.service.OrderCacheService;
import com.nhnacademy.book.order.service.OrderValidationService;
import com.nhnacademy.book.orderProduct.dto.OrderProductWrappingDto;
import com.nhnacademy.book.wrappingPaper.dto.WrappingPaperDto;
import com.nhnacademy.book.wrappingPaper.service.WrappingPaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderValidationServiceImpl implements OrderValidationService {
    private final SellingBookService sellingBookService;
    private final WrappingPaperService wrappingPaperService;
    private final OrderCacheService orderCacheService;
    private final DeliveryFeePolicyService deliveryFeePolicyService;

    @Transactional(readOnly = true)
    @Override
    public ValidatedOrderDto validateOrder(OrderRequestDto order) {
        // 주문상품 검증
        List<ValidatedOrderProductDto> validatedOrderProductDtos = new ArrayList<>();
        for (OrderProductRequestDto orderProduct : order.getOrderProducts()) {
            validatedOrderProductDtos.add(validateOrderProduct(orderProduct));
        }
        // 사용포인트 검증
        int validatedUsedPoint = validatePoint(order.getUsedPoint());
        // 배송비 검증
        BigDecimal validatedDeliveryFee = validateDeliveryFee(order);
        LocalDate deliveryWishDate = validateDeliveryWishDate(order.getDeliveryWishDate());
        BigDecimal paymentPrice = calculatePaymentPrice(order);

        return new ValidatedOrderDto(validatedOrderProductDtos, deliveryWishDate, validatedUsedPoint, validatedDeliveryFee, paymentPrice);
    }


    @Transactional(readOnly = true)
    @Override
    public ValidatedOrderProductDto validateOrderProduct(OrderProductRequestDto orderProduct) {
        // 판매책 검증
        ValidatedOrderProductDto validatedOrderProductDto = validateSellingBook(orderProduct);

        // 포장지 검증
        if (isWrapped(orderProduct)) {
            assert orderProduct.getWrapping() != null; // ?
            validatedOrderProductDto.applyWrapping(validateWrappingPaper(orderProduct.getWrapping()));
        }

        // 쿠폰 검증
        if (isCouponApplied(orderProduct)) {
            assert orderProduct.getAppliedCoupons() != null; // ?
            for (OrderProductAppliedCouponDto appliedCoupon : orderProduct.getAppliedCoupons()) {
                validatedOrderProductDto.applyCoupon(validateCoupon(appliedCoupon));
            }
        }
        return validatedOrderProductDto;
    }

    /**
     * 배송비 계산
     *
     * @param order 주문요청 DTO
     * @return 계산된 배송비
     */
    @Override
    public BigDecimal validateDeliveryFee(OrderRequestDto order) {
        BigDecimal paymentPrice = calculatePaymentPrice(order); // TODO: 중복되는 로직 있어서 리팩토링해야됨
        return deliveryFeePolicyService.getCalculatedDeliveryFee(1L, new DeliveryFeeCalculateRequestDto(paymentPrice));
    }

    @Override
    public int validatePoint(int usedPoint) {
        // 포인트를 사용했을 때만 검증
        if (usedPoint > 0) {
            //TODO: 포인트 검증
        }
        return usedPoint;
    }


    /**
     * 주문상품 검증
     *
     * @param orderProduct 주문상품
     */
    @Override
    public ValidatedOrderProductDto validateSellingBook(OrderProductRequestDto orderProduct) {
        BookDetailResponseDto product = sellingBookService.getSellingBook(orderProduct.getProductId());
        //TODO: 임시
//        if (product.getSellingPrice().compareTo(orderProduct.getPrice()) != 0) {
//            throw new IllegalArgumentException(product.getBookTitle() + "의 가격이 변동되었습니다.");
//        }
        if (false) {
            throw new IllegalArgumentException(product.getBookTitle() + "의 가격이 변동되었습니다.");
        }
        // 재고선점
        orderCacheService.preemptStockCache(orderProduct.getProductId(), orderProduct.getQuantity());

        return new ValidatedOrderProductDto(orderProduct.getProductId(), orderProduct.getQuantity(), orderProduct.getPrice());
    }


    /**
     * 주문 포장지 검증
     *
     * @param orderProductWrapping 주문상품 포장 요청
     */
    @Override
    public ValidatedWrappingPaperDto validateWrappingPaper(OrderProductWrappingDto orderProductWrapping) {
        WrappingPaperDto wrappingPaper = wrappingPaperService.getWrappingPaper(orderProductWrapping.getWrappingPaperId());

        if (wrappingPaper.getPrice().compareTo(orderProductWrapping.getPrice()) != 0) {
            throw new IllegalArgumentException(wrappingPaper.getName() + "의 가격이 변동되었습니다.");
        }
        // 재고선점
        orderCacheService.preemptStockCache(orderProductWrapping.getWrappingPaperId(), orderProductWrapping.getQuantity());

        return new ValidatedWrappingPaperDto(orderProductWrapping.getWrappingPaperId(), orderProductWrapping.getQuantity(), orderProductWrapping.getPrice());
    }


    @Override
    public ValidateCouponDto validateCoupon(OrderProductAppliedCouponDto appliedCoupon) {
        //TODO: 쿠폰 검증
        // 할인가 검증
        return new ValidateCouponDto(appliedCoupon.getCouponId(), appliedCoupon.getDiscount());
    }

    @Override
    public LocalDate validateDeliveryWishDate(LocalDate deliveryWishDate) {
        //TODO: 배송희망날짜 가능 여부 구현
        return deliveryWishDate;
    }


    private BigDecimal calculatePaymentPrice(OrderRequestDto order) {
        BigDecimal productPrice = BigDecimal.ZERO;
        BigDecimal wrappingPrice = BigDecimal.ZERO;
        BigDecimal couponDiscount = BigDecimal.ZERO;

        for (OrderProductRequestDto orderProduct : order.getOrderProducts()) {
            productPrice = productPrice.add(orderProduct.getPrice().multiply(BigDecimal.valueOf(orderProduct.getQuantity())));
            if (isWrapped(orderProduct)) {
                wrappingPrice = wrappingPrice.add(orderProduct.getWrapping().getPrice().multiply(BigDecimal.valueOf(orderProduct.getWrapping().getQuantity())));
            }
            if (isCouponApplied(orderProduct)) {
                for (OrderProductAppliedCouponDto coupon : orderProduct.getAppliedCoupons()) {
                    couponDiscount = couponDiscount.subtract(coupon.getDiscount());
                }
            }
        }
        return productPrice
                .add(wrappingPrice)
                .subtract(couponDiscount)
                .subtract(BigDecimal.valueOf(order.getUsedPoint()));
    }

    /**
     * 쿠폰 적용 여부
     *
     * @param orderProduct 주문상품
     * @return 쿠폰 적용 여부
     */
    private boolean isCouponApplied(OrderProductRequestDto orderProduct) {
        return orderProduct.getAppliedCoupons() != null && !orderProduct.getAppliedCoupons().isEmpty();
    }


    /**
     * 주문상품 포장 여부 확인
     *
     * @param orderProduct 주문상품
     * @return 포장 여부
     */
    private boolean isWrapped(OrderProductRequestDto orderProduct) {
        return orderProduct.getWrapping() != null;
    }
}
