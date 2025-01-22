package com.nhnacademy.book.point.domain;

import com.nhnacademy.book.member.domain.Member;
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
public class MemberPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long memberPointId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mp_member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mp_condition_id")
    private PointCondition pointCondition;

    @Column(nullable = false)
    private Integer point; // 적립된 포인트 추가

    @Column
    private LocalDateTime addDate;

    @Column
    private LocalDateTime usingDate;

    @Column
    private LocalDateTime endDate;

    @Column(nullable = false)
    private String type;

}
