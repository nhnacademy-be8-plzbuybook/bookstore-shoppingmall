package com.nhnacademy.book.member.domain.service.Impl;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.StatusName;
import com.nhnacademy.book.member.domain.dto.MemberCreateRequestDto;
import com.nhnacademy.book.member.domain.dto.MemberGradeCreateRequestDto;
import com.nhnacademy.book.member.domain.dto.MemberStatusCreateRequestDto;
import com.nhnacademy.book.member.domain.repository.MemberGradeRepository;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import com.nhnacademy.book.member.domain.repository.MemberStatusRepository;
import com.nhnacademy.book.member.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberGradeRepository memberGradeRepository;
    private final MemberStatusRepository memberStatusRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member save(MemberCreateRequestDto memberCreateRequestDto) {
        //이메일 중복 검사
        if (memberRepository.existsByEmail(memberCreateRequestDto.getEmail())) {
            throw new RuntimeException("이메일이 이미 존재함!");
        }

        MemberGrade memberGrade = memberGradeRepository.findById(memberCreateRequestDto.getMemberGradeId()).orElseThrow(() -> new RuntimeException("멤버 등급이 없다!"));
        MemberStatus memberStatus = memberStatusRepository.findById(memberCreateRequestDto.getMemberStateId()).orElseThrow(() -> new RuntimeException("멤버 상태가 없다!"));
        String encodedPassword = passwordEncoder.encode(memberCreateRequestDto.getPassword());

        Member member = Member.builder()
                .memberGrade(memberGrade)
                .memberStatus(memberStatus)
                .name(memberCreateRequestDto.getName())
                .phone(memberCreateRequestDto.getPhone())
                .email(memberCreateRequestDto.getEmail())
                .birth(memberCreateRequestDto.getBirth())
                .password(encodedPassword)
                .build();

        return memberRepository.save(member);
    }


    @Override
    public MemberGrade save(MemberGradeCreateRequestDto memberGradeCreateRequestDto) {
        MemberGrade memberGrade = new MemberGrade();
        memberGrade.setMemberGradeName(memberGradeCreateRequestDto.getMemberGradeName());
        memberGrade.setConditionPrice(memberGradeCreateRequestDto.getConditionPrice());
        memberGrade.setGradeChange(memberGradeCreateRequestDto.getGradeChange());

        return memberGradeRepository.save(memberGrade);
    }

    @Override
    public MemberStatus save(MemberStatusCreateRequestDto memberStatusCreateRequestDto){
        MemberStatus memberStatus = new MemberStatus();
        memberStatus.setMemberStateName(StatusName.valueOf(memberStatusCreateRequestDto.getMemberStateName()));

        return memberStatusRepository.save(memberStatus);
    }

    @Override
    public MemberGrade findByMemberGradeId(Long id) {
        MemberGrade memberGrade = memberGradeRepository.findById(id).orElseThrow(() -> new RuntimeException("멤버 등급이 없다!"));
        return memberGrade;
    }

    @Override
    public MemberStatus findByMemberStatusId(Long id){
        MemberStatus memberStatus = memberStatusRepository.findById(id).orElseThrow(() -> new RuntimeException("멤버 상태가 없다!"));
        return memberStatus;
    }
}
