package com.nhnacademy.book.order.service.impl;

import com.nhnacademy.book.deliveryFeePolicy.exception.NotFoundException;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.order.entity.OrderProductReturn;
import com.nhnacademy.book.order.entity.Orders;
import com.nhnacademy.book.order.repository.OrderRepository;
import com.nhnacademy.book.order.service.ReturnPointService;
import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.orderProduct.repository.OrderProductRepository;
import com.nhnacademy.book.point.service.MemberPointService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReturnPointServiceImpl implements ReturnPointService {
    //환불 비율 = 샤용 포인트 / 주문 목록의 전체 합(책 1개 가격 * 수량)
    //환불 포인트 공식 : (환불 비율 * 환불 수량 * 환불 책 1개 가격) + (환불 수량 * 환불 책 1개 가격)

    private final OrderProductRepository orderProductRepository;
    private final MemberPointService memberPointService;
    private final MemberRepository memberRepository;


    @Override
    public void returnPoint(Long orderProductId) {
        //사용 포인트
        Orders orders = orderProductRepository.findOrderByOrderProductId(orderProductId);
        Integer usePoint = orders.getUsedPoint();

        //회원에 해당하는 주문서의 전체 내용 (orderProduct) member 145 products 0
        Long memberId = orderProductRepository.findMemberIdByOrderProductId(orderProductId);
        //memberOrderId
        Long memberOrderId = orderProductRepository.findMemberOrderIdByOrderProductId(orderProductId);
        List<OrderProduct> orderProducts = orderProductRepository.findByMemberOrderId(memberId, memberOrderId);

        //환불 비율 분모 (0)
        BigDecimal totalPrice = calculateTotalPrice(orderProducts);

        //환불 비율 = 샤용 포인트 / 주문 목록의 전체 합(책 1개 가격 * 수량)
        double refundRatio = usePoint/totalPrice.doubleValue();

        //orderProductId로 환불 조회
        OrderProductReturn orderProductReturn = orderProductRepository.findByOrderProductOrderProductId(orderProductId);
        //환불 수량 1
        Integer refundQuantity = orderProductReturn.getQuantity();
        //환불책 1개 가격 20000
        Optional<OrderProduct> orderProduct = orderProductRepository.findById(orderProductId);
        BigDecimal refundOneBook = orderProduct.get().getPrice();


        //환불 포인트 공식 : (환불 비율 * 환불 수량 * 환불 책 1개 가격) + (환불 수량 * 환불 책 1개 가격)
        // Double 값을 BigDecimal로 변환하여 계산
        BigDecimal refundRatioBD = BigDecimal.valueOf(refundRatio);
        BigDecimal refundQuantityBD = BigDecimal.valueOf(refundQuantity);

        // 첫 번째 계산: refundRatio * refundQuantity * refundOneBook
        BigDecimal firstPart = refundRatioBD.multiply(refundQuantityBD).multiply(refundOneBook);

        // 두 번째 계산: refundQuantity * refundOneBook
        BigDecimal secondPart = refundQuantityBD.multiply(refundOneBook);

        // 두 계산을 더함
        BigDecimal refundPoint = firstPart.add(secondPart);

        // 결과를 소수점 두 자리에서 반올림
        refundPoint = refundPoint.setScale(0, RoundingMode.FLOOR);


        memberPointService.restorePoint(
                memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException("회원 정보를 찾을 수 없습니다.")),
                refundPoint.intValue()
        );
    }

    //환불비율 분모 계산
    public BigDecimal calculateTotalPrice(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .map(orderProduct -> orderProduct.getPrice().multiply(new BigDecimal(orderProduct.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
