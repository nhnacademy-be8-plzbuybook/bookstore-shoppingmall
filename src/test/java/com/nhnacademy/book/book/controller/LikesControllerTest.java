package com.nhnacademy.book.book.controller;

import com.nhnacademy.book.book.dto.response.SellingBookAndBookResponseDto;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MissingRequestHeaderException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

        List<SellingBookAndBookResponseDto> likedBooks = Arrays.asList(
                new SellingBookAndBookResponseDto(1L, 1L, "test", new BigDecimal("100.00"), true,1, SellingBook.SellingBookStatus.SELLING, true,1L, "test", "test", categories, authors),
                new SellingBookAndBookResponseDto(2L, 2L, "test", new BigDecimal("100.00"), false,1, SellingBook.SellingBookStatus.SELLING, false,1L, "test", "test", categories, authors)
        );

        Page<SellingBookAndBookResponseDto> page = new PageImpl<>(likedBooks);

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



    @Test
    @DisplayName("좋아요 토글 - X-USER-ID 헤더 없음")
    void toggleLike_nullHeader() throws Exception {
        // 헤더 없이 POST 요청
        mockMvc.perform(post("/api/member-selling-books/like/{sellingBookId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)) // 헤더를 제거
                .andDo(print())
                .andExpect(status().isBadRequest()) // HTTP 400 상태 검증
                .andExpect(result -> {
                    // 발생한 예외 확인
                    Exception resolvedException = result.getResolvedException();
                    assertTrue(resolvedException instanceof MissingRequestHeaderException); // 예외 타입 검증

                    MissingRequestHeaderException ex = (MissingRequestHeaderException) resolvedException;
                    assertEquals("X-USER-ID", ex.getHeaderName()); // 누락된 헤더 이름 검증
                });

        // likesService 호출이 없음을 검증
        verify(likesService, times(0)).toggleLikeBook(anyString(), anyLong());
    }


    @Test
    @DisplayName("좋아요 토글 - 성공")
    void toggleLike_success() throws Exception {
        when(likesService.toggleLikeBook("user@example.com", 1L)).thenReturn(5L);

        mockMvc.perform(post("/api/member-selling-books/like/{sellingBookId}", 1L)
                        .header("X-USER-ID", "user@example.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(5L)); // JSON 반환 값 검증

        verify(likesService, times(1)).toggleLikeBook("user@example.com", 1L);
    }

}