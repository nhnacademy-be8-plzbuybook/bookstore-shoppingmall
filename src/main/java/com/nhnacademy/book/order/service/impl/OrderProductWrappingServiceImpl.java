package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.entity.OrderProductWrapping;
import com.nhnacademy.book.order.repository.OrderProductWrappingRepository;
import com.nhnacademy.book.order.service.OrderCacheService;
import com.nhnacademy.book.order.service.OrderProductWrappingService;
import com.nhnacademy.book.orderProduct.dto.OrderProductWrappingDto;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.repository.OrderProductRepository;
import com.nhnacademy.book.wrappingPaper.entity.WrappingPaper;
import com.nhnacademy.book.wrappingPaper.repository.WrappingPaperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderProductWrappingServiceImpl implements OrderProductWrappingService {
    private final OrderProductWrappingRepository orderProductWrappingRepository;
    private final OrderCacheService orderCacheService;

    private final OrderProductRepository orderProductRepository;
    private final WrappingPaperRepository wrappingPaperRepository;

    @Transactional
    @Override
    public void saveOrderProductWrapping(long orderProductId, OrderProductWrappingDto orderProductWrapping) {
        if (orderProductWrapping != null) {
            OrderProduct orderProduct = orderProductRepository.findById(orderProductId).orElseThrow(() -> new NotFoundException("찾을 수 없는 주문상품입니다."));
            WrappingPaper wrappingPaper = wrappingPaperRepository.findById(orderProductWrapping.getWrappingPaperId()).orElseThrow(() -> new NotFoundException("찾을 수 없는 포장"));
            // 포장지 재고 차감
            reduceWrappingPaperStock(wrappingPaper);
            // 주문상품-포장 저장
            orderProductWrappingRepository.save(new OrderProductWrapping(orderProduct, wrappingPaper, orderProductWrapping.getQuantity(), wrappingPaper.getPrice()));
        }
    }

    private void reduceWrappingPaperStock(WrappingPaper wrappingPaper) {
        long stock = orderCacheService.getStockCache(wrappingPaper.getId());
        wrappingPaper.setStock(stock);
    }
}
