package com.nhnacademy.book.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.book.book.exception.*;
import com.nhnacademy.book.cartbook.exception.BookStatusNotSellingBookException;
import com.nhnacademy.book.cartbook.exception.CartBookDoesNotExistException;
import com.nhnacademy.book.cartbook.exception.SellingBookNotFoundInBookCartException;
import com.nhnacademy.book.deliveryFeePolicy.exception.StockNotEnoughException;
import com.nhnacademy.book.member.domain.exception.*;
import com.nhnacademy.book.order.exception.NonMemberPasswordNotMatchException;
import com.nhnacademy.book.order.exception.OrderRequestFailException;
import com.nhnacademy.book.order.exception.PriceMismatchException;
import com.nhnacademy.book.review.exception.*;
import com.nhnacademy.book.skm.exception.KeyMangerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.net.BindException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @RestController
    static class TestController {
        @GetMapping("/test/duplicate-email")
        public void throwDuplicateEmailException() {
            throw new DuplicateEmailException("중복된 이메일입니다.");
        }

        @GetMapping("/test/member-grade-not-found")
        public void throwMemberGradeNotFoundException() {
            throw new MemberGradeNotFoundException("회원 등급을 찾을 수 없습니다.");
        }
        @GetMapping("/test/member-status-not-found")
        public void throwMemberStatusNotFoundException() {
            throw new MemberStatusNotFoundException("회원 상태를 찾을 수 없습니다.");
        }

        @GetMapping("/test/default-member-grade-not-found")
        public void throwDefaultMemberGradeNotFoundException() {
            throw new DefaultMemberGradeNotFoundException("기본 등급을 찾을 수 없습니다.");
        }

        @GetMapping("/test/default-status-grade-not-found")
        public void throwDefaultStatusGradeNotFoundException() {
            throw new DefaultStatusGradeNotfoundException("기본 상태를 찾을 수 없습니다.");
        }
        @GetMapping("/test/member-email-not-found")
        public void throwMemberEmailNotFoundException() {
            throw new MemberEmailNotFoundException("이메일로 회원을 찾을 수 없습니다.");
        }
        @GetMapping("/test/member-id-not-found")
        public void throwMemberIdNotFoundException() {
            throw new MemberIdNotFoundException("ID로 회원을 찾을 수 없습니다.");
        }

        @GetMapping("/test/duplicate-member-state")
        public void throwDuplicateMemberStateException() {
            throw new DuplicateMemberStateException("회원 상태가 중복되었습니다.");
        }

        @GetMapping("/test/duplicate-member-grade")
        public void throwDuplicateMemberGradeException() {
            throw new DuplicateMemberGradeException("회원 등급이 중복되었습니다.");
        }
        @GetMapping("/test/duplicate-member-modification")
        public void throwDuplicateMemberModificationException() {
            throw new DuplicateMemberModificationException("회원 수정 시 기존 값과 같습니다.");
        }

        @GetMapping("/test/member-not-found")
        public void throwMemberNotFoundException() {
            throw new MemberNotFoundException("등록된 회원이 없습니다.");
        }

        @GetMapping("/test/address-limit-exceeded")
        public void throwAddressLimitExceededException() {
            throw new AddressLimitExceededException("배송지는 최대 10개까지만 등록 가능합니다.");
        }

        @GetMapping("/test/auth-not-found")
        public void throwAuthNotFoundException() {
            throw new AuthNotFoundException("권한이 없습니다.");
        }

        @GetMapping("/test/duplicate-address")
        public void throwDuplicateAddressException() {
            throw new DuplicateAddressException("이미 등록된 배송지입니다.");
        }

        @GetMapping("/test/stock-not-enough")
        public void throwStockNotEnoughException() {
            throw new StockNotEnoughException("재고가 부족합니다.");
        }

        @GetMapping("/test/price-mismatch")
        public void throwPriceMismatchException() {
            throw new PriceMismatchException("가격이 일치하지 않습니다.");
        }

        @PostMapping("/test/missing-part")
        public void throwMissingPartException() throws MissingServletRequestPartException {
            throw new MissingServletRequestPartException("file");
        }

        @GetMapping("/test/key-manager")
        public void throwKeyManagerException() {
            throw new KeyMangerException("키 관리 중 오류가 발생했습니다.");
        }

        @GetMapping("/test/point-condition-not-found")
        public void throwPointConditionNotFoundException() {
            throw new PointConditionNotFoundException("포인트 조건을 찾을 수 없습니다.");
        }

        @GetMapping("/test/order-product-not-found")
        public void throwOrderProductNotFoundException() {
            throw new OrderProductNotFoundException("OrderProduct를 찾을 수 없습니다.");
        }

        @GetMapping("/test/certification-not-found")
        public void throwCertificationNotFoundException() {
            throw new CertificationNotFoundException("회원 인증 정보를 찾을 수 없습니다.");
        }

        @PostMapping("/test/invalid-certification-method")
        public void throwInvalidCertificationMethodException() {
            throw new InvalidCertificationMethodException("허용되지 않은 인증 방법입니다.");
        }

        @PostMapping("/test/duplicate-certification")
        public void throwDuplicateCertificationException() {
            throw new DuplicateCertificationException("중복된 인증 방법입니다.");
        }
        @GetMapping("/test/author-id-not-found")
        public void throwAuthorIdNotFoundException() {
            throw new AuthorIdNotFoundException("Author ID를 찾을 수 없습니다.");
        }

        @GetMapping("/test/author-name-not-found")
        public void throwAuthorNameNotFoundException() {
            throw new AuthorNameNotFoundException("Author 이름을 찾을 수 없습니다.");
        }
        @GetMapping("/test/authors-not-found")
        public void throwAuthorsNotFoundException() {
            throw new AuthorsNotFoundException("Authors를 찾을 수 없습니다.");

        }
        @GetMapping("/test/book-author-not-found")
        public void throwBookAuthorNotFoundException() {
            throw new BookAuthorNotFoundException("책의 저자를 찾을 수 없습니다.");

        }

        @GetMapping("/test/book-not-found")
        public void throwBookNotFoundException() {
            throw new BookNotFoundException("Book을 찾을 수 없습니다.");
        }

        @GetMapping("/test/book-already-exists")
        public void throwBookAlreadyExistsException() {
            throw new BookAlreadyExistsException("ISBN이 중복되었습니다.");
        }

        @GetMapping("/test/category-already-exists")
        public void throwCategoryAlreadyExistsException() {
            throw new CategoryAlreadyExistsException("카테고리가 이미 존재합니다.");
        }

        @GetMapping("/test/default-auth-not-found")
        public void throwDefaultAuthNotfoundException() {
            throw new DefaultAuthNotfoundException("회원 가입에 필요한 권한이 없습니다.");
        }

        @GetMapping("/test/publisher-not-found")
        public void throwPublisherNotFoundException() {
            throw new PublisherNotFoundException("출판사를 찾을 수 없습니다.");
        }
        @GetMapping("/test/selling-book-not-found")
        public void throwSellingBookNotFoundException() {
            throw new SellingBookNotFoundException("판매 중인 책을 찾을 수 없습니다.");
        }

        @GetMapping("/test/duplicate-phone")
        public void throwDuplicatePhoneException() {
            throw new DuplicatePhoneException("전화번호가 중복되었습니다.");
        }
        @GetMapping("/test/duplicate-review")
        public void throwDuplicateReviewException() {
            throw new DuplicateReviewException("중복된 리뷰입니다.");
        }

        @GetMapping("/test/invalid-order-product-status")
        public void throwInvalidOrderProductStatusException() {
            throw new InvalidOrderProductStatusException("리뷰를 작성할 수 없는 주문 상태입니다.");
        }
        @GetMapping("/test/invalid-order-access")
        public void throwInvalidOrderAccessException() {
            throw new InvalidOrderAccessException("해당 주문은 회원의 주문이 아닙니다.");
        }

        @GetMapping("/test/non-member-password-not-match")
        public void throwNonMemberPasswordNotMatchException() {
            throw new NonMemberPasswordNotMatchException("비회원 주문 비밀번호가 일치하지 않습니다.");
        }

        @GetMapping("/test/review-not-found")
        public void throwReviewNotFoundException() {
            throw new ReviewNotFoundException("해당 리뷰를 찾을 수 없습니다.");
        }
        @GetMapping("/test/book-status-not-selling")
        public void throwBookStatusNotSellingBookException() {
            throw new BookStatusNotSellingBookException("책 상태가 판매 중이 아닙니다.");
        }

        @GetMapping("/test/cart-book-does-not-exist")
        public void throwCartBookDoesNotExistException() {
            throw new CartBookDoesNotExistException("장바구니에 책이 존재하지 않습니다.");
        }

        @GetMapping("/test/selling-book-not-found-in-cart")
        public void throwSellingBookNotFoundInBookCartException() {
            throw new SellingBookNotFoundInBookCartException("장바구니에서 판매 중인 책을 찾을 수 없습니다.");
        }

        @GetMapping("/test/order-request-fail")
        public void throwOrderRequestFailException() {
            throw new OrderRequestFailException("주문 요청에 실패했습니다.");
        }

    }

    @Test
    @DisplayName("중복 이메일 예외 처리 테스트")
    void handleDuplicateEmailException() throws Exception {
        mockMvc.perform(get("/test/duplicate-email"))
                .andExpect(status().isConflict()) // HTTP 409 상태 확인
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("중복된 이메일입니다."));

    }

    @Test
    @DisplayName("회원 등급을 찾을 수 없는 예외 처리 테스트")
    void handleMemberGradeNotFoundException() throws Exception {
        mockMvc.perform(get("/test/member-grade-not-found"))
                .andExpect(status().isNotFound()) // HTTP 404 상태 확인
                .andExpect(jsonPath("$.status").value(404)) // JSON 응답 필드 검증
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("회원 등급을 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("회원 상태를 찾을 수 없는 예외 처리 테스트")
    void handleMemberStatusNotFoundException() throws Exception {
        mockMvc.perform(get("/test/member-status-not-found"))
                .andExpect(status().isNotFound()) // HTTP 404 상태 확인
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("회원 상태를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("기본 등급을 찾을 수 없는 예외 처리 테스트")
    void handleDefaultMemberGradeNotFoundException() throws Exception {
        mockMvc.perform(get("/test/default-member-grade-not-found"))
                .andExpect(status().isNotFound()) // HTTP 404 상태 확인
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("기본 등급을 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("기본 상태를 찾을 수 없는 예외 처리 테스트")
    void handleDefaultStatusGradeNotFoundException() throws Exception {
        mockMvc.perform(get("/test/default-status-grade-not-found"))
                .andExpect(status().isNotFound()) // HTTP 404 상태 확인
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("기본 상태를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("이메일로 특정 회원을 조회할 때 찾을 수 없는 예외 처리 테스트")
    void handleMemberEmailNotFoundException() throws Exception {
        mockMvc.perform(get("/test/member-email-not-found"))
                .andExpect(status().isNotFound()) // HTTP 404 상태 확인
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("이메일로 회원을 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("ID로 특정 회원을 조회할 때 찾을 수 없는 예외 처리 테스트")
    void handleMemberIdNotFoundException() throws Exception {
        mockMvc.perform(get("/test/member-id-not-found"))
                .andExpect(status().isNotFound()) // HTTP 404 상태 확인
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("ID로 회원을 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("회원 상태를 추가할 때 중복 예외 처리 테스트")
    void handleDuplicateMemberStateException() throws Exception {
        mockMvc.perform(get("/test/duplicate-member-state"))
                .andExpect(status().isConflict()) // HTTP 409 상태 확인
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("회원 상태가 중복되었습니다."));
    }

    @Test
    @DisplayName("회원 등급을 추가할 때 중복 예외 처리 테스트")
    void handleDuplicateMemberGradeException() throws Exception {
        mockMvc.perform(get("/test/duplicate-member-grade"))
                .andExpect(status().isConflict()) // HTTP 409 상태 확인
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("회원 등급이 중복되었습니다."));
    }

    @Test
    @DisplayName("회원 수정 시 기존 값과 같을 경우 예외 처리 테스트")
    void handleDuplicateMemberModificationException() throws Exception {
        mockMvc.perform(get("/test/duplicate-member-modification"))
                .andExpect(status().isConflict()) // HTTP 409 상태 확인
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("회원 수정 시 기존 값과 같습니다."));
    }

    @Test
    @DisplayName("전체 회원 조회 시 등록된 회원이 없는 경우 예외 처리 테스트")
    void handleMemberNotFoundException() throws Exception {
        mockMvc.perform(get("/test/member-not-found"))
                .andExpect(status().isNotFound()) // HTTP 404 상태 확인
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("등록된 회원이 없습니다."));
    }

    @Test
    @DisplayName("배송지 등록 시 10개 초과 오류 예외 처리 테스트")
    void handleAddressLimitExceededException() throws Exception {
        mockMvc.perform(get("/test/address-limit-exceeded"))
                .andExpect(status().isBadRequest()) // HTTP 400 상태 확인
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("배송지는 최대 10개까지만 등록 가능합니다."));
    }

    @Test
    @DisplayName("권한이 없는 경우 예외 처리 테스트")
    void handleAuthNotFoundException() throws Exception {
        mockMvc.perform(get("/test/auth-not-found"))
                .andExpect(status().isNotFound()) // HTTP 404 상태 확인
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("권한이 없습니다."));
    }

    @Test
    @DisplayName("같은 배송지를 등록하는 경우 예외 처리 테스트")
    void handleDuplicateAddressException() throws Exception {
        mockMvc.perform(get("/test/duplicate-address"))
                .andExpect(status().isConflict()) // HTTP 409 상태 확인
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("이미 등록된 배송지입니다."));
    }

    @Test
    @DisplayName("재고 부족 예외 처리 테스트")
    void handleStockNotEnoughException() throws Exception {
        mockMvc.perform(get("/test/stock-not-enough"))
                .andExpect(status().isConflict()) // HTTP 409 상태 확인
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("재고가 부족합니다."));
    }

    @Test
    @DisplayName("가격이 일치하지 않을 때 예외 처리 테스트")
    void handlePriceMismatchException() throws Exception {
        mockMvc.perform(get("/test/price-mismatch"))
                .andExpect(status().isConflict()) // HTTP 409 상태 확인
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("가격이 일치하지 않습니다."));
    }


    @Test
    @DisplayName("@RequestPart validation check 예외 처리 테스트")
    void handleMissingPartException() throws Exception {
        mockMvc.perform(post("/test/missing-part"))
                .andExpect(status().isBadRequest()) // HTTP 400 상태 확인
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Required part 'file' is not present."));
    }

    @Test
    @DisplayName("키 관리 중 오류 예외 처리 테스트")
    void handleKeyManagerException() throws Exception {
        mockMvc.perform(get("/test/key-manager"))
                .andExpect(status().isInternalServerError()) // HTTP 500 상태 확인
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("키 관리 중 오류가 발생했습니다."));
    }

    @Test
    @DisplayName("포인트 조건을 찾을 수 없을 때 예외 처리 테스트")
    void handlePointConditionNotFoundException() throws Exception {
        mockMvc.perform(get("/test/point-condition-not-found"))
                .andExpect(status().isNotFound()) // HTTP 404 상태 확인
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("포인트 조건을 찾을 수 없습니다."));
    }
    @Test
    @DisplayName("OrderProductId로 OrderProduct를 조회할 때 예외 처리 테스트")
    void handleOrderProductNotFoundException() throws Exception {
        mockMvc.perform(get("/test/order-product-not-found"))
                .andExpect(status().isNotFound()) // HTTP 404 상태 확인
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("OrderProduct를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("회원의 인증 정보가 없을 때 예외 처리 테스트")
    void handleCertificationNotFoundException() throws Exception {
        mockMvc.perform(get("/test/certification-not-found"))
                .andExpect(status().isNotFound()) // HTTP 404 상태 확인
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("회원 인증 정보를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("허용되지 않은 인증 방법으로 변경하려 할 때 예외 처리 테스트")
    void handleInvalidCertificationMethodException() throws Exception {
        mockMvc.perform(post("/test/invalid-certification-method"))
                .andExpect(status().isBadRequest()) // HTTP 400 상태 확인
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("허용되지 않은 인증 방법입니다."));
    }

    @Test
    @DisplayName("중복된 인증 방법을 추가하려 할 때 예외 처리 테스트")
    void handleDuplicateCertificationException() throws Exception {
        mockMvc.perform(post("/test/duplicate-certification"))
                .andExpect(status().isConflict()) // HTTP 409 상태 확인
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("중복된 인증 방법입니다."));
    }

    @Test
    @DisplayName("AuthorId로 Author를 조회할 때 예외 처리 테스트")
    void handleAuthorIdNotFoundException() throws Exception {
        mockMvc.perform(get("/test/author-id-not-found"))
                .andExpect(status().isNotFound()) // HTTP 404 상태 확인
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Author ID를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("AuthorName으로 Author를 조회할 때 예외 처리 테스트")
    void handleAuthorNameNotFoundException() throws Exception {
        mockMvc.perform(get("/test/author-name-not-found"))
                .andExpect(status().isNotFound()) // HTTP 404 상태 확인
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Author 이름을 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("Authors를 조회할 때 예외 처리 테스트")
    void handleAuthorsNotFoundException() throws Exception {
        mockMvc.perform(get("/test/authors-not-found"))
                .andExpect(status().isNotFound()) // HTTP 404 상태 확인
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Authors를 찾을 수 없습니다."));
    }
    @Test
    @DisplayName("BookAuthorNotFoundException 예외 처리 테스트")
    void handleBookAuthorNotFoundException() throws Exception {
        mockMvc.perform(get("/test/book-author-not-found"))
                .andExpect(status().isNotFound()) // HTTP 404 상태 확인
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("책의 저자를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("Book을 찾을 수 없을 때 예외 처리 테스트")
    void handleBookNotFoundException() throws Exception {
        mockMvc.perform(get("/test/book-not-found"))
                .andExpect(status().isNotFound()) // HTTP 404 상태 확인
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Book을 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("ISBN 중복 예외 처리 테스트")
    void handleBookAlreadyExistsException() throws Exception {
        mockMvc.perform(get("/test/book-already-exists"))
                .andExpect(status().isConflict()) // HTTP 409 상태 확인
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("ISBN이 중복되었습니다."));
    }

    @Test
    @DisplayName("카테고리가 이미 존재할 때 예외 처리 테스트")
    void handleCategoryAlreadyExistsException() throws Exception {
        mockMvc.perform(get("/test/category-already-exists"))
                .andExpect(status().isConflict()) // HTTP 409 상태 확인
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("카테고리가 이미 존재합니다."));
    }

    @Test
    @DisplayName("회원 가입에 필요한 권한이 없을 때 예외 처리 테스트")
    void handleDefaultAuthNotfoundException() throws Exception {
        mockMvc.perform(get("/test/default-auth-not-found"))
                .andExpect(status().isNotFound()) // HTTP 404 상태 확인
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("회원 가입에 필요한 권한이 없습니다."));
    }

    @Test
    @DisplayName("출판사를 찾을 수 없을 때 예외 처리 테스트")
    void handlePublisherNotFoundException() throws Exception {
        mockMvc.perform(get("/test/publisher-not-found"))
                .andExpect(status().isNotFound()) // HTTP 404 상태 확인
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("출판사를 찾을 수 없습니다."));
    }
    @Test
    @DisplayName("판매 중인 책을 찾을 수 없을 때 예외 처리 테스트")
    void handleSellingBookNotFoundException() throws Exception {
        mockMvc.perform(get("/test/selling-book-not-found"))
                .andExpect(status().isNotFound()) // HTTP 404 상태 확인
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("판매 중인 책을 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("전화번호가 중복일 때 예외 처리 테스트")
    void handleDuplicatePhoneException() throws Exception {
        mockMvc.perform(get("/test/duplicate-phone"))
                .andExpect(status().isConflict()) // HTTP 409 상태 확인
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("전화번호가 중복되었습니다."));
    }

    @Test
    @DisplayName("중복된 리뷰일 때  예외 처리 테스트")
    void handleDuplicateReviewException() throws Exception {
        mockMvc.perform(get("/test/duplicate-review"))
                .andExpect(status().isConflict()) // HTTP 409 상태 확인
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("중복된 리뷰입니다."));
    }

    @Test
    @DisplayName("리뷰를 작성할 수 없는 주문 상태일 때 예외 처리 테스트")
    void handleInvalidOrderProductStatusException() throws Exception {
        mockMvc.perform(get("/test/invalid-order-product-status"))
                .andExpect(status().isBadRequest()) // HTTP 400 상태 확인
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("리뷰를 작성할 수 없는 주문 상태입니다."));
    }
    @Test
    @DisplayName("해당 주문이 회원의 주문이 아닐 때 예외 처리 테스트")
    void handleInvalidOrderAccessException() throws Exception {
        mockMvc.perform(get("/test/invalid-order-access"))
                .andExpect(status().isForbidden()) // HTTP 403 상태 확인
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.error").value("Forbidden"))
                .andExpect(jsonPath("$.message").value("해당 주문은 회원의 주문이 아닙니다."));
    }

    @Test
    @DisplayName("비회원 주문 비밀번호가 일치하지 않을 때 예외 처리 테스트")
    void handleNonMemberPasswordNotMatchException() throws Exception {
        mockMvc.perform(get("/test/non-member-password-not-match"))
                .andExpect(status().isForbidden()) // HTTP 403 상태 확인
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.error").value("Forbidden"))
                .andExpect(jsonPath("$.message").value("비회원 주문 비밀번호가 일치하지 않습니다."));
    }

    @Test
    @DisplayName("해당 리뷰를 찾을 수 없을 떄 예외 처리 테스트")
    void handleReviewNotFoundException() throws Exception {
        mockMvc.perform(get("/test/review-not-found"))
                .andExpect(status().isNotFound()) // HTTP 404 상태 확인
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("해당 리뷰를 찾을 수 없습니다."));
    }
    @Test
    @DisplayName("책 상태가 판매 중이 아닐 떄 예외 처리 테스트")
    void handleBookStatusNotSellingBookException() throws Exception {
        mockMvc.perform(get("/test/book-status-not-selling"))
                .andExpect(status().isBadRequest()) // HTTP 400 상태 확인
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("책 상태가 판매 중이 아닙니다."));
    }

    @Test
    @DisplayName("장바구니에 책이 존재하지 않을 때 예외 처리 테스트")
    void handleCartBookDoesNotExistException() throws Exception {
        mockMvc.perform(get("/test/cart-book-does-not-exist"))
                .andExpect(status().isNotFound()) // HTTP 404 상태 확인
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("장바구니에 책이 존재하지 않습니다."));
    }

    @Test
    @DisplayName("장바구니에서 판매 중인 책을 찾을 수 없을 때 예외 처리 테스트")
    void handleSellingBookNotFoundInBookCartException() throws Exception {
        mockMvc.perform(get("/test/selling-book-not-found-in-cart"))
                .andExpect(status().isNotFound()) // HTTP 404 상태 확인
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("장바구니에서 판매 중인 책을 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("주문 요청에 실패했을 때 예외 처리 테스트")
    void handleOrderRequestFailException() throws Exception {
        mockMvc.perform(get("/test/order-request-fail"))
                .andExpect(status().isNotFound()) // HTTP 404 상태 확인
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("주문 요청에 실패했습니다."));
    }

}
