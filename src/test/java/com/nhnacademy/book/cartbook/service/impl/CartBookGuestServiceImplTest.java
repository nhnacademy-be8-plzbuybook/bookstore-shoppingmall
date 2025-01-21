//package com.nhnacademy.book.cartbook.service.impl;
//
//import com.nhnacademy.book.book.entity.Book;
//import com.nhnacademy.book.book.entity.BookImage;
//import com.nhnacademy.book.book.entity.SellingBook;
//import com.nhnacademy.book.book.exception.BookNotFoundException;
//import com.nhnacademy.book.book.repository.BookImageRepository;
//import com.nhnacademy.book.book.repository.SellingBookRepository;
//import com.nhnacademy.book.cartbook.dto.request.CreateCartBookRequestDto;
//import com.nhnacademy.book.cartbook.dto.response.ReadCartBookResponseDto;
//import com.nhnacademy.book.cartbook.exception.BookStatusNotSellingBookException;
//import com.nhnacademy.book.cartbook.repository.CartBookRedisRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//
//@ExtendWith(MockitoExtension.class)
//class CartBookGuestServiceImplTest {
//
//    @Mock
//    private CartBookRedisRepository cartBookRedisRepository;
//
//    @Mock
//    private SellingBookRepository sellingBookRepository;
//
//    @Mock
//    private BookImageRepository bookImageRepository;
//
//    @InjectMocks
//    private CartBookGuestServiceImpl cartBookGuestService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testAddToGuestCart_Success() {
//        // given
//        String sessionId = "testSessionId";
//        CreateCartBookRequestDto requestDto = new CreateCartBookRequestDto(1L, 2);
//
//        SellingBook sellingBook = mock(SellingBook.class);
//        when(sellingBookRepository.findById(1L)).thenReturn(Optional.of(sellingBook));
//        when(sellingBook.getSellingBookStatus()).thenReturn(SellingBook.SellingBookStatus.SELLING);
//        when(sellingBook.getSellingBookId()).thenReturn(1L);
//        when(sellingBook.getBookTitle()).thenReturn("Test Book");
//        when(sellingBook.getSellingBookPrice()).thenReturn(BigDecimal.valueOf(1000));
//        when(sellingBook.getSellingBookStock()).thenReturn(10);
//
//        Book book = mock(Book.class);
//        when(sellingBook.getBook()).thenReturn(book);
//
//        BookImage bookImage = mock(BookImage.class);
//        when(bookImageRepository.findByBook(book)).thenReturn(Optional.of(bookImage));
//        when(bookImage.getImageUrl()).thenReturn("http://test.com/image.jpg");
//
//        when(cartBookRedisRepository.readAllHashName("Guest:" + sessionId)).thenReturn(Collections.emptyList());
//        when(cartBookRedisRepository.create(anyString(), anyLong(), any(ReadCartBookResponseDto.class))).thenReturn(1L);
//
//        // when
//        Long result = cartBookGuestService.AddToGuestCart(requestDto, sessionId);
//
//        // then
//        assertNotNull(result);
//        verify(cartBookRedisRepository, times(1)).create(anyString(), anyLong(), any(ReadCartBookResponseDto.class));
//    }
//
//    @Test
//    void testAddToGuestCart_BookNotFound() {
//        // given
//        String sessionId = "testSessionId";
//        CreateCartBookRequestDto requestDto = new CreateCartBookRequestDto(1L, 2);
//
//        when(sellingBookRepository.findById(1L)).thenReturn(Optional.empty());
//
//        // when & then
//        assertThrows(BookNotFoundException.class, () -> cartBookGuestService.AddToGuestCart(requestDto, sessionId));
//    }
//
//    @Test
//    void testAddToGuestCart_BookNotSelling() {
//        // given
//        String sessionId = "testSessionId";
//        CreateCartBookRequestDto requestDto = new CreateCartBookRequestDto(1L, 2);
//
//        SellingBook sellingBook = mock(SellingBook.class);
//        when(sellingBookRepository.findById(1L)).thenReturn(Optional.of(sellingBook));
//        when(sellingBook.getSellingBookStatus()).thenReturn(SellingBook.SellingBookStatus.SELLEND);
//
//        // when & then
//        assertThrows(BookStatusNotSellingBookException.class, () -> cartBookGuestService.AddToGuestCart(requestDto, sessionId));
//    }
//
//    @Test
//    void testUpdateGuestCartItem() {
//        // given
//        String sessionId = "testSessionId";
//        Long cartId = 1L;
//        int quantity = 3;
//
//        when(cartBookRedisRepository.update("Guest:" + sessionId, cartId, quantity)).thenReturn(1L);
//
//        // when
//        Long result = cartBookGuestService.updateGuestCartItem(cartId, quantity, sessionId);
//
//        // then
//        assertEquals(1L, result);
//        verify(cartBookRedisRepository, times(1)).update("Guest:" + sessionId, cartId, quantity);
//    }
//
//    @Test
//    void testRemoveItemFromGuestCart() {
//        // given
//        String sessionId = "testSessionId";
//        Long cartId = 1L;
//
//        when(cartBookRedisRepository.delete("Guest:" + sessionId, cartId)).thenReturn(1L);
//
//        // when
//        Long result = cartBookGuestService.removeItemFromGuestCart(cartId, sessionId);
//
//        // then
//        assertEquals(1L, result);
//        verify(cartBookRedisRepository, times(1)).delete("Guest:" + sessionId, cartId);
//    }
//
//    @Test
//    void testClearGuestCart() {
//        // given
//        String sessionId = "testSessionId";
//
//        doNothing().when(cartBookRedisRepository).deleteAll("Guest:" + sessionId);
//
//        // when
//        cartBookGuestService.clearGuestCart(sessionId);
//
//        // then
//        verify(cartBookRedisRepository, times(1)).deleteAll("Guest:" + sessionId);
//    }
//
//    @Test
//    void testGetGuestCart() {
//        // given
//        String sessionId = "testSessionId";
//        List<ReadCartBookResponseDto> cartItems = Collections.emptyList();
//
//        when(cartBookRedisRepository.readAllHashName("Guest:" + sessionId)).thenReturn(cartItems);
//
//        // when
//        List<ReadCartBookResponseDto> result = cartBookGuestService.getGuestCart(sessionId);
//
//        // then
//        assertEquals(cartItems, result);
//        verify(cartBookRedisRepository, times(1)).readAllHashName("Guest:" + sessionId);
//    }
//}