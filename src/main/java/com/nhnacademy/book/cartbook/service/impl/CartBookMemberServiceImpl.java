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
import com.nhnacademy.book.cartbook.dto.response.*;
import com.nhnacademy.book.cartbook.entity.CartBook;
import com.nhnacademy.book.cartbook.exception.SellingBookNotFoundInBookCartException;
import com.nhnacademy.book.cartbook.repository.CartBookRedisRepository;
import com.nhnacademy.book.cartbook.repository.CartBookRepository;
import com.nhnacademy.book.cartbook.service.CartBookMemberService;
import com.nhnacademy.book.member.domain.Cart;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.exception.MemberNotFoundException;
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
    private final CartBookRedisRepository cartBookRedisRepository;

    public CartBookMemberServiceImpl(CartBookRepository cartBookRepository,
                                     SellingBookRepository sellingBookRepository,
                                     MemberRepository memberRepository,
                                     BookImageRepository bookImageRepository,
                                     CartRepository cartRepository,
                                     CartBookRedisRepository cartBookRedisRepository) {
        this.cartBookRepository = cartBookRepository;
        this.sellingBookRepository = sellingBookRepository;
        this.memberRepository = memberRepository;
        this.bookImageRepository = bookImageRepository;
        this.cartRepository = cartRepository;
        this.cartBookRedisRepository = cartBookRedisRepository;
    }

    @Override
    public List<ReadAllMemberCartBookResponseDto> readAllCartMember(String email) {
        List<ReadAllMemberCartBookResponseDto> responses = new ArrayList<>();

        Long memberId = memberRepository.getMemberIdByEmail(email);

        Optional<Cart> cartOptional = cartRepository.findByMember_MemberId(memberId);

        Cart cart = cartOptional.orElseGet(() -> new Cart(memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("장바구니에서 해당 이메일로 가입된 회원을 찾을 수 없습니다."))));

        cartRepository.save(cart);
        List<ReadGuestCartBookResponseDto> redisResponselist = cartBookRedisRepository.readAllHashName("Member" + memberId);

        if(redisResponselist.isEmpty()) {
            List<CartBook> cartBookList = cartBookRepository.findAllByCart(cart);
            for(CartBook cartBook : cartBookList) {
                String url = getBookImageUrl(cartBook.getSellingBook().getBook());

                cartBookRedisRepository.create("Member" + memberId,
                        cartBook.getId(),
                        ReadGuestCartBookResponseDto.builder()
                                .cartBookId(cartBook.getId())
                                .sellingBookId(cartBook.getSellingBook().getSellingBookId())
                                .bookTitle(cartBook.getSellingBook().getBookTitle())
                                .sellingBookPrice(cartBook.getSellingBook().getSellingBookPrice())
                                .imageUrl(url)
                                .quantity(cartBook.getQuantity())
                                .sellingBookStock(cartBook.getSellingBook().getSellingBookStock())
                                .sellingBookPackageable(cartBook.getSellingBook().getSellingBookPackageable())
                                .used(cartBook.getSellingBook().getUsed())
                                .build()
                );
                responses.add(ReadAllMemberCartBookResponseDto.builder()
                                .quantity(cartBook.getQuantity())
                                .cartBookId(cartBook.getId())
                                .sellingBookId(cartBook.getSellingBook().getSellingBookId())
                                .bookTitle(cartBook.getSellingBook().getBookTitle())
                                .sellingBookPrice(cartBook.getSellingBook().getSellingBookPrice())
                                .imageUrl(url)
                                .sellingBookStock(cartBook.getSellingBook().getSellingBookStock())
                                .build()
                );
            }
            return responses;
        } else {
            for(ReadGuestCartBookResponseDto redisResponse : redisResponselist) {
                responses.add(ReadAllMemberCartBookResponseDto.builder()
                        .quantity(redisResponse.quantity())
                        .cartBookId(redisResponse.cartBookId())
                        .sellingBookId(redisResponse.sellingBookId())
                        .bookTitle(redisResponse.bookTitle())
                        .sellingBookPrice(redisResponse.sellingBookPrice())
                        .imageUrl(redisResponse.imageUrl())
                        .sellingBookStock(redisResponse.sellingBookStock())
                        .build()
                );
            }
            return responses;
        }
    }

