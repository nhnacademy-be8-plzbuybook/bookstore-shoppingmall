package com.nhnacademy.book.book.service.Impl;

import com.nhnacademy.book.book.dto.response.SellingBookAndBookResponseDto;
import com.nhnacademy.book.book.entity.*;
import com.nhnacademy.book.book.repository.*;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.MemberStatus;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikesServiceTest {

    @InjectMocks
    private LikesService likesService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private LikesRepository likesRepository;

    @Mock
    private SellingBookRepository sellingBookRepository;

    @Mock
    private BookImageRepository bookImageRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookAuthorRepository bookAuthorRepository;

    @Mock
    private Pageable pageable;

    @Test
    @DisplayName("좋아요 누른 책 조회 성공")
    void getLikeBooks_success() {
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

        // 좋아요 리스트 설정
        List<SellingBook> likedBooksList = Arrays.asList(sellingBook1, sellingBook2);
        Page<SellingBook> page = new PageImpl<>(likedBooksList);

        // Mockito가 호출될 때 반환될 값 설정
        when(likesRepository.findLikedBooksByMemberId(1L, pageable)).thenReturn(page);

        // 메서드 호출
        Page<SellingBookAndBookResponseDto> result = likesService.getLikeBooks(1L, pageable);

        // 검증
        assertNotNull(result);
        assertEquals(2, result.getContent().size()); // 좋아요를 누른 책 2권

        // Mockito verify
        verify(likesRepository, times(1)).findLikedBooksByMemberId(1L, pageable);

    }

    @Test
    @DisplayName("좋아요 토글 - 좋아요 추가")
    void toggleLikeBook_addLike() {
        Member member = mock(Member.class);
        SellingBook sellingBook = mock(SellingBook.class);

        when(memberRepository.findByEmail("user@example.com")).thenReturn(Optional.of(member));
        when(sellingBookRepository.findById(1L)).thenReturn(Optional.of(sellingBook));
        when(likesRepository.findByMemberAndSellingBook(member, sellingBook)).thenReturn(Optional.empty());
        when(likesRepository.countBySellingBook(sellingBook)).thenReturn(1L);

        Long likeCount = likesService.toggleLikeBook("user@example.com", 1L);

        assertEquals(1L, likeCount);
        verify(likesRepository, times(1)).save(any(Likes.class));
    }

    @Test
    @DisplayName("좋아요 토글 - 좋아요 취소")
    void toggleLikeBook_removeLike() {
        Member member = mock(Member.class);
        SellingBook sellingBook = mock(SellingBook.class);
        Likes likes = mock(Likes.class);

        when(memberRepository.findByEmail("user@example.com")).thenReturn(Optional.of(member));
        when(sellingBookRepository.findById(1L)).thenReturn(Optional.of(sellingBook));
        when(likesRepository.findByMemberAndSellingBook(member, sellingBook)).thenReturn(Optional.of(likes));
        when(likesRepository.countBySellingBook(sellingBook)).thenReturn(0L);

        Long likeCount = likesService.toggleLikeBook("user@example.com", 1L);

        assertEquals(0L, likeCount);
        verify(likesRepository, times(1)).delete(likes);
    }

    @Test
    @DisplayName("좋아요 토글 - 회원 미존재")
    void toggleLikeBook_memberNotFound() {
        when(memberRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> likesService.toggleLikeBook("user@example.com", 1L));

        assertEquals("회원 이메일이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("좋아요 토글 - 판매책 미존재")
    void toggleLikeBook_sellingBookNotFound() {
        Member member = mock(Member.class);
        when(memberRepository.findByEmail("user@example.com")).thenReturn(Optional.of(member));
        when(sellingBookRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> likesService.toggleLikeBook("user@example.com", 1L));

        assertEquals("판매책 ID가 유효하지 않습니다.", exception.getMessage());
    }
}