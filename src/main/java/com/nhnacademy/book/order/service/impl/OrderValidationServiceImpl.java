package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.book.repository.SellingBookRepository;
import com.nhnacademy.book.deliveryFeePolicy.dto.DeliveryFeeCalculateRequestDto;
import com.nhnacademy.book.deliveryFeePolicy.exception.ConflictException;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.deliveryFeePolicy.service.DeliveryFeePolicyService;
import com.nhnacademy.book.feign.CouponClient;
import com.nhnacademy.book.feign.dto.CouponCalculationRequestDto;
import com.nhnacademy.book.feign.dto.CouponCalculationResponseDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductAppliedCouponDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.exception.PriceMismatchException;
import com.nhnacademy.book.order.repository.OrderDeliveryRepository;
import com.nhnacademy.book.order.repository.OrderRepository;
import com.nhnacademy.book.order.service.OrderCacheService;
import com.nhnacademy.book.order.service.OrderDeliveryService;
import com.nhnacademy.book.order.service.OrderValidationService;
import com.nhnacademy.book.orderProduct.dto.OrderProductWrappingDto;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
import com.nhnacademy.book.wrappingPaper.dto.WrappingPaperDto;
import com.nhnacademy.book.wrappingPaper.service.WrappingPaperService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class OrderValidationServiceImpl implements OrderValidationService {
    private final SellingBookRepository sellingBookRepository;
    private final WrappingPaperService wrappingPaperService;
    private final OrderCacheService orderCacheService;
    private final DeliveryFeePolicyService deliveryFeePolicyService;
    private final CouponClient couponClient;
    private final OrderRepository orderRepository;
    private final OrderDeliveryService orderDeliveryService;


    @Transactional(readOnly = true)
    @Override
    public void validateOrder(OrderRequestDto order) {
        // 주문상품 검증
        for (OrderProductRequestDto orderProduct : order.getOrderProducts()) {
            validateOrderProduct(orderProduct);
        }
        // 사용포인트 검증
        validatePoint(order.getUsedPoint());
        // 배송비 검증
        validateDeliveryFee(order);
        // 배송 희망날짜 검증
        validateDeliveryWishDate(order.getDeliveryWishDate());
        // 주문금액 검증
        //validateOrderPrice(order);
    }


    @Transactional(readOnly = true)
    @Override
    public void validateOrderProduct(OrderProductRequestDto orderProduct) {
        // 판매책 검증
        validateSellingBook(orderProduct);

        // 포장지 검증
        if (isWrapped(orderProduct)) {
            assert orderProduct.getWrapping() != null; // ?
            validateWrappingPaper(orderProduct.getWrapping());
        }

        // 쿠폰 검증
        if (isCouponApplied(orderProduct)) {
            assert orderProduct.getAppliedCoupons() != null; // ?
            for (OrderProductAppliedCouponDto appliedCoupon : orderProduct.getAppliedCoupons()) {
                validateCoupon(appliedCoupon);
            }
        }
    }

    /**
     * 배송비 계산
     *
     * @param order 주문요청 DTO
     */
    @Override
    public void validateDeliveryFee(OrderRequestDto order) {
        BigDecimal deliveryFee = deliveryFeePolicyService.getCalculatedDeliveryFee(1L, new DeliveryFeeCalculateRequestDto(order.getOrderPrice()));
        if (order.getDeliveryFee().compareTo(deliveryFee) != 0) {
            throw new PriceMismatchException("배송비가 변동되었습니다.");
        }
    }

    @Override
    public void validatePoint(int usedPoint) {
        // 포인트를 사용했을 때만 검증
        if (usedPoint > 0) {
            //TODO: 포인트 검증
        }
    }

    @Override
    public void validateOrderProductForReturning(OrderProduct orderProduct) {
        int statusCode = orderProduct.getStatus().getCode();
        // 발송완료 <= statusCode <= 구매확정
        if (!(statusCode >= 2 && statusCode <= 5)) {
            throw new ConflictException("반품이 불가능한 주문상품입니다. (사유: 반품가능 상태가 아님)");
        }
        Orders order = orderRepository.findById(orderProduct.getOrder().getId()).orElseThrow(() -> new NotFoundException("주문정보를 찾을 수 없습니다."));
        boolean isReturnable = orderDeliveryService.isInReturnablePeriod(order);
        if (!isReturnable) {
            throw new ConflictException("반품이 불가능한 주문입니다. (사유: 반품기간 지남)");
        }
    }

    @Override
    public void validateOrderProductForReturnCompletion(OrderProduct orderProduct) {
        if (orderProduct.getStatus() != OrderProductStatus.RETURN_REQUESTED) {
            throw new ConflictException("반품요청된 주문상품이 아닙니다.");
        }
    }

    @Override
    public void validateOrderProductForCanceling(OrderProduct orderProduct) {
        if (orderProduct.getStatus().getCode() > 1) {
            throw new ConflictException("주문상품의 상태가 " + orderProduct.getStatus().getStatus() + "일 때는 주문취소가 불가능합니다.");
        }
    }


    /**
     * 주문상품 검증
     *
     * @param orderProduct 주문상품
     */
    @Override
    public void validateSellingBook(OrderProductRequestDto orderProduct) {
        SellingBook sellingBook = sellingBookRepository.findById(orderProduct.getProductId()).orElseThrow(() -> new NotFoundException("찾을 수 없는 상품입니다."));

        if (sellingBook.getSellingBookPrice().compareTo(orderProduct.getPrice()) != 0) {
            throw new PriceMismatchException(sellingBook.getBookTitle() + "의 가격이 변동되었습니다.");
        }
        // 재고선점
        Long preemptedQuantity = orderCacheService.preemptStockCache(orderProduct.getProductId(), orderProduct.getQuantity());
        if (preemptedQuantity == null) {
            // 재고 업데이트
            Long stock = (long) (sellingBook.getSellingBookStock() - orderProduct.getQuantity());
            orderCacheService.addStockCache(sellingBook.getSellingBookId(), stock);
        }
    }


    /**
     * 주문 포장지 검증
     *
     * @param orderProductWrapping 주문상품 포장 요청
     */
    @Override
    public void validateWrappingPaper(OrderProductWrappingDto orderProductWrapping) {
        // 포장지 검증
        WrappingPaperDto wrappingPaper = wrappingPaperService.getWrappingPaper(orderProductWrapping.getWrappingPaperId());
        // 포장지 금액 검증
        if (wrappingPaper.getPrice().compareTo(orderProductWrapping.getPrice()) != 0) {
            throw new PriceMismatchException(wrappingPaper.getName() + "의 가격이 변동되었습니다.");
        }
        // 재고선점
        Long preemptedQuantity = orderCacheService.preemptStockCache(orderProductWrapping.getWrappingPaperId(), orderProductWrapping.getQuantity());
        if (preemptedQuantity == null) {
            // 재고 업데이트
            Long requiredQuantity = wrappingPaper.getStock() - orderProductWrapping.getQuantity();
            orderCacheService.addStockCache(wrappingPaper.getId(), requiredQuantity);
        }
    }


    @Override
    public void validateCoupon(OrderProductAppliedCouponDto appliedCoupon) {
        //TODO: 쿠폰 검증
        // 할인가 검증
        Long couponId = appliedCoupon.getCouponId();
        BigDecimal discount = appliedCoupon.getDiscount();

        couponClient.validateCouponCalculation(couponId, new CouponCalculationRequestDto(discount));

        // 쿠폰 아이디가 유효한지 검증
        // 할인가격이 유효한지 검증
//        return new ValidateCouponDto(appliedCoupon.getCouponId(), appliedCoupon.getDiscount());
    }

    @Override
    public void validateDeliveryWishDate(LocalDate deliveryWishDate) {
        //TODO: 배송희망날짜 가능 여부 구현
    }


    private void validateOrderPrice(OrderRequestDto order) {
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
        BigDecimal orderPrice = productPrice
                .add(wrappingPrice)
                .subtract(couponDiscount)
                .subtract(BigDecimal.valueOf(order.getUsedPoint()));

        if (order.getOrderPrice().compareTo(orderPrice) != 0) {
            throw new PriceMismatchException("주문금액이 변동되었습니다.");
        }
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
