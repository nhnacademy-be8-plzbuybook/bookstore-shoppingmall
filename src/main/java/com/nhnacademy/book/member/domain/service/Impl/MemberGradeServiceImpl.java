package com.nhnacademy.book.member.domain.service.Impl;

import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.dto.MemberGradeCreateRequestDto;
import com.nhnacademy.book.member.domain.exception.DuplicateMemberGradeException;
import com.nhnacademy.book.member.domain.exception.DuplicateMemberStateException;
import com.nhnacademy.book.member.domain.repository.MemberGradeRepository;
import com.nhnacademy.book.member.domain.service.MemberGradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberGradeServiceImpl implements MemberGradeService {

    private final MemberGradeRepository memberGradeRepository;

    //회원 등급 저장
    @Override
    public MemberGrade createMemberGrade(MemberGradeCreateRequestDto memberGradeCreateRequestDto) {
        memberGradeRepository.findByMemberGradeName(memberGradeCreateRequestDto.getMemberGradeName())
                .ifPresent(existing -> {
                    throw new DuplicateMemberGradeException("이미 존재하는 등급!");
                });

        MemberGrade memberGrade = new MemberGrade();
        memberGrade.setMemberGradeName(memberGradeCreateRequestDto.getMemberGradeName());
        memberGrade.setConditionPrice(memberGradeCreateRequestDto.getConditionPrice());
        memberGrade.setGradeChange(memberGradeCreateRequestDto.getGradeChange());

        return memberGradeRepository.save(memberGrade);
    }
}
