package com.nhnacademy.book.member.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    //회원 등급 id
    @ManyToOne
    @JoinColumn(name = "m_grade_id", nullable = false)
    private MemberGrade memberGrade;

    //회원 상태 id
    @ManyToOne
    @JoinColumn(name = "m_state_id", nullable = false)
    private MemberStatus memberStatus;

    //이름
    @Column(nullable = false, length = 100)
    private String name;

    //연락처
    @Column(nullable = false, length = 15, unique = true)
    private String phone;

    //이메일
    @Column(nullable = false, length = 100, unique = true)
    private String email;

    //생일
    @Column(nullable = false)
    private LocalDate birth;

    //비밀번호
//    @Convert(converter = PasswordConverter.class)
    @Column(nullable = false, length = 200)
    private String password;



}
