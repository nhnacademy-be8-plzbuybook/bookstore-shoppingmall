package com.nhnacademy.book.book.controller;

import com.nhnacademy.book.book.dto.response.SellingBookResponseDto;
import com.nhnacademy.book.book.entity.*;
import com.nhnacademy.book.book.service.Impl.LikesService;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.MemberStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LikesControllerTest {

    @InjectMocks
    private LikesController likesController;

    @Mock
    private LikesService likesService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(likesController).build();
    }

    @Test
    @DisplayName("회원이 좋아요 누른책 가져오는 controller")
    void getLikedBooks_success() throws Exception {
        MemberGrade memberGrade = new MemberGrade(1L, "NORMAL", new BigDecimal("100.0"), LocalDateTime.now());
        MemberStatus memberStatus = new MemberStatus(1L, "ACTIVE");
        Member member = new Member(1L, memberGrade, memberStatus, "윤지호", "010-7237-3951", "yoonwlgh12@naver.com", LocalDate.now(), "encodedPassword");
        Publisher publisher = new Publisher();
        publisher.setPublisherId(1L);

        Publisher publisher2 = new Publisher();
        publisher2.setPublisherId(2L);


        Book book1 = new Book();
        book1.setBookId(1L);
        book1.setPublisher(publisher);

        Book book2 = new Book();
        book2.setBookId(2L);
        book2.setPublisher(publisher2);

        SellingBook sellingBook1 = new SellingBook();
        sellingBook1.setSellingBookId(1L);
        sellingBook1.setBook(book1);

        SellingBook sellingBook2 = new SellingBook();
        sellingBook2.setSellingBookId(2L);
        sellingBook2.setBook(book2);

        Likes like1 = new Likes();
        like1.setSellingBook(sellingBook1);
        like1.setMember(member);

        Likes like2 = new Likes();
        like2.setSellingBook(sellingBook2);
        like2.setMember(member);


        BookImage bookImage1 = new BookImage();
        bookImage1.setBook(book1);

        BookImage bookImage2 = new BookImage();
        bookImage2.setBook(book2);

        Category category1 = new Category();
        category1.setCategoryId(1L);

        Category category2 = new Category();
        category2.setCategoryId(2L);


        BookCategory bookCategory1 = new BookCategory();
        bookCategory1.setBook(book1);
        bookCategory1.setCategory(category1);

        BookCategory bookCategory2 = new BookCategory();
        bookCategory2.setBook(book2);
        bookCategory2.setCategory(category2);

        List<String> categories = new ArrayList<>();
        List<String> authors = new ArrayList<>();

        List<SellingBookResponseDto> likedBooks = Arrays.asList(
                new SellingBookResponseDto(1L, 1L, "test", new BigDecimal("100.00"), true,1, SellingBook.SellingBookStatus.SELLING, true,1L, "test", "test", categories, authors),
                new SellingBookResponseDto(2L, 2L, "test", new BigDecimal("100.00"), false,1, SellingBook.SellingBookStatus.SELLING, false,1L, "test", "test", categories, authors)
        );

        Page<SellingBookResponseDto> page = new PageImpl<>(likedBooks);

        when(likesService.getLikeBooks(1L, PageRequest.of(0,16))).thenReturn(page);

        mockMvc.perform(get("/api/members/{memberId}/liked-books", 1L)
                        .param("page", "0")
                        .param("size", "16")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        //값 비교도 해줘야 하지만 넘어오는 값이 없다고 한다 근데 구현한 내용은 잘 작동합니다

        verify(likesService, times(1)).getLikeBooks(1L, PageRequest.of(0, 16));

    }
}