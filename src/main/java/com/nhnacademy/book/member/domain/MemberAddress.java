package com.nhnacademy.book.member.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member_address")
public class MemberAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long memberAddressId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_member_id", nullable = false)
    private Member member;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT false")
    private Boolean defaultAddress;

    @Column(length = 100, nullable = false)
    private String locationAddress;

    @Column(length = 100, nullable = false)
    private String detailAddress;

    @Column(name = "zipcode", length = 20, nullable = false)
    private String zipCode;

    @Column(name = "nickname", length = 50)
    private String nickName;

    @Column(length = 100)
    private String recipient;

    @Column(length = 15)
    private String recipientPhone;





}
