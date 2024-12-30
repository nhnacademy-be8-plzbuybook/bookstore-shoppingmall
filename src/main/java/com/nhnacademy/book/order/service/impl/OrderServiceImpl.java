package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.book.dto.response.BookDetailResponseDto;
import com.nhnacademy.book.book.dto.response.SellingBookResponseDto;
import com.nhnacademy.book.book.service.Impl.BookService;
import com.nhnacademy.book.book.service.Impl.SellingBookService;
import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.order.dto.OrderSaveRequestDto;
import com.nhnacademy.book.order.dto.OrderSaveResponseDto;
import com.nhnacademy.book.order.dto.OrderUpdateRequestDto;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.enums.OrderStatus;
import com.nhnacademy.book.order.repository.OrderRepository;
import com.nhnacademy.book.order.service.OrderService;
import com.nhnacademy.book.orderProduct.dto.OrderProductSaveRequestDto;
import com.nhnacademy.book.orderProduct.dto.OrderProductWrappingDto;
import com.nhnacademy.book.orderProduct.service.OrderProductService;
import com.nhnacademy.book.wrappingPaper.service.WrappingPaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final WrappingPaperService wrappingPaperService;
    private final SellingBookService sellingBookService;
//    private final OrderProductService orderProductService;
    private final BookService bookService;

    @Override
    public Orders getOrderById(String orderId) {
        Optional<Orders> optionalOrder = orderRepository.findById(orderId);
        Orders order = optionalOrder.orElseThrow(() -> new NotFoundException("can not found order"));
        // dto로 감싸서 리턴
        return null;
    }

    @Override
    public List<Orders> getOrders() {
        return List.of();
    }

    @Transactional
    @Override
    public OrderSaveResponseDto createOrder(OrderSaveRequestDto saveRequest) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderProductSaveRequestDto orderProduct : saveRequest.getOrderProducts()) {
            // TODO: 상품 재고 검증
            // calculateFeeIfValidated(orderProduct.getSellingBookId(), orderProduct.getQuantity());

            // 포장지 재고 검증
            BigDecimal wrappingPaperPrice = wrappingPaperService.calculateFeeIfValidated(orderProduct.getWrapping());
            totalPrice = totalPrice.add(wrappingPaperPrice);
            // TODO: 사용할 쿠폰 검증

            // TODO: 사용할 포인트 검증

            // TODO: 주문희망일자에 배송할 수 있는지 검증
        }
        String orderId = UUID.randomUUID().toString();
        String orderName = generateOrderName(saveRequest.getOrderProducts());

        // 주문 저장
        Orders order = Orders.builder()
                .status(OrderStatus.PAYMENT_PENDING)
                .id(orderId)
                .name(orderName)
                .orderPrice(totalPrice)
                // 임시값들
                .usedPoint(1000)
                .number("123456789") // orderNumber
                .deliveryWishDate(saveRequest.getDeliveryWishDate())
                .build();
        orderRepository.save(order);

        return new OrderSaveResponseDto(orderId, totalPrice, orderName);
    }


    @Override
    public String modifyOrder(String orderId, OrderUpdateRequestDto updateRequest) {
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("can not found order"));
        // 전체를 업데이트 하는게 아니라 주문 상태정도만 업데이트?
        order.update(updateRequest);
        return orderId;
    }

    @Transactional(readOnly = true)
    protected String generateOrderName(List<OrderProductSaveRequestDto> orderProductList) {
        Long sellingBookId = orderProductList.getFirst().getSellingBookId();
        // 이거 판매책쪽 도메인에서 메소드로 구현하는 게?? -> BookDetailResponseDto로 변경했습니다.......
        BookDetailResponseDto sellingBook = sellingBookService.getSellingBook(sellingBookId);
        Long bookId = sellingBook.getBookId();
        BookDetailResponseDto book = bookService.getBookDetail(bookId);
        String bookTitle = book.getBookTitle();

        if (orderProductList.size() > 1) {
            return String.format("%s 외 %d 건", bookTitle, orderProductList.size() - 1);
        }
        return bookTitle;
    }


//    @Override
//    public void removeOrder(String orderId) {
//        boolean isExist = orderRepository.existsById(orderId);
//
//        if (isExist) {
//            orderRepository.deleteById(orderId);
//        }
//
//    }
}
