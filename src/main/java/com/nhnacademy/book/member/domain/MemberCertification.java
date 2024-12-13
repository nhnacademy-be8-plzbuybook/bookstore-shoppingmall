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
public class MemberCertification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long memberAuthId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mc_member_id", nullable = false)
    private Member member;

    @Column
    private LocalDateTime lastLogin;

    @Column(nullable = false)
    private String certificationMethod;
}
