package com.nhnacademy.book.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_grade_id", nullable = false)
    private Long memberGradeId;

    //회원 등급 이름
    @Column(name = "name", nullable = false, length = 10, columnDefinition = "DEFAULT 'NORMAL'")
    private String memberGradeName;

    //등업 조건 주문 금액
    @Column(name = "condition_price")
    private BigDecimal conditionPrice;

    //회원 등급 변경 로그
    @Column(name = "grade_change")
    private LocalDateTime gradeChange;
}
