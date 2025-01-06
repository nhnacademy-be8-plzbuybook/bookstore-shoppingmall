package com.nhnacademy.book.cartbook.service.impl;

import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.BookImage;
import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.book.exception.SellingBookNotFoundException;
import com.nhnacademy.book.book.repository.BookImageRepository;
import com.nhnacademy.book.book.repository.SellingBookRepository;
import com.nhnacademy.book.cart.exception.CartNotFoundException;
import com.nhnacademy.book.cart.repository.CartRepository;
import com.nhnacademy.book.cartbook.dto.request.*;
import com.nhnacademy.book.cartbook.dto.response.CreateMemberCartBookResponseDto;
import com.nhnacademy.book.cartbook.dto.response.ReadAllMemberCartBookResponseDto;
import com.nhnacademy.book.cartbook.dto.response.ReadMemberCartBookResponseDto;
import com.nhnacademy.book.cartbook.dto.response.UpdateMemberCartBookResponseDto;
import com.nhnacademy.book.cartbook.entity.CartBook;
import com.nhnacademy.book.cartbook.exception.CartBookDoesNotExistException;
import com.nhnacademy.book.cartbook.exception.SellingBookNotFoundInBookCartException;
import com.nhnacademy.book.cartbook.repository.CartBookRepository;
import com.nhnacademy.book.cartbook.service.CartBookMemberService;
import com.nhnacademy.book.member.domain.Cart;
import com.nhnacademy.book.member.domain.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class CartBookMemberServiceImpl implements CartBookMemberService {
    private final CartBookRepository cartBookRepository;
    private final SellingBookRepository sellingBookRepository;
    private final MemberRepository memberRepository;
    private final BookImageRepository bookImageRepository;
    private final CartRepository cartRepository;

    public CartBookMemberServiceImpl(CartBookRepository cartBookRepository,
                                     SellingBookRepository sellingBookRepository,
                                     MemberRepository memberRepository,
                                     BookImageRepository bookImageRepository,
                                     CartRepository cartRepository) {
        this.cartBookRepository = cartBookRepository;
        this.sellingBookRepository = sellingBookRepository;
        this.memberRepository = memberRepository;
        this.bookImageRepository = bookImageRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public List<ReadAllMemberCartBookResponseDto> readAllCartMember(String email) {
        List<ReadAllMemberCartBookResponseDto> responses = new ArrayList<>();
        Cart cart = cartRepository.findByMember_MemberId(memberRepository.getMemberIdByEmail(email))
                .orElseThrow(()-> new CartNotFoundException("회원에 해당하는 장바구니를 찾을 수 없습니다."));

        List<CartBook> cartBooks = cartBookRepository.findAllByCart(cart);

        for (CartBook cartBook : cartBooks) {
            String url = getBookImageUrl(cartBook.getSellingBook().getBook());

            ReadAllMemberCartBookResponseDto response = ReadAllMemberCartBookResponseDto.builder()
                    .cartBookId(cartBook.getId())
                    .sellingBookId(cartBook.getSellingBook().getSellingBookId())
                    .bookTitle(cartBook.getSellingBook().getBookTitle())
                    .sellingBookPrice(cartBook.getSellingBook().getSellingBookPrice())
                    .imageUrl(url)
                    .quantity(cartBook.getQuantity())
                    .sellingBookStock(cartBook.getSellingBook().getSellingBookStock())
                    .build();

            responses.add(response);
        }

        return responses;
    }

    @Override
    public ReadMemberCartBookResponseDto readMemberCartBook(Long cartBookId, String email) {
//        Cart cart = cartRepository.findByMember_MemberId(memberRepository.getMemberIdByEmail(email))
//                .orElseThrow(()-> new CartNotFoundException("회원에 해당하는 장바구니를 찾을 수 없습니다."));

        CartBook cartBook = cartBookRepository.findById(cartBookId)
                .orElseThrow(()-> new CartBookDoesNotExistException("해당 책 장바구니를 찾을 수 없습니다."));

        return ReadMemberCartBookResponseDto.builder()
                .cartBookId(cartBook.getId())
                .quantity(cartBook.getQuantity())
                .sellingBookId(cartBook.getSellingBook().getSellingBookId())
                .sellingBookPrice(cartBook.getSellingBook().getSellingBookPrice())
                .sellingBookStock(cartBook.getSellingBook().getSellingBookStock())
                .title(cartBook.getSellingBook().getBookTitle())
                .url(getBookImageUrl(cartBook.getSellingBook().getBook()))
                .build();
    }

    @Override
    public CreateMemberCartBookResponseDto createBookCartMember(CreateCartBookRequestDto createCartBookRequestDto, String email) {
        Cart cart = cartRepository.findByMember_MemberId(memberRepository.getMemberIdByEmail(email))
                .orElseThrow(()-> new CartNotFoundException("회원에 해당하는 장바구니를 찾을 수 없습니다."));
//
        SellingBook sellingBook = sellingBookRepository.findById(createCartBookRequestDto.sellingBookId())
                .orElseThrow(() -> new SellingBookNotFoundException("책장바구니에서 판매책을 찾을 수 없습니다."));

        Optional<CartBook> cartBookOptional = cartBookRepository
                .findCartBookBySellingBook_SellingBookIdAndCart_CartId(createCartBookRequestDto.sellingBookId(), cart.getCartId());

        CartBook cartBook;

        if(cartBookOptional.isPresent()) { // 책 장바구니가 이미 존재하면 수량만 변경.
            cartBook = cartBookOptional.get();
            cartBook.setQuantity(cartBook.getQuantity() + createCartBookRequestDto.quantity());
        } else {
            cartBook = new CartBook( // 책 장바구니가 없는경우 새로 생성.
                    createCartBookRequestDto.quantity(),
                    sellingBook,
                    cart
            );
        }

        cartBookRepository.save(cartBook);

        String url = getBookImageUrl(cartBook.getSellingBook().getBook()); // 이미지 url 가져오기.

        return CreateMemberCartBookResponseDto.builder()
                .cartBookId(cartBook.getId())
                .sellingBookId(cartBook.getSellingBook().getSellingBookId())
                .bookTitle(cartBook.getSellingBook().getBookTitle())
                .sellingBookPrice(cartBook.getSellingBook().getSellingBookPrice())
                .imageUrl(url)
                .quantity(cartBook.getQuantity())
                .sellingBookStock(cartBook.getSellingBook().getSellingBookStock())
                .build();
    }




    @Override
    public UpdateMemberCartBookResponseDto updateBookCartMember(UpdateCartBookRequestDto updateCartBookRequestDto, String email) {
        Cart cart = cartRepository.findByMember_MemberId(memberRepository.getMemberIdByEmail(email))
                .orElseThrow(()-> new CartNotFoundException("회원에 해당하는 장바구니를 찾을 수 없습니다."));

        SellingBook sellingBook = sellingBookRepository.findById(updateCartBookRequestDto.sellingBookId())
                .orElseThrow(()-> new SellingBookNotFoundInBookCartException("책장바구니에서 판매책을 찾을 수 없습니다."));

        CartBook cartBook = cartBookRepository.findById(updateCartBookRequestDto.cartBookId())
                .filter(book -> book.getCart().getCartId().equals(cart.getCartId()))
                .orElseThrow(() -> new SellingBookNotFoundInBookCartException("장바구니에서 해당 책을 찾을 수 없습니다."));

        cartBook.setQuantity(updateCartBookRequestDto.quantity());
        cartBookRepository.save(cartBook);

        String url = getBookImageUrl(cartBook.getSellingBook().getBook()); // 이미지 url 가져오기.

        return UpdateMemberCartBookResponseDto.builder()
                .bookCartId(cartBook.getId())
                .sellingBookId(sellingBook.getSellingBookId())
                .sellingBookPrice(sellingBook.getSellingBookPrice())
                .url(url)
                .title(sellingBook.getBookTitle())
                .quantity(cartBook.getQuantity())
                .sellingBookStock(sellingBook.getSellingBookStock())
                .build();
    }

    @Override
    public ResponseEntity<Long> deleteBookCartMember(Long bookCartId, String email) {
        Cart cart = cartRepository.findByMember_MemberId(memberRepository.getMemberIdByEmail(email))
                .orElseThrow(()-> new CartNotFoundException("회원에 해당하는 장바구니를 찾을 수 없습니다."));

        CartBook cartBook = cartBookRepository.findById(bookCartId)
                .filter(book -> book.getCart().getCartId().equals(cart.getCartId()))
                .orElseThrow(() -> new IllegalArgumentException("장바구니에서 해당 상품을 찾을 수 없습니다."));

        cartBookRepository.delete(cartBook);
        return ResponseEntity.ok(cartBook.getId());
    }


    @Override
    public ResponseEntity<String> deleteAllBookCartMember(String email) {
        Cart cart = cartRepository.findByMember_MemberId(memberRepository.getMemberIdByEmail(email))
                .orElseThrow(()-> new CartNotFoundException("회원에 해당하는 장바구니를 찾을 수 없습니다."));

        cartBookRepository.deleteByCart(cart);
        return ResponseEntity.ok(email);
    }



    private String getBookImageUrl(Book book) {
        BookImage bookImage = bookImageRepository.findByBook(book).orElse(null);
        String url = (bookImage != null) ? bookImage.getImageUrl() : null;

        if (url == null || url.isEmpty()) {
            log.info("책에 대한 이미지 URL이 존재하지 않습니다.");
        }

        return url;
    }
}