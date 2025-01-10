package com.nhnacademy.book.orderProduct.service.impl;

import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.book.repository.SellingBookRepository;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductAppliedCouponDto;
import com.nhnacademy.book.order.dto.orderRequests.OrderProductRequestDto;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.service.OrderCacheService;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
import com.nhnacademy.book.orderProduct.repository.OrderProductRepository;
import com.nhnacademy.book.orderProduct.service.OrderProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderProductServiceImpl implements OrderProductService {
    private final OrderProductRepository orderProductRepository;
    private final SellingBookRepository sellingBookRepository;
    private final OrderCacheService orderCacheService;

    @Transactional
    @Override
    public OrderProduct saveOrderProduct(Orders order, OrderProductRequestDto orderProductRequest) {
        SellingBook sellingBook = sellingBookRepository.findById(orderProductRequest.getProductId()).orElseThrow(() -> new NotFoundException("찾을 수 없는 상품입니다."));
        // 판매책 재고 차감
        sellingBook.setSellingBookStock(orderCacheService.getStockCache(sellingBook.getSellingBookId()));

        BigDecimal couponDiscount = BigDecimal.ZERO;
        if (orderProductRequest.getAppliedCoupons() != null && !orderProductRequest.getAppliedCoupons().isEmpty()) {
            couponDiscount = orderProductRequest.getAppliedCoupons().stream().map(OrderProductAppliedCouponDto::getDiscount).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        OrderProduct orderProduct = OrderProduct.builder()
                .sellingBook(sellingBook)
                .quantity(orderProductRequest.getQuantity())
                .price(orderProductRequest.getPrice())
                .status(OrderProductStatus.PAYMENT_COMPLETED)
                .couponDiscount(couponDiscount)
                .order(order)
                .build();
        OrderProduct savedOrderProduct = orderProductRepository.save(orderProduct);
        orderProductRepository.flush();
        return savedOrderProduct;
    }

    @Transactional
    @Override
    public Optional<OrderProduct> findOrderProductBySellingBookId(Long sellingBookId) {
        return orderProductRepository.findBySellingBook_SellingBookId(sellingBookId);
    }
}
