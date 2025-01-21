package com.nhnacademy.book.cartbook.repository;

import com.nhnacademy.book.book.entity.Book;
import com.nhnacademy.book.book.entity.Publisher;
import com.nhnacademy.book.book.entity.SellingBook;
import com.nhnacademy.book.cartbook.entity.CartBook;
import com.nhnacademy.book.member.domain.Cart;
import com.nhnacademy.book.member.domain.Member;
import com.nhnacademy.book.member.domain.MemberGrade;
import com.nhnacademy.book.member.domain.MemberStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class CartBookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CartBookRepository cartBookRepository;

    private Member member;
    private Cart cart;
    private SellingBook sellingBook1;
    private SellingBook sellingBook2;
    private CartBook cartBook1;
    private CartBook cartBook2;

    @BeforeEach
    void setUp() {
        // MemberGrade 설정
        MemberGrade memberGrade = entityManager.find(MemberGrade.class, 999L);
        if (memberGrade == null) {
            memberGrade = new MemberGrade();
            memberGrade.setMemberGradeName("NORMAL");
            memberGrade.setConditionPrice(BigDecimal.valueOf(100000));
            memberGrade.setGradeChange(LocalDateTime.now());
            entityManager.persist(memberGrade); // 새로운 경우에는 persist 사용
        } else {
            memberGrade.setMemberGradeName("NORMAL");
            memberGrade.setConditionPrice(BigDecimal.valueOf(100000));
            memberGrade.setGradeChange(LocalDateTime.now());
            memberGrade = entityManager.merge(memberGrade); // 이미 존재하는 경우에는 merge 사용
        }

        // MemberStatus 설정
        MemberStatus memberStatus = entityManager.find(MemberStatus.class, 999L);
        if (memberStatus == null) {
            memberStatus = new MemberStatus();
            memberStatus.setMemberStateName("ACTIVE");
            entityManager.persist(memberStatus); // 새로운 경우에는 persist 사용
        } else {
            memberStatus.setMemberStateName("ACTIVE");
            memberStatus = entityManager.merge(memberStatus); // 이미 존재하는 경우에는 merge 사용
        }

        // Member 설정
        member = Member.builder()
                .name("Test User")
                .phone("010-1234-5678")
                .email("test@test.com")
                .birth(LocalDate.of(1990, 1, 1))
                .password("password123")
                .memberGrade(memberGrade)
                .memberStatus(memberStatus)
                .build();
        entityManager.persist(member); // 새로운 Member 객체 저장

        // Cart 설정
        cart = new Cart(member);
        entityManager.persist(cart); // Cart 저장

        // Publisher 설정
        Publisher publisher1 = new Publisher("Publisher 1");
        Publisher publisher2 = new Publisher("Publisher 2");
        entityManager.persist(publisher1);
        entityManager.persist(publisher2);

        // Book 설정
        Book book1 = new Book();
        book1.setBookTitle("Book Title 1");
        book1.setBookIndex("Index 1");
        book1.setBookDescription("Description 1");
        book1.setBookPubDate(LocalDate.now());
        book1.setBookPriceStandard(BigDecimal.valueOf(10000));
        book1.setBookIsbn13("ISBN131");
        book1.setPublisher(publisher1);
        entityManager.persist(book1);

        Book book2 = new Book();
        book2.setBookTitle("Book Title 2");
        book2.setBookIndex("Index 2");
        book2.setBookDescription("Description 2");
        book2.setBookPubDate(LocalDate.now());
        book2.setBookPriceStandard(BigDecimal.valueOf(20000));
        book2.setBookIsbn13("ISBN132");
        book2.setPublisher(publisher2);
        entityManager.persist(book2);

        // SellingBook 설정
        sellingBook1 = new SellingBook();
        sellingBook1.setBook(book1);
        sellingBook1.setSellingBookPrice(BigDecimal.valueOf(10000));
        sellingBook1.setSellingBookPackageable(true);
        sellingBook1.setSellingBookStock(100);
        sellingBook1.setSellingBookStatus(SellingBook.SellingBookStatus.SELLING);
        sellingBook1.setUsed(false);
        sellingBook1.setSellingBookViewCount(0L);
        entityManager.persist(sellingBook1);

        sellingBook2 = new SellingBook();
        sellingBook2.setBook(book2);
        sellingBook2.setSellingBookPrice(BigDecimal.valueOf(20000));
        sellingBook2.setSellingBookPackageable(true);
        sellingBook2.setSellingBookStock(50);
        sellingBook2.setSellingBookStatus(SellingBook.SellingBookStatus.SELLING);
        sellingBook2.setUsed(false);
        sellingBook2.setSellingBookViewCount(0L);
        entityManager.persist(sellingBook2);

        // CartBook 설정
        cartBook1 = new CartBook(2, sellingBook1, cart);
        cartBook2 = new CartBook(1, sellingBook2, cart);
        entityManager.persist(cartBook1);
        entityManager.persist(cartBook2);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("장바구니로 장바구니도서 목록 조회 테스트")
    void findAllByCart() {
        // when
        List<CartBook> cartBooks = cartBookRepository.findAllByCart(cart);

        // then
        assertThat(cartBooks).hasSize(2);
        assertThat(cartBooks).extracting(CartBook::getSellingBook)
                .extracting(SellingBook::getSellingBookId)
                .containsExactlyInAnyOrder(sellingBook1.getSellingBookId(), sellingBook2.getSellingBookId());
    }

    @Test
    @DisplayName("장바구니로 장바구니도서 삭제 테스트")
    void deleteByCart() {
        // when
        cartBookRepository.deleteByCart(cart);
        entityManager.flush();
        entityManager.clear();

        // then
        List<CartBook> cartBooks = cartBookRepository.findAllByCart(cart);
        assertThat(cartBooks).isEmpty();
    }

    @Test
    @DisplayName("판매책 ID와 장바구니 ID로 장바구니도서 조회 테스트")
    void findBySellingBook_SellingBookIdAndCart_CartId_Success() {
        Optional<CartBook> foundCartBook = cartBookRepository.findBySellingBook_SellingBookIdAndCart_CartId(sellingBook1.getSellingBookId(), cart.getCartId());

        assertThat(foundCartBook).isPresent();
        assertThat(foundCartBook.get().getSellingBook().getSellingBookId()).isEqualTo(sellingBook1.getSellingBookId());
        assertThat(foundCartBook.get().getCart().getCartId()).isEqualTo(cart.getCartId());
    }

    @Test
    @DisplayName("판매책 ID와 장바구니 ID로 장바구니도서 조회 실패 테스트")
    void findBySellingBook_SellingBookIdAndCart_CartId_Fail() {
        Optional<CartBook> foundCartBook = cartBookRepository.findBySellingBook_SellingBookIdAndCart_CartId(999L, cart.getCartId());

        assertThat(foundCartBook).isEmpty();
    }
}