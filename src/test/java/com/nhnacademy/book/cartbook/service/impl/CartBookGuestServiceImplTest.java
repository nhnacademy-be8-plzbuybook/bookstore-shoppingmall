package com.nhnacademy.book.cartbook.service.impl;

import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.BookImage;
import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.book.exception.BookNotFoundException;
import com.nhnacademy.book.book.repository.BookImageRepository;
import com.nhnacademy.book.book.repository.SellingBookRepository;
import com.nhnacademy.book.cartbook.dto.request.CreateCartBookRequestDto;
import com.nhnacademy.book.cartbook.dto.response.ReadCartBookResponseDto;
import com.nhnacademy.book.cartbook.exception.BookStatusNotSellingBookException;
import com.nhnacademy.book.cartbook.repository.CartBookRedisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartBookGuestServiceImplTest {

    @Mock
    private CartBookRedisRepository cartBookRedisRepository;

    @Mock
    private SellingBookRepository sellingBookRepository;

    @Mock
    private BookImageRepository bookImageRepository;

    @Spy
    @InjectMocks
    private CartBookGuestServiceImpl cartBookGuestService;

    private SellingBook testSellingBook;
    private Book testBook;
    private BookImage testBookImage;


    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setBookId(1L);
        testBook.setBookTitle("Test Book");

        testSellingBook = new SellingBook();
        testSellingBook.setSellingBookId(1L);
        testSellingBook.setBook(testBook);
        testSellingBook.setSellingBookPrice(BigDecimal.valueOf(10000));
        testSellingBook.setSellingBookStock(100);
        testSellingBook.setSellingBookStatus(SellingBook.SellingBookStatus.SELLING);

        testBookImage = new BookImage();
        testBookImage.setImageUrl("http://example.com/image.jpg");

    }

    @Test
    @DisplayName("비회원 장바구니에 도서 추가 성공 테스트")
    void addToGuestCart_Success() {
        // given
        CreateCartBookRequestDto requestDto = new CreateCartBookRequestDto(testSellingBook.getSellingBookId(), 1);

        // 모킹: 판매 도서가 존재하도록 설정
        given(sellingBookRepository.findById(testSellingBook.getSellingBookId()))
                .willReturn(Optional.of(testSellingBook));

        // 모킹: 책 이미지 리포지토리에서 이미지 URL을 가져올 수 있도록 설정
        given(bookImageRepository.findByBook(testBook)).willReturn(Optional.of(testBookImage));

        // 모킹: Redis에서 해당 세션의 장바구니가 비어 있다고 설정
        given(cartBookRedisRepository.isHit("Guest:" + "sessionId")).willReturn(false);

        // 모킹: 장바구니에 도서를 추가할 때 반환할 ID 설정
        given(cartBookRedisRepository.create(eq("Guest:" + "sessionId"), any(), any())).willReturn(1L);

        // when
        Long result = cartBookGuestService.addToGuestCart(requestDto, "sessionId");

        // then
        assertThat(result).isEqualTo(1L);
        verify(cartBookRedisRepository).create(eq("Guest:" + "sessionId"), any(), any());
    }

    @Test
    @DisplayName("비회원 장바구니에 도서 추가 실패 - 도서가 존재하지 않는 경우")
    void addToGuestCart_BookNotFound() {
        // given
        CreateCartBookRequestDto requestDto = new CreateCartBookRequestDto(999L, 1); // 존재하지 않는 ID

        // 모킹: 판매 도서를 찾을 수 없도록 설정
        given(sellingBookRepository.findById(requestDto.sellingBookId()))
                .willReturn(Optional.empty());

        // when & then
        assertThrows(BookNotFoundException.class, () -> cartBookGuestService.addToGuestCart(requestDto, "sessionId"));
    }

    @Test
    @DisplayName("비회원 장바구니에 도서 추가 실패 - 도서 상태가 판매 중이 아닌 경우")
    void addToGuestCart_BookNotSelling() {
        // given
        CreateCartBookRequestDto requestDto = new CreateCartBookRequestDto(testSellingBook.getSellingBookId(), 1);
        testSellingBook.setSellingBookStatus(SellingBook.SellingBookStatus.SELLEND); // 상태 변경

        // 모킹: 판매 도서가 존재하도록 설정
        given(sellingBookRepository.findById(testSellingBook.getSellingBookId()))
                .willReturn(Optional.of(testSellingBook));

        // when & then
        assertThrows(BookStatusNotSellingBookException.class, () -> cartBookGuestService.addToGuestCart(requestDto, "sessionId"));
    }

    @Test
    @DisplayName("비회원 장바구니 도서 수량 수정 성공 테스트")
    void updateGuestCartItem_Success() {
        // given
        String sessionId = "test-session";
        Long cartId = 1L;
        int newQuantity = 5;

        // 모킹: Redis에서 도서 수량 업데이트를 수행하도록 설정
        when(cartBookRedisRepository.update("Guest:" + sessionId, cartId, newQuantity)).thenReturn(cartId);

        // when
        Long updatedCartId = cartBookGuestService.updateGuestCartItem(cartId, newQuantity, sessionId);

        // then
        assertEquals(cartId, updatedCartId);
        verify(cartBookRedisRepository, times(1)).update("Guest:" + sessionId, cartId, newQuantity);
    }

    @Test
    @DisplayName("비회원 장바구니 도서 삭제 성공 테스트")
    void removeItemFromGuestCart_Success() {
        // given
        String sessionId = "test-session";
        Long cartId = 1L;

        // 모킹: Redis에서 도서 삭제를 수행하도록 설정
        when(cartBookRedisRepository.delete("Guest:" + sessionId, cartId)).thenReturn(cartId);

        // when
        Long removedCartId = cartBookGuestService.removeItemFromGuestCart(cartId, sessionId);

        // then
        assertEquals(cartId, removedCartId);
        verify(cartBookRedisRepository, times(1)).delete("Guest:" + sessionId, cartId);
    }

    @Test
    @DisplayName("비회원 장바구니 전체 비우기 성공 테스트")
    void clearGuestCart_Success() {
        // given
        String sessionId = "test-session";

        // when
        cartBookGuestService.clearGuestCart(sessionId);

        // then
        verify(cartBookRedisRepository, times(1)).deleteAll("Guest:" + sessionId);
    }

    @Test
    @DisplayName("비회원 장바구니 조회 성공 테스트")
    void getGuestCart_Success() {
        // given
        String sessionId = "test-session";
        List<ReadCartBookResponseDto> expectedCartItems = new ArrayList<>();
        ReadCartBookResponseDto dto = ReadCartBookResponseDto.builder()
                .cartId(1L)
                .cartBookId(1L)
                .sellingBookId(testSellingBook.getSellingBookId())
                .bookTitle(testBook.getBookTitle())
                .sellingBookPrice(testSellingBook.getSellingBookPrice())
                .imageUrl(testBookImage.getImageUrl())
                .quantity(1)
                .sellingBookStock(testSellingBook.getSellingBookStock())
                .build();
        expectedCartItems.add(dto);

        // 모킹: Redis에서 장바구니 아이템을 읽어올 수 있도록 설정
        given(cartBookRedisRepository.readAllHashName("Guest:" + sessionId)).willReturn(expectedCartItems);

        // when
        List<ReadCartBookResponseDto> result = cartBookGuestService.getGuestCart(sessionId);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).bookTitle()).isEqualTo(testBook.getBookTitle());
        verify(cartBookRedisRepository).readAllHashName("Guest:" + sessionId);
    }


}
