//package com.nhnacademy.book.order.repository;
//
//import com.nhnacademy.book.book.entity.SellingBook;
//import com.nhnacademy.book.order.dto.OrderProductReturnDto;
//import com.nhnacademy.book.order.dto.OrderReturnSearchRequestDto;
//import com.nhnacademy.book.order.entity.OrderProductReturn;
//import com.nhnacademy.book.order.entity.Orders;
//import com.nhnacademy.book.order.enums.OrderStatus;
//import com.nhnacademy.book.orderProduct.entity.OrderProduct;
//import com.nhnacademy.book.orderProduct.entity.OrderProductStatus;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//@DataJpaTest
//class OrderReturnQueryRepositoryTest {
//    @Autowired
//    private OrderReturnQueryRepository orderReturnQueryRepository;
//    @Autowired
//    private TestEntityManager entityManager;
//
//
//    @BeforeEach
//    void setUp() {
//        Orders order = new Orders("orderId", "orderNumber", "orderName", LocalDate.now(), 1000,
//                BigDecimal.valueOf(3000), LocalDateTime.now(), OrderStatus.RETURN_REQUESTED, BigDecimal.valueOf(10_000));
//        entityManager.persist(order);
//        SellingBook sellingBook = new SellingBook();
//        entityManager.persist(sellingBook);
//        OrderProduct orderProduct = new OrderProduct(1L, sellingBook, order, BigDecimal.valueOf(10_000), 1, BigDecimal.ZERO,
//                OrderProductStatus.RETURN_REQUESTED);
//        entityManager.persist(orderProduct);
//
//        OrderProductReturn orderProductReturn = new OrderProductReturn(1L, "returnReason",
//                "1234567890", 1, LocalDateTime.now(), null, orderProduct);
//        entityManager.persist(orderProductReturn);
//
//        entityManager.flush();
//        entityManager.clear();
//    }
//
//    @Test
//    void findOrderProductReturnPage() {
//        String trackingNumber = "1234567890";
//        String status = OrderProductStatus.RETURN_REQUESTED.name();
//
//        OrderReturnSearchRequestDto searchRequest = new OrderReturnSearchRequestDto(trackingNumber, status);
//        Pageable pageable = PageRequest.of(0,10);
//
//        Page<OrderProductReturnDto> result = orderReturnQueryRepository.findOrderProductReturnPage(searchRequest, pageable);
//
//        assertTrue(result.hasContent());
//        List<OrderProductReturnDto> content = result.getContent();
//        assertEquals(trackingNumber, content.getFirst().getTrackingNumber());
//
//    }
//}