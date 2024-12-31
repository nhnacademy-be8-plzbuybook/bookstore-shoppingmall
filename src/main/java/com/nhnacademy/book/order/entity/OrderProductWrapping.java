package com.nhnacademy.book.order.entity;

import com.nhnacademy.book.orderProduct.entity.OrderProduct;
import com.nhnacademy.book.wrappingPaper.entity.WrappingPaper;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class OrderProductWrapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_wrapping")
    private Long id;

    @ManyToOne // 현재는 1 종류의 상품에 1 종류의 포장지만 적용가능, 확장성을 위해 manyToOn으로 설정
    @JoinColumn(name = "opw_order_product_id", referencedColumnName = "order_product_id")
    private OrderProduct orderProduct;

    @ManyToOne
    @JoinColumn(name = "opw_wrapping_paper_id", referencedColumnName = "wrapping_paper_id")
    private WrappingPaper wrappingPaper;

    private int quantity;
}