//    @Override
//    public ReadMemberCartBookResponseDto readMemberCartBook(Long cartBookId, String email) {
//        Long memberId = memberRepository.getMemberIdByEmail(email);
//
//        Optional<Cart> cartOptional = cartRepository.findByMember_MemberId(memberId);
//
//        Cart cart = cartOptional.orElseGet(() -> new Cart(memberRepository.findByEmail(email)
//                .orElseThrow(() -> new MemberNotFoundException("장바구니에서 해당 이메일로 가입된 회원을 찾을 수 없습니다."))));
////
////        Cart cart = cartRepository.findByMember_MemberId(memberRepository.getMemberIdByEmail(email))
////                .orElseThrow(()-> new CartNotFoundException("회원에 해당하는 장바구니를 찾을 수 없습니다."));
//
//        CartBook cartBook = cartBookRepository.findById(cartBookId)
//                .orElseThrow(()-> new CartBookDoesNotExistException("해당 책 장바구니를 찾을 수 없습니다."));
//
//        return ReadMemberCartBookResponseDto.builder()
//                .cartBookId(cartBook.getId())
//                .quantity(cartBook.getQuantity())
//                .sellingBookId(cartBook.getSellingBook().getSellingBookId())
//                .sellingBookPrice(cartBook.getSellingBook().getSellingBookPrice())
//                .sellingBookStock(cartBook.getSellingBook().getSellingBookStock())
//                .title(cartBook.getSellingBook().getBookTitle())
//                .url(getBookImageUrl(cartBook.getSellingBook().getBook()))
//                .build();
//    }

    @Override
    public Long createBookCartMember(CreateCartBookRequestDto createCartBookRequestDto, String email) {
        Long memberId = memberRepository.getMemberIdByEmail(email);

        Optional<Cart> optionalCart = cartRepository.findByMember_MemberId(memberId);

        Cart cart;

        if(optionalCart.isEmpty()) {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new MemberNotFoundException("장바구니에서 찾는 해당하는 회원이 존재하지 않습니다."));
            cart = new Cart(member);

            cartRepository.save(cart);
        } else {
            cart = optionalCart.get();
        }

        SellingBook sellingBook = sellingBookRepository.findById(createCartBookRequestDto.sellingBookId())
                .orElseThrow(() -> new SellingBookNotFoundException("책장바구니에서 판매책을 찾을 수 없습니다."));

        Optional<CartBook> cartBookOptional = cartBookRepository.findBySellingBook_SellingBookIdAndCart_CartId(createCartBookRequestDto.sellingBookId(), cart.getCartId());

        CartBook cartBook;

        if(cartBookOptional.isPresent()) {
            cartBook = cartBookOptional.get();
            cartBook.setQuantity(cartBook.getQuantity() + createCartBookRequestDto.quantity());
        } else {
            cartBook = new CartBook(
                    createCartBookRequestDto.quantity(),
                    sellingBook,
                    cart
            );
            cartBook = cartBookRepository.save(cartBook);
        }

        cartBookRedisRepository.create("Member" + memberId,
                cartBook.getId(),
                ReadGuestCartBookResponseDto.builder()
                        .cartBookId(cartBook.getId())
                        .sellingBookId(cartBook.getSellingBook().getSellingBookId())
                        .bookTitle(cartBook.getSellingBook().getBookTitle())
                        .sellingBookPrice(cartBook.getSellingBook().getSellingBookPrice())
                        .imageUrl(getBookImageUrl(cartBook.getSellingBook().getBook()))
                        .quantity(cartBook.getQuantity())
                        .sellingBookStock(cartBook.getSellingBook().getSellingBookStock())
                        .sellingBookPackageable(cartBook.getSellingBook().getSellingBookPackageable())
                        .used(cartBook.getSellingBook().getUsed())
                        .build()
        );
        return cartBook.getId();
    }




    @Override
    public Long updateBookCartMember(UpdateCartBookRequestDto updateCartBookRequestDto, String email) {
        Long MemberId = memberRepository.getMemberIdByEmail(email);

        Cart cart = cartRepository.findByMember_MemberId(MemberId)
                .orElseThrow(()-> new CartNotFoundException("회원에 해당하는 장바구니를 찾을 수 없습니다."));

        CartBook cartBook = cartBookRepository.findBySellingBook_SellingBookIdAndCart_CartId(updateCartBookRequestDto.sellingBookId(), cart.getCartId())
                .orElseThrow(() -> new SellingBookNotFoundInBookCartException("장바구니에서 해당 책을 찾을 수 없습니다."));

        cartBook.setQuantity(cartBook.getQuantity() + updateCartBookRequestDto.quantity());

        cartBook.setSellingBook(
                sellingBookRepository.findById(updateCartBookRequestDto.sellingBookId())
                        .orElseThrow(() -> new SellingBookNotFoundException("책장바구니에서 판매책을 찾을 수 없습니다."))
        );

        cartBook.setCart(cart);

        if(cartBook.getQuantity() <= 0) {
            cartBookRepository.deleteByCart(cart);
            cartBookRedisRepository.delete("Member" + MemberId, cartBook.getId());
        } else {
            cartBookRepository.save(cartBook);
            cartBookRedisRepository.update("Member" + MemberId, cartBook.getId(), cartBook.getQuantity());
        }

        return cartBook.getId();
    }

    @Override
    public String deleteBookCartMember(Long bookCartId, String email) {
        Long memberId = memberRepository.getMemberIdByEmail(email);
        cartBookRepository.delete(
                cartBookRepository.findById(bookCartId)
                        //.filter(book -> book.getCart().getMember().getEmail().equals(email))
                        .orElseThrow(() -> new IllegalArgumentException("장바구니에서 해당 상품을 찾을 수 없습니다."))
        );
        cartBookRedisRepository.delete("Member" + memberId, bookCartId);

        return email;
    }


    @Override
    public String deleteAllBookCartMember(String email) {
        Long memberId = memberRepository.getMemberIdByEmail(email);

        Cart cart = cartRepository.findByMember_MemberId(memberId)
                .orElseThrow(() -> new CartNotFoundException("회원에 해당하는 장바구니를 찾을 수 없습니다."));

        cartBookRepository.deleteByCart(cart);
        cartBookRedisRepository.deleteAll("Member" + memberId);
        return email;
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