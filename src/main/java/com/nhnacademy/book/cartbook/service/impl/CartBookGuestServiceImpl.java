//package com.nhnacademy.book.cartbook.service.impl;
//
//import com.nhnacademy.book.book.entity.Book;
//import com.nhnacademy.book.book.entity.BookImage;
//import com.nhnacademy.book.book.entity.SellingBook;
//import com.nhnacademy.book.book.exception.BookNotFoundException;
//import com.nhnacademy.book.book.exception.SellingBookNotFoundException;
//import com.nhnacademy.book.book.repository.BookImageRepository;
//import com.nhnacademy.book.book.repository.SellingBookRepository;
//import com.nhnacademy.book.cartbook.dto.response.ReadGuestCartBookResponseDto;
//import com.nhnacademy.book.cartbook.entity.CartBook;
//import com.nhnacademy.book.cartbook.exception.CartBookDoesNotExistException;
//import com.nhnacademy.book.cartbook.repository.CartBookRedisRepository;
//import com.nhnacademy.book.cartbook.repository.CartBookRepository;
//import com.nhnacademy.book.cartbook.service.CartBookGuestService;
//import com.nhnacademy.book.cart.repository.CartRepository;
//import com.nhnacademy.book.member.domain.Cart;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Transactional
//@Slf4j
//public class CartBookGuestServiceImpl implements CartBookGuestService {
//    private final CartBookRedisRepository cartBookRedisRepository;
//    private final CartBookRepository cartBookRepository;
//    private final SellingBookRepository sellingBookRepository;
//    private final CartRepository cartRepository;
//    private final BookImageRepository bookImageRepository;
//
//    public CartBookGuestServiceImpl(CartBookRedisRepository cartBookRedisRepository,
//                                    CartBookRepository cartBookRepository,
//                                    SellingBookRepository sellingBookRepository,
//                                    CartRepository cartRepository,
//                                    BookImageRepository bookImageRepository) {
//        this.cartBookRedisRepository = cartBookRedisRepository;
//        this.cartBookRepository = cartBookRepository;
//        this.sellingBookRepository = sellingBookRepository;
//        this.cartRepository = cartRepository;
//        this.bookImageRepository = bookImageRepository;
//    }
//
//    @Override
//    public Optional<Cart> readCartBook(Long cartId, Long memberId) {
//        return Optional.empty();
//    }
//
//    @Override
//    public Long createCartBook(Long sellingBookId, int quantity) {
//        // 판매 도서 조회
//        SellingBook sellingBook = sellingBookRepository.findById(sellingBookId)
//                .orElseThrow(() -> new BookNotFoundException("도서가 존재하지 않습니다."));
//
//        // 새로운 Cart 생성
//        Cart cart = new Cart();
//        cartRepository.save(cart);
//        Long cartId = cart.getCartId();
//
//        // CartBook 생성 및 저장
//        CartBook cartBook = new CartBook(quantity, sellingBook, cart);
//        cartBookRepository.save(cartBook);
//
//        // 책 이미지 URL 조회
//        Book book = cartBook.getSellingBook().getBook();
//        BookImage bookImage = bookImageRepository.findByBook(book).orElse(null);
//        String url = bookImage != null ? bookImage.getImageUrl() : null;
//
//        if (url == null || url.isEmpty()) {
//            log.info("책에 대한 이미지 URL이 존재하지 않습니다.");
//        }
//
//        // Redis에 저장
//        cartBookRedisRepository.create(
//                Long.toString(cartId),
//                cartBook.getId(),
//                ReadGuestCartBookResponseDto.builder()
//                        .cartBookId(cartBook.getId())
//                        .bookId(sellingBookId)
//                        .bookTitle(sellingBook.getBookTitle())
//                        .sellingBookPrice(sellingBook.getSellingBookPrice())
//                        .imageUrl(url)
//                        .quantity(cartBook.getQuantity())
//                        .sellingBookStock(sellingBook.getSellingBookStock())
//                        .sellingBookPackageable(sellingBook.getSellingBookPackageable())
//                        .used(sellingBook.getUsed())
//                        .build()
//        );
//
//        return cartId;
//    }
//
//    @Override
//    public Long updateCartBook(Long bookId, Long cartId, int quantity) {
//        SellingBook sellingBook = sellingBookRepository.findById(bookId)
//                .orElseThrow(()->new SellingBookNotFoundException("장바구니에서" + bookId.toString() + "가 존재하지 않습니다."));
//        Cart cart = cartRepository.findById(cartId)
//                .orElseThrow(()->new CartDoesNotExistsException(cartId.toString() + "가 존재하지 않습니다."));
//
//        Optional<CartBook> optionalBookCart = cartBookRepository.findBySellingBookAndCart(sellingBook, cart);
//
//        if(optionalBookCart.isEmpty()) {
//            cartBookRepository.save(new CartBook(quantity, sellingBook, cart));
//            return cartId;
//        }
//        hasDataToLoad(cartId);
//        CartBook cartBook = optionalBookCart.get();
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
//        cartBookRedisRepository.delete(cartBookId.toString(), cartBook.getId());
//
//        return cartBookId;
//    }
//
//    @Override
//    public Long deleteAllGuestBookCart(Long cartId) {
//        Cart cart = cartRepository.findById(cartId)
//                .orElseThrow(()->new CartDoesNotExistsException("카트가 존재하지 않습니다."));
//
//        cartBookRedisRepository.deleteAll(cartId.toString());
//
//        return cartId;
//    }
//
//    @Override
//    public List<ReadGuestCartBookResponseDto> readAllCartBook(Long cartId) {
//        List<ReadGuestCartBookResponseDto> cartBookGuestResponseDtoList = cartBookRedisRepository.readAllHashName(cartId.toString());
//        if(!cartBookGuestResponseDtoList.isEmpty()) {
//            return cartBookGuestResponseDtoList;
//        }
//        return hasDataToLoad(cartId);
//    }
//
//
//
//
//    private List<ReadGuestCartBookResponseDto> hasDataToLoad(Long cartId) {
//        List<ReadGuestCartBookResponseDto> readBookCartGuestResponses = readAllFromDb(cartId);
//        if (cartBookRedisRepository.isMiss(cartId.toString()) && !readBookCartGuestResponses.isEmpty()) {
//            cartBookRedisRepository.loadData(readBookCartGuestResponses, cartId.toString());
//        }
//        return readBookCartGuestResponses;
//    }
//
//    private List<ReadGuestCartBookResponseDto> readAllFromDb(Long cartId) {
//        List<CartBook> list = cartBookRepository.findAllByCart_CartId(cartId);
//        List<ReadGuestCartBookResponseDto> listDto = new ArrayList<>();
//        log.info("{}", list);
//        for (CartBook cartBook : list) {
//            Book book =  cartBook.getSellingBook().getBook(); // 판매책으로 책정보를 가져옴
//            BookImage bookImage = bookImageRepository.findByBook(book).orElse(null); // 책으로 책이미지를 가져옴
//            String url = bookImage.getImageUrl(); // 책이미지에서 이미지 url 을 가져옴
//
//            if(url != null && !url.isEmpty()) {
//                log.info("책에 대한 이미지 url이 존재하지 않습니다.");
//            }
//            listDto.add(ReadGuestCartBookResponseDto.builder()
//                    .cartBookId(cartBook.getId())
//                    .bookId(cartBook.getSellingBook().getSellingBookId())
//                    .sellingBookPrice(cartBook.getSellingBook().getSellingBookPrice())
//                    .imageUrl(url)
//                    .bookTitle(cartBook.getSellingBook().getBookTitle())
//                    .quantity(cartBook.getQuantity())
//                    .sellingBookStock(cartBook.getSellingBook().getSellingBookStock())
//                    .sellingBookPackageable(cartBook.getSellingBook().getSellingBookPackageable())
//                    .used(cartBook.getSellingBook().getUsed())
//                    .build());
//        }
//
//        return listDto;
//    }
//}
