package com.nhnacademy.book.domain;

import com.nhnacademy.book.MemberGrade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private long memberId;

    //회원 등급 id
    @ManyToOne
    @JoinColumn(name = "member_grade_id", nullable = false)
    private MemberGrade memberGrade;

    //회원 상태 id
    @ManyToOne
    @JoinColumn(name = "member_status_id", nullable = false)
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
    @Column(nullable = false, length = 100)
    private String password;

    //권한은 아직 확정 아님



}
