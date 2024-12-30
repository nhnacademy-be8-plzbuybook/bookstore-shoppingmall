package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.book.dto.response.BookDetailResponseDto;
import com.nhnacademy.book.book.service.Impl.SellingBookService;
import com.nhnacademy.book.deliveryFeePolicy.exception.StockNotEnoughException;
import com.nhnacademy.book.order.dto.orderRequests.MemberOrderRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductAppliedCouponDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductRequestDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderRequestDto;
import com.nhnacademy.book.order.service.OrderValidationService;
import com.nhnacademy.book.orderProduct.dto.OrderProductWrappingDto;
import com.nhnacademy.book.wrappingPaper.dto.WrappingPaperDto;
import com.nhnacademy.book.wrappingPaper.service.WrappingPaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderValidationServiceImpl implements OrderValidationService {
    private final SellingBookService sellingBookService;
    private final WrappingPaperService wrappingPaperService;

    public void validateOrder(OrderRequestDto order) {
        for (OrderProductRequestDto orderProduct: order.getOrderProducts()) {
            validateOrderProduct(orderProduct);
        }
        validatePoint(order.getUsedPoint());
    }

    @Override
    public void validateOrderProduct(OrderProductRequestDto orderProduct) {
        // 판매책 검증
        validateSellingBook(orderProduct);

        // 포장지 검증
        if (isWrapping(orderProduct)) {
            assert orderProduct.getWrapping() != null; // ?
            validateWrappingPaper(orderProduct.getWrapping());
        }

        // 쿠폰 검증
        if (isCouponApplied(orderProduct)) {
            assert orderProduct.getAppliedCoupons() != null; // ?
            for(OrderProductAppliedCouponDto appliedCoupon: orderProduct.getAppliedCoupons()) {
                validateCoupon(appliedCoupon);
            }
        }
    }

    private void validateMemberOrderRequest(MemberOrderRequestDto orderRequest) {
        for (OrderProductRequestDto orderProductRequest : orderRequest.getOrderProducts()) {
            validateOrderProduct(orderProductRequest);
        }
        // 포인트 검증
        validatePoint(orderRequest.getUsedPoint());
    }

    private void validatePoint(int usedPoint) {
        // 포인트를 사용했을 때만 검증
        if (usedPoint > 0) {
            //TODO: 포인트 검증
        }
    }

    /**
     * 주문상품 검증
     *
     * @param orderProduct 주문상품
     */
    private void validateSellingBook(OrderProductRequestDto orderProduct) {
        BookDetailResponseDto product = sellingBookService.getSellingBook(orderProduct.getProductId());

//        if (product.getStock < orderProduct.getQuantity()) {
//            throw new StockNotEnoughException(product.getBookTitle() + "의 재고가 부족합니다.");
//        }
        if (product.getSellingPrice().compareTo(orderProduct.getPrice()) != 0) {
            throw new IllegalArgumentException(product.getBookTitle() + "의 가격이 변동되었습니다.");
        }

    }


    /**
     * 주문상품 포장 여부 확인
     *
     * @param orderProduct 주문상품
     * @return 포장 여부
     */
    private boolean isWrapping(OrderProductRequestDto orderProduct) {
        return orderProduct.getWrapping() != null;
    }


    /**
     * 주문 포장지 검증
     *
     * @param orderProductWrapping 주문상품 포장 요청
     */
    private void validateWrappingPaper(OrderProductWrappingDto orderProductWrapping) {
        WrappingPaperDto wrappingPaper = wrappingPaperService.getWrappingPaper(orderProductWrapping.getWrappingPaperId());

        if (wrappingPaper.getStock() < orderProductWrapping.getQuantity()) {
            throw new StockNotEnoughException(wrappingPaper.getName() + "의 재고가 부족합니다.");
        }
        if (wrappingPaper.getPrice().compareTo(orderProductWrapping.getPrice()) != 0) {
            throw new IllegalArgumentException(wrappingPaper.getName() + "의 가격이 변동되었습니다.");
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
     * 쿠폰 검증
     *
     * @param couponId
     */
    private void validateCoupon(OrderProductAppliedCouponDto appliedCoupon) {
        //TODO: 쿠폰 검증
        // 할인가 검증
    }
}
