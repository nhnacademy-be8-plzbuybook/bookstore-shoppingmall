package com.nhnacademy.book.member.domain.service;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.dto.MemberCreateRequestDto;

import java.time.LocalDate;

public interface MemberService {
    Member save(MemberCreateRequestDto memberCreateRequestDto);

}
