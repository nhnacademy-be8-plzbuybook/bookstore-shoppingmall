package com.nhnacademy.book.cartbook.service.impl;

import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.BookImage;
import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.book.repository.BookImageRepository;
import com.nhnacademy.book.book.repository.SellingBookRepository;
import com.nhnacademy.book.cart.repository.CartRepository;
import com.nhnacademy.book.cartbook.dto.request.CreateCartBookRequestDto;
import com.nhnacademy.book.cartbook.dto.request.UpdateCartBookRequestDto;
import com.nhnacademy.book.cartbook.dto.response.ReadCartBookResponseDto;
import com.nhnacademy.book.cartbook.entity.CartBook;
import com.nhnacademy.book.cartbook.exception.BookStatusNotSellingBookException;
import com.nhnacademy.book.cartbook.repository.CartBookRedisRepository;
import com.nhnacademy.book.cartbook.repository.CartBookRepository;
import com.nhnacademy.book.member.domain.Cart;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.exception.MemberNotFoundException;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CartBookMemberServiceImplTest {

    @Mock
    private CartBookRepository cartBookRepository;

    @Mock
    private SellingBookRepository sellingBookRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BookImageRepository bookImageRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartBookRedisRepository cartBookRedisRepository;

    @InjectMocks
    private CartBookMemberServiceImpl cartBookMemberService;

    private Member testMember;
    private Cart testCart;
    private SellingBook testSellingBook;
    private CartBook testCartBook;
    private Book testBook;
    private BookImage testBookImage;
    private final String TEST_EMAIL = "test@test.com";
    private final Long TEST_MEMBER_ID = 1L;

    @BeforeEach
    void setUp() {
        testMember = new Member();
        testMember.setMemberId(TEST_MEMBER_ID);
        testMember.setEmail(TEST_EMAIL);

        testCart = new Cart(testMember);

        testBook = new Book();
        testBook.setBookTitle("Test Book");

        testSellingBook = new SellingBook();
        testSellingBook.setBook(testBook);
        testSellingBook.setSellingBookPrice(BigDecimal.valueOf(10000));
        testSellingBook.setSellingBookStock(100);
        testSellingBook.setSellingBookStatus(SellingBook.SellingBookStatus.SELLING);

        testCartBook = new CartBook(2, testSellingBook, testCart);

        testBookImage = new BookImage();
        testBookImage.setImageUrl("test-image-url");
    }

    @Test
    @DisplayName("회원 장바구니 도서 목록 조회 성공 테스트 - Redis에 데이터가 없는 경우")
    void readAllCartMember_Success_NoRedisData() {
        // given
        given(memberRepository.getMemberIdByEmail(TEST_EMAIL)).willReturn(TEST_MEMBER_ID);
        given(cartRepository.findByMember_MemberId(TEST_MEMBER_ID)).willReturn(Optional.of(testCart));
        given(cartBookRepository.findAllByCart(testCart)).willReturn(Arrays.asList(testCartBook));
        given(cartBookRedisRepository.readAllHashName("Member" + TEST_MEMBER_ID)).willReturn(List.of());
        given(bookImageRepository.findByBook(testBook)).willReturn(Optional.of(testBookImage));

        // when
        List<ReadCartBookResponseDto> result = cartBookMemberService.readAllCartMember(TEST_EMAIL);

        // then
        assertThat(result).hasSize(1);
        ReadCartBookResponseDto dto = result.get(0);
        assertThat(dto.bookTitle()).isEqualTo(testBook.getBookTitle());
        assertThat(dto.sellingBookPrice()).isEqualTo(testSellingBook.getSellingBookPrice());
    }

    @Test
    @DisplayName("회원 장바구니에 도서 추가 성공 테스트")
    void createBookCartMember_Success() {
        // given
        CreateCartBookRequestDto requestDto = new CreateCartBookRequestDto(testSellingBook.getSellingBookId(), 1);

        given(memberRepository.getMemberIdByEmail(TEST_EMAIL)).willReturn(TEST_MEMBER_ID);
        given(cartRepository.findByMember_MemberId(TEST_MEMBER_ID)).willReturn(Optional.of(testCart));
        given(sellingBookRepository.findById(any())).willReturn(Optional.of(testSellingBook));
        given(cartBookRepository.save(any(CartBook.class))).willReturn(testCartBook);
        given(bookImageRepository.findByBook(testBook)).willReturn(Optional.of(testBookImage));

        // when
        Long result = cartBookMemberService.createBookCartMember(requestDto, TEST_EMAIL);

        // then
        assertThat(result).isEqualTo(testCartBook.getId());
        verify(cartBookRedisRepository).create(eq("Member" + TEST_MEMBER_ID), any(), any());
    }

    @Test
    @DisplayName("회원 장바구니 도서 수량 수정 성공 테스트")
    void updateBookCartMember_Success() {
        // given
        UpdateCartBookRequestDto requestDto = new UpdateCartBookRequestDto(testCartBook.getId(),testSellingBook.getSellingBookId(), 3);

        given(memberRepository.getMemberIdByEmail(TEST_EMAIL)).willReturn(TEST_MEMBER_ID);
        given(cartRepository.findByMember_MemberId(TEST_MEMBER_ID)).willReturn(Optional.of(testCart));
        given(cartBookRepository.findBySellingBook_SellingBookIdAndCart_CartId(any(), any()))
                .willReturn(Optional.of(testCartBook));
        given(sellingBookRepository.findById(any())).willReturn(Optional.of(testSellingBook));

        // when
        Long result = cartBookMemberService.updateBookCartMember(requestDto, TEST_EMAIL);

        // then
        assertThat(result).isEqualTo(testCartBook.getId());
        verify(cartBookRepository).save(any(CartBook.class));
        verify(cartBookRedisRepository).update(eq("Member" + TEST_MEMBER_ID), any(), eq(3));
    }

    @Test
    @DisplayName("회원 장바구니 도서 삭제 성공 테스트")
    void deleteBookCartMember_Success() {
        // given
        given(memberRepository.getMemberIdByEmail(TEST_EMAIL)).willReturn(TEST_MEMBER_ID);
        given(cartBookRepository.findById(any())).willReturn(Optional.of(testCartBook));

        // when
        String result = cartBookMemberService.deleteBookCartMember(testCartBook.getId(), TEST_EMAIL);

        // then
        assertThat(result).isEqualTo(TEST_EMAIL);
        verify(cartBookRepository).delete(any(CartBook.class));
        verify(cartBookRedisRepository).delete(eq("Member" + TEST_MEMBER_ID), any());
    }

    @Test
    @DisplayName("회원 장바구니 전체 비우기 성공 테스트")
    void deleteAllBookCartMember_Success() {
        // given
        given(memberRepository.getMemberIdByEmail(TEST_EMAIL)).willReturn(TEST_MEMBER_ID);
        given(cartRepository.findByMember_MemberId(TEST_MEMBER_ID)).willReturn(Optional.of(testCart));

        // when
        String result = cartBookMemberService.deleteAllBookCartMember(TEST_EMAIL);

        // then
        assertThat(result).isEqualTo(TEST_EMAIL);
        verify(cartBookRepository).deleteByCart(testCart);
        verify(cartBookRedisRepository).deleteAll("Member" + TEST_MEMBER_ID);
    }

    @Test
    @DisplayName("존재하지 않는 회원의 장바구니 조회 실패 테스트")
    void readAllCartMember_MemberNotFound() {
        // given
        given(memberRepository.getMemberIdByEmail(TEST_EMAIL))
                .willThrow(new MemberNotFoundException("회원을 찾을 수 없습니다."));

        // when & then
        assertThatThrownBy(() -> cartBookMemberService.readAllCartMember(TEST_EMAIL))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("판매 중지된 도서 장바구니 추가 실패 테스트")
    void createBookCartMember_BookNotSelling() {
        // given
        CreateCartBookRequestDto requestDto = new CreateCartBookRequestDto(testSellingBook.getSellingBookId(), 1);
        testSellingBook.setSellingBookStatus(SellingBook.SellingBookStatus.SELLEND);

        given(memberRepository.getMemberIdByEmail(TEST_EMAIL)).willReturn(TEST_MEMBER_ID);
        given(cartRepository.findByMember_MemberId(TEST_MEMBER_ID)).willReturn(Optional.of(testCart));
        given(sellingBookRepository.findById(any())).willReturn(Optional.of(testSellingBook));

        // when & then
        assertThatThrownBy(() -> cartBookMemberService.createBookCartMember(requestDto, TEST_EMAIL))
                .isInstanceOf(BookStatusNotSellingBookException.class);
    }
//
//    @Test
//    void readAllCartMember() {
//    }
//
//    @Test
//    void createBookCartMember() {
//    }
//
//    @Test
//    void updateBookCartMember() {
//    }
//
//    @Test
//    void deleteBookCartMember() {
//    }
//
//    @Test
//    void deleteAllBookCartMember() {
//    }
}