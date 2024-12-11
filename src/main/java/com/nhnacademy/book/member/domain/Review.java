package com.nhnacademy.book.member.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "r_member_id", nullable = false)
    private Member member;

    @OneToOne
    @JoinColumn(name = "r_order_product_id", nullable = false)
    private OrderProduct orderProduct;

    //평가 점수
    private Integer score;

    //리뷰 내용
    private String content;

    //리뷰 작성 날짜
    @Column(name = "write_date")
    private LocalDateTime writeDate;

    //리뷰 수정 날짜
    @Column(name = "modify_date")
    private LocalDateTime modifiedDate;

}
