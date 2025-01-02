package com.nhnacademy.book.order.entity;

import com.nhnacademy.book.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class MemberOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_order_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "mo_member_id", referencedColumnName = "member_id")
    private Member member;
    @OneToOne
    @JoinColumn(name = "mo_order_id", referencedColumnName = "order_id")
    private Orders order;

    public MemberOrder(Member member, Orders order) {
        this.member = member;
        this.order = order;
    }
}
