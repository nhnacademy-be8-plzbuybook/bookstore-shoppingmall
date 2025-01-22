package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.book.repository.SellingBookRepository;
import com.nhnacademy.book.coupon.dto.ValidationCouponCalculationRequestDto;
import com.nhnacademy.book.coupon.service.CouponService;
import com.nhnacademy.book.deliveryFeePolicy.dto.DeliveryFeeCalculateRequestDto;
import com.nhnacademy.book.deliveryFeePolicy.exception.ConflictException;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.deliveryFeePolicy.exception.StockNotEnoughException;
import com.nhnacademy.book.deliveryFeePolicy.service.DeliveryFeePolicyService;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductAppliedCouponDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.exception.PriceMismatchException;
import com.nhnacademy.book.order.service.OrderCacheService;
import com.nhnacademy.book.order.service.OrderDeliveryService;
import com.nhnacademy.book.order.service.OrderValidationService;
import com.nhnacademy.book.orderProduct.dto.OrderProductWrappingDto;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
import com.nhnacademy.book.wrappingPaper.entity.WrappingPaper;
import com.nhnacademy.book.wrappingPaper.repository.WrappingPaperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class OrderValidationServiceImpl implements OrderValidationService {
    private final SellingBookRepository sellingBookRepository;
    private final WrappingPaperRepository wrappingPaperRepository;
    private final OrderCacheService orderCacheService;
    private final DeliveryFeePolicyService deliveryFeePolicyService;
    private final OrderDeliveryService orderDeliveryService;
    private final CouponService couponService;


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
    }


    @Transactional(readOnly = true)
    @Override
    public void validateOrderProduct(OrderProductRequestDto orderProduct) {
        // 판매책 검증
        validateSellingBook(orderProduct);

        // 포장지 검증
        if (isWrapped(orderProduct)) {
            validateWrappingPaper(orderProduct.getWrapping());
        }

        // 쿠폰 검증
        if (isCouponApplied(orderProduct)) {
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
        Orders order = orderProduct.getOrder();
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
    @Transactional(readOnly = true)
    @Override
    public void validateSellingBook(OrderProductRequestDto orderProduct) {
        SellingBook sellingBook = sellingBookRepository.findById(orderProduct.getProductId()).orElseThrow(() -> new NotFoundException("찾을 수 없는 상품입니다."));

        int currentStock = orderCacheService.getProductStockCache(sellingBook.getSellingBookId());
        if (currentStock < orderProduct.getQuantity()) {
            throw new StockNotEnoughException("재고가 부족합니다.");
        }

        if (sellingBook.getSellingBookPrice().compareTo(orderProduct.getPrice()) != 0) {
            throw new PriceMismatchException(sellingBook.getBookTitle() + "의 가격이 변동되었습니다.");
        }
    }


    /**
     * 주문 포장지 검증
     *
     * @param orderProductWrapping 주문상품 포장 요청
     */
    @Transactional(readOnly = true)
    @Override
    public void validateWrappingPaper(OrderProductWrappingDto orderProductWrapping) {
        // 포장지 검증
        Long wrappingPaperId = orderProductWrapping.getWrappingPaperId();
        WrappingPaper wrappingPaper = wrappingPaperRepository.findById(wrappingPaperId).orElseThrow(() -> new NotFoundException("포장지를 찾을 수 없습니다."));
        Long currentStock = orderCacheService.getWrappingPaperStockCache(wrappingPaperId);
        //재고검증
        if (currentStock < orderProductWrapping.getQuantity()) {
            throw new StockNotEnoughException(wrappingPaper.getName() + "의 재고가 부족합니다.");
        }
        // 포장지 금액 검증
        if (wrappingPaper.getPrice().compareTo(orderProductWrapping.getPrice()) != 0) {
            throw new PriceMismatchException(wrappingPaper.getName() + "의 가격이 변동되었습니다.");
        }
    }


    @Override
    public void validateCoupon(OrderProductAppliedCouponDto appliedCoupon) {
        Long couponId = appliedCoupon.getCouponId();
        BigDecimal discount = appliedCoupon.getDiscount();

        couponService.validateCouponCalculation(couponId, new ValidationCouponCalculationRequestDto(discount));
    }

    @Override
    public void validateDeliveryWishDate(LocalDate deliveryWishDate) {
        //TODO: 배송희망날짜 가능 여부 구현
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
