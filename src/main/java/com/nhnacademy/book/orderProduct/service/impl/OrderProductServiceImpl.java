package com.nhnacademy.book.orderProduct.service.impl;

import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.book.repository.SellingBookRepository;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.deliveryFeePolicy.exception.StockNotEnoughException;
import com.nhnacademy.book.orderProduct.dto.OrderProductSaveRequestDto;
import com.nhnacademy.book.orderProduct.dto.OrderProductWrappingDto;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.repository.OrderProductRepository;
import com.nhnacademy.book.orderProduct.service.OrderProductService;
import com.nhnacademy.book.wrappingPaper.service.WrappingPaperService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class OrderProductServiceImpl implements OrderProductService {
    private final OrderProductRepository orderProductRepository;
    private final SellingBookRepository sellingBookRepository;
    private final WrappingPaperService wrappingPaperService;

    @Transactional
    @Override
    public OrderProduct saveOrderProduct(OrderProductSaveRequestDto orderProductSaveRequest) {
        long sellingBookId = orderProductSaveRequest.getSellingBookId();
        SellingBook sellingBook = sellingBookRepository.findById(sellingBookId).orElseThrow(() -> new NotFoundException("존재하지 않는 판매 책입니다."));

        // 판매 책 검증 -> 판매책에서 담당하도록
        if (!sellingBook.getSellingBookStatus().equals(SellingBook.SellingBookStatus.SELLING)) {
            throw new IllegalArgumentException("판매 중인 책이 아닙니다.");
        }
        if (sellingBook.getSellingBookStock() < orderProductSaveRequest.getQuantity()) {
            throw new StockNotEnoughException("판매책의 재고가 부족합니다.");
        }
        sellingBook.setSellingBookStock(sellingBook.getSellingBookStock() - orderProductSaveRequest.getQuantity());

        BigDecimal totalPrice = sellingBook.getSellingBookPrice().multiply(new BigDecimal(orderProductSaveRequest.getQuantity()));

        // 포장지 검증
        if (orderProductSaveRequest.getWrapping() != null) {
            OrderProductWrappingDto orderProductWrapping = orderProductSaveRequest.getWrapping();
            long wrappingPaperId = orderProductWrapping.getWrappingPaperId();
            int wrappingPaperQuantity = orderProductWrapping.getQuantity();

            BigDecimal consumedWrappingPaperPrice = wrappingPaperService.calculateFeeIfValidated(wrappingPaperId, wrappingPaperQuantity);
            totalPrice = totalPrice.add(consumedWrappingPaperPrice);
        }

        OrderProduct orderProduct = OrderProduct.builder()
                .quantity(orderProductSaveRequest.getQuantity())
                .sellingBook(sellingBook)
                .totalPrice(totalPrice)
                .couponDiscountPrice(orderProductSaveRequest.getCouponDiscount())
                .build();
        return orderProductRepository.save(orderProduct);
    }
}
