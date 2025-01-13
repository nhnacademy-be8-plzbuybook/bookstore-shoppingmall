package com.nhnacademy.book.cartbook.service.impl;

import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.BookImage;
import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.book.exception.BookNotFoundException;
import com.nhnacademy.book.book.repository.BookImageRepository;
import com.nhnacademy.book.book.repository.SellingBookRepository;
import com.nhnacademy.book.cartbook.dto.request.CreateCartBookRequestDto;
import com.nhnacademy.book.cartbook.dto.response.ReadCartBookResponseDto;
import com.nhnacademy.book.cartbook.repository.CartBookRedisRepository;
import com.nhnacademy.book.cartbook.service.CartBookGuestService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class CartBookGuestServiceImpl implements CartBookGuestService {
    private final CartBookRedisRepository cartBookRedisRepository;
    private final SellingBookRepository sellingBookRepository;
    private final BookImageRepository bookImageRepository;

    public CartBookGuestServiceImpl(CartBookRedisRepository cartBookRedisRepository,
                                    SellingBookRepository sellingBookRepository,
                                    BookImageRepository bookImageRepository) {
        this.cartBookRedisRepository = cartBookRedisRepository;
        this.sellingBookRepository = sellingBookRepository;
        this.bookImageRepository = bookImageRepository;
    }


    @Override
    public Long AddtoGuestCart(CreateCartBookRequestDto createCartBookRequestDto, HttpServletRequest request) {
        String sessionId = getSessionId(request);

        Long cartId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;

        SellingBook sellingBook = sellingBookRepository.findById(createCartBookRequestDto.sellingBookId())
                .orElseThrow(() -> new BookNotFoundException("비회원 장바구니에서 찾는 도서가 존재하지 않습니다."));

        List<ReadCartBookResponseDto> existingCartItems = cartBookRedisRepository.readAllHashName("Guest:" + sessionId);
        Long cartBookId = (long) (existingCartItems.size() + 1); // 자동으로 증가

        ReadCartBookResponseDto readCartBookResponseDto = ReadCartBookResponseDto.builder()
                .cartId(cartId)
                .cartBookId(cartBookId)
                .sellingBookId(sellingBook.getSellingBookId())
                .bookTitle(sellingBook.getBookTitle())
                .sellingBookPrice(sellingBook.getSellingBookPrice())
                .imageUrl(getImageUrl(sellingBook, bookImageRepository))
                .quantity(createCartBookRequestDto.quantity())
                .sellingBookStock(sellingBook.getSellingBookStock())
                .build();
        return cartBookRedisRepository.create("Guest:" + sessionId, cartId, readCartBookResponseDto);

    }


    @Override
    public Long updateGuestCartItem(Long sellingBookId, int quantity, HttpServletRequest request) {
        String sessionId = getSessionId(request);
        return cartBookRedisRepository.update("Guest:" + sessionId, sellingBookId, quantity);
    }

    @Override
    public Long removeItemFromGuestCart(Long sellingBookId, HttpServletRequest request) {
        String sessionId = getSessionId(request);
        return cartBookRedisRepository.delete("Guest:" + sessionId, sellingBookId);
    }

    @Override
    public void clearGuestCart(HttpServletRequest request) {
        String sessionId = getSessionId(request);
        cartBookRedisRepository.deleteAll("Guest:" + sessionId);
    }

    @Override
    public List<ReadCartBookResponseDto> getGuestCart(HttpServletRequest request) {
        String sessionId = getSessionId(request);
        log.debug("sessionId: {}", sessionId);
        return cartBookRedisRepository.readAllHashName("Guest:" + sessionId);
    }

    private String getImageUrl(SellingBook sellingBook, BookImageRepository bookImageRepository) {
        Book book =  sellingBook.getBook(); // 판매책으로 책정보를 가져옴
        BookImage bookImage = bookImageRepository.findByBook(book).orElse(null); // 책으로 책이미지를 가져옴
        String url = bookImage.getImageUrl(); // 책이미지에서 이미지 url 을 가져옴

        if(url != null && !url.isEmpty()) {
            log.info("책에 대한 이미지 url이 존재하지 않습니다.");
        }
        return url;
    }

    private String getSessionId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if("JSESSIONID".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
//
//    @Override
//    public Long createCartBook(Long sellingBookId, Long cartId, int quantity) {
//        // 판매 도서 조회
//        SellingBook sellingBook = sellingBookRepository.findById(sellingBookId)
//                .orElseThrow(() -> new BookNotFoundException("장바구니에서 찾는 도서가 존재하지 않습니다."));
//
//        Cart cart;
//        if(Objects.isNull(cartId) || cartId == 0) {
//            cart = new Cart();
//            cartRepository.save(cart);
//            cartId = cart.getCartId();
//        } else {
//            cart = cartRepository.findById(cartId)
//                    .orElseThrow(() -> new CartBookDoesNotExistException("장바구니가 존재하지 않습니다."));
//        }
//
//        if(cartBookRepository.existsCartBookBySellingBook_SellingBookIdAndCart(sellingBookId, cart)) {
//            updateCartBook(sellingBookId, cartId, quantity);
//        } else {
//            CartBook cartBook = new CartBook(quantity, sellingBook, cart);
//            cartBookRepository.save(cartBook);
//
//            Book book = cartBook.getSellingBook().getBook();
//            BookImage bookImage = bookImageRepository.findByBook(book).orElse(null);
//            String url = bookImage.getImageUrl();
//
//            if(url == null || url.isEmpty()) {
//                log.info("책에 대한 이미지 URL이 존재하지 않습니다.");
//            }
//
//            cartBookRedisRepository.create(
//                    Long.toString(cartId),
//                    cartBook.getId(),
//                    ReadCartBookResponseDto.builder()
//                            .cartBookId(cartBook.getId())
//                            .sellingBookId(sellingBookId)
//                            .bookTitle(sellingBook.getBookTitle())
//                            .sellingBookPrice(sellingBook.getSellingBookPrice())
//                            .imageUrl(url)
//                            .quantity(cartBook.getQuantity())
//                            .sellingBookStock(sellingBook.getSellingBookStock())
//                            .build()
//            );
//        }
//
//        return cartId;
//    }
//
//    @Override
//    public Long updateCartBook(Long sellingBookId, Long cartId, int quantity) {
//        SellingBook sellingBook = sellingBookRepository.findById(sellingBookId)
//                .orElseThrow(()->new SellingBookNotFoundException("장바구니에서" + sellingBookId.toString() + "가 존재하지 않습니다."));
//
//        Cart cart = cartRepository.findById(cartId)
//                .orElseThrow(()->new CartBookDoesNotExistException(cartId.toString() + "가 존재하지 않습니다."));
//
//        Optional<CartBook> optionalCartBook = cartBookRepository.findBySellingBookAndCart(sellingBook, cart);
//
//        if(optionalCartBook.isEmpty()) {
//            cartBookRepository.save(new CartBook(quantity, sellingBook, cart));
//            return cartId;
//        }
//        hasDataToLoad(cartId);
//        CartBook cartBook = optionalCartBook.get();
//
//        int amount = cartBook.getQuantity() - quantity;
//        if(amount > 0) {
//            cartBook.setQuantity(quantity);
//            cartBookRepository.save(cartBook);
//            cartBookRedisRepository.update(cartId.toString(), cartBook.getId(), amount);
//        } else {
//            cartBookRepository.delete(cartBook);
//            cartBookRedisRepository.delete(cartId.toString(), cartBook.getId());
//        }
//
//        return cart.getCartId();
//    }
//
//    @Override
//    public Long deleteCartBook(Long cartBookId, Long cartId) {
//        CartBook cartBook = cartBookRepository.findById(cartBookId)
//                .orElseThrow(()->new CartBookDoesNotExistException("북카트가 존재하지 않습니다."));
//
//        cartBookRepository.delete(cartBook);
//        cartBookRedisRepository.delete(cartId.toString(), cartBook.getId());
//
//        return cartBookId;
//    }
//
//    @Override
//    public String deleteAllGuestBookCart(Long cartId) {
//        Cart cart = cartRepository.findById(cartId)
//                .orElseThrow(()->new CartBookDoesNotExistException("장바구가 존재하지 않습니다."));
//
//        cartBookRepository.deleteByCart(cart);
//        cartBookRedisRepository.deleteAll(cartId.toString());
//
//
//        return cartId.toString();
//    }
//
//    @Override
//    public List<ReadCartBookResponseDto> readAllCartBook(Long cartId) {
//        List<ReadCartBookResponseDto> cartBookGuestResponseDtoList = cartBookRedisRepository.readAllHashName(cartId.toString());
//        if(!cartBookGuestResponseDtoList.isEmpty()) {
//            return cartBookGuestResponseDtoList;
//        }
//        return hasDataToLoad(cartId);
//    }
//
//
//
//
//    private List<ReadCartBookResponseDto> hasDataToLoad(Long cartId) {
//        List<ReadCartBookResponseDto> readBookCartGuestResponses = readAllFromDb(cartId);
//        if (cartBookRedisRepository.isMiss(cartId.toString()) && !readBookCartGuestResponses.isEmpty()) {
//            cartBookRedisRepository.loadData(readBookCartGuestResponses, cartId.toString());
//        }
//        return readBookCartGuestResponses;
//    }
//
//    private List<ReadCartBookResponseDto> readAllFromDb(Long cartId) {
//        List<CartBook> list = cartBookRepository.findAllByCart_CartId(cartId);
//        List<ReadCartBookResponseDto> listDto = new ArrayList<>();
//        log.info("{}", list);
//        for (CartBook cartBook : list) {
//            Book book =  cartBook.getSellingBook().getBook(); // 판매책으로 책정보를 가져옴
//            BookImage bookImage = bookImageRepository.findByBook(book).orElse(null); // 책으로 책이미지를 가져옴
//            String url = bookImage.getImageUrl(); // 책이미지에서 이미지 url 을 가져옴
//
//            if(url != null && !url.isEmpty()) {
//                log.info("책에 대한 이미지 url이 존재하지 않습니다.");
//            }
//            listDto.add(ReadCartBookResponseDto.builder()
//                    .cartBookId(cartBook.getId())
//                    .sellingBookId(cartBook.getSellingBook().getSellingBookId())
//                    .sellingBookPrice(cartBook.getSellingBook().getSellingBookPrice())
//                    .imageUrl(url)
//                    .bookTitle(cartBook.getSellingBook().getBookTitle())
//                    .quantity(cartBook.getQuantity())
//                    .sellingBookStock(cartBook.getSellingBook().getSellingBookStock())
//                    .build());
//        }
//
//        return listDto;
//    }
}
