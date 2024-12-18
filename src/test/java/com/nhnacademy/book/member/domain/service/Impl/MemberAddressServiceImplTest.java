package com.nhnacademy.book.member.domain.service.Impl;

import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberAddress;
import com.nhnacademy.book.member.domain.dto.MemberAddressRequestDto;
import com.nhnacademy.book.member.domain.repository.MemberAddressRepository;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberAddressServiceImplTest {

    @Mock
    private MemberAddressRepository memberAddressRepository;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private MemberAddressServiceImpl memberAddressService;


}
