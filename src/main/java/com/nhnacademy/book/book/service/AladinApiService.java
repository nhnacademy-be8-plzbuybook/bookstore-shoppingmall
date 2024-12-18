package com.nhnacademy.book.book.service;

import com.nhnacademy.book.book.dto.response.aladin.AladinBookListResponse;
import com.nhnacademy.book.book.dto.response.aladin.AladinResponse;
import com.nhnacademy.book.book.entity.*;
import com.nhnacademy.book.book.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class AladinApiService {

    // 알라딘 API URL (기본 ItemList API URL)
    @Value("${aladin.api.url}")
    private String aladinApiUrl;

    // 알라딘 API URL (개별 ISBN 조회용 Lookup API URL)
    @Value("${aladin.api.lookup.url}")
    private String aladinLookupUrl;

    private final RestTemplate restTemplate;
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;
    private final SellingBookRepository sellingBookRepository;
    private final BookImageRepository bookImageRepository;

    // 생성자: 필요한 Repository와 RestTemplate 주입


    public AladinApiService(RestTemplate restTemplate, BookRepository bookRepository,
                            PublisherRepository publisherRepository, CategoryRepository categoryRepository,
                            AuthorRepository authorRepository, SellingBookRepository sellingBookRepository,
                            BookImageRepository bookImageRepository) {
        this.restTemplate = restTemplate;
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
        this.categoryRepository = categoryRepository;
        this.authorRepository = authorRepository;
        this.sellingBookRepository = sellingBookRepository;
        this.bookImageRepository = bookImageRepository;
    }



    /**
     * 알라딘 상품 리스트 API를 사용하여 최신 도서 정보를 가져와 저장합니다.
     */
    @Transactional
    public void saveBooksFromListApi() {
        int itemsPerPage = 10; // 한 페이지당 결과 개수
        int maxPages = 10;     // 최대 페이지 수
        int savedCount = 0;
        int startIndex = 1;    // API 호출 시작 인덱스

        try {
            System.out.println("알라딘 API를 통한 데이터 가져오기 시작.");

            for (int currentPage = 1; currentPage <= maxPages; currentPage++) {
                // API 호출 URL 생성
                String listUrl = String.format("%s&start=%d&MaxResults=%d",
                        aladinApiUrl, startIndex, itemsPerPage);

                System.out.println("API 호출 URL: " + listUrl);

                // API 호출 및 응답 처리
                AladinBookListResponse response = restTemplate.getForObject(listUrl, AladinBookListResponse.class);

                // API 응답 체크
                if (response == null || response.getBooks() == null || response.getBooks().isEmpty()) {
                    System.out.println("더 이상 가져올 데이터가 없습니다. 페이지: " + currentPage);
                    break;
                }

                System.out.println("가져온 책 수: " + response.getBooks().size());

                // 중복 검사 및 데이터 처리
                for (AladinResponse book : response.getBooks()) {
                    if (!bookRepository.existsByBookIsbn13(book.getIsbn13())) {
                        processBookData(book);
                        savedCount++;
                        System.out.println("저장된 책: " + book.getTitle());
                    } else {
                        System.out.println("중복된 책 (ISBN13: " + book.getIsbn13() + ") 스킵됨.");
                    }
                }

                // 다음 페이지로 이동
                startIndex += itemsPerPage;
                System.out.println("페이지 " + currentPage + " 처리 완료.");
            }

        } catch (Exception e) {
            System.err.println("알라딘 API 호출 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("총 저장된 책 수: " + savedCount);
    }

    /**
     * 특정 ISBN 리스트를 기반으로 개별 도서 데이터를 조회하여 저장합니다.
     * @param isbns ISBN 리스트
     */
    @Transactional
    public void saveBooksByIsbns(List<String> isbns) {
        try {
            for (String isbn : isbns) {
                // 알라딘 Lookup API 호출 URL
                String url = String.format("%s&ItemId=%s", aladinLookupUrl, isbn);
                AladinBookListResponse response = restTemplate.getForObject(url, AladinBookListResponse.class);

                // 응답이 유효하고 책 데이터가 존재하면 처리
                if (response != null && response.getBooks() != null) {
                    response.getBooks().forEach(this::processBookData);
                }
            }
        } catch (Exception e) {
            System.err.println("알라딘 ISBN API 호출 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("알라딘 ISBN API 호출 실패", e);
        }
    }

    /**
     * 기본 ItemList API를 호출하여 도서 데이터를 조회하고 저장합니다.
     */
    @Transactional
    public void saveBooksFromDefaultApi() {
        try {
            // 알라딘 기본 ItemList API 호출
            AladinBookListResponse response = restTemplate.getForObject(aladinApiUrl, AladinBookListResponse.class);

            // 응답이 유효하고 책 데이터가 존재하면 처리
            if (response != null && response.getBooks() != null) {
                response.getBooks().forEach(this::processBookData);
            }
        } catch (Exception e) {
            System.err.println("알라딘 기본 API 호출 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("알라딘 기본 API 호출 실패", e);
        }
    }

    /**
     * 개별 책 데이터를 처리하고 중복 확인 후 저장합니다.
     * @param bookData 알라딘 API에서 가져온 개별 책 데이터
     */
    private void processBookData(AladinResponse bookData) {
        try {
            // 중복 확인: ISBN13 기준
            if (!bookRepository.existsByBookIsbn13(bookData.getIsbn13())) {
                Book book = mapToBookEntity(bookData);
                bookRepository.save(book);

                // 이미지 URL 저장
                saveBookImage(book, bookData.getCover());

                // 카테고리 저장
                saveBookCategory(book, bookData.getCategoryName());

                SellingBook sellingBook = createSellingBook(book, bookData);
                sellingBookRepository.save(sellingBook);

                System.out.println("저장된 책: " + book.getBookTitle());
            } else {
                System.out.println("이미 존재하는 책 (ISBN: " + bookData.getIsbn13() + ")");
            }
        } catch (Exception e) {
            System.err.println("책 데이터 처리 중 오류: " + bookData.getIsbn13());
            e.printStackTrace();
        }
    }

    /**
     * 카테고리 경로를 > 기준으로 나눠서 저장
     * @param fullPath 예시: "국내도서>예술/대중문화>음악>악보/작곡>피아노 및 건반악기 악보"
     */
    @Transactional
    public void processCategoryPath(String fullPath) {
        final String[] categoryParts = fullPath.split(">"); // 카테고리 경로를 > 기준으로 나눔
        Category parentCategory = null; // 초기 부모 카테고리 설정

        System.out.println("처리할 경로: " + fullPath);

        for (int depth = 0; depth < categoryParts.length; depth++) {
            final String categoryName = categoryParts[depth].trim(); // 각 카테고리 이름

            System.out.println("현재 카테고리 이름: " + categoryName + ", 깊이: " + (depth + 1));

            // 부모와 이름을 기준으로 카테고리 찾기
            final Category currentParent = parentCategory; // 람다식 내 사용되는 변수는 final 또는 effectively final 이어야 함

            int finalDepth = depth;
            Category category = categoryRepository
                    .findByCategoryNameAndParentCategory(categoryName, currentParent)
                    .orElseGet(() -> {
                        System.out.println("새로운 카테고리 생성: " + categoryName + ", 깊이: " + (finalDepth + 1));
                        // 새 카테고리 생성
                        Category newCategory = new Category();
                        newCategory.setCategoryName(categoryName);
                        newCategory.setCategoryDepth(finalDepth + 1); // depth 설정
                        newCategory.setParentCategory(currentParent); // 부모 카테고리 설정

                        return categoryRepository.save(newCategory); // 저장
                    });

            System.out.println("저장된/찾은 카테고리: " + category.getCategoryName() + ", ID: " + category.getCategoryId());

            parentCategory = category; // 현재 카테고리를 부모로 설정
        }
    }

    /**
     * 이미지 URL을 저장합니다.
     * @param book Book 엔티티
     * @param imageUrl 이미지 URL
     */
    private void saveBookImage(Book book, String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            BookImage bookImage = new BookImage();
            bookImage.setBook(book);
            bookImage.setImageId((long) imageUrl.hashCode()); // 간단한 해시값으로 ID 생성

            bookImageRepository.save(bookImage); // BookImageRepository 사용
            System.out.println("이미지 URL 저장: " + imageUrl);
        }
    }


    /**
     * 카테고리 경로를 저장합니다.
     * @param book Book 엔티티
     * @param categoryPath 카테고리 전체 경로 (예: "국내도서>예술/대중문화>음악>악보/작곡>피아노 및 건반악기 악보")
     */
    private void saveBookCategory(Book book, String categoryPath) {
        if (categoryPath != null && !categoryPath.isEmpty()) {
            // 카테고리 경로를 > 기준으로 나눠서 저장
            processCategoryPath(categoryPath);

            // 최하위 카테고리 (마지막 깊이) 찾기
            String[] categoryParts = categoryPath.split(">");
            String lastCategoryName = categoryParts[categoryParts.length - 1].trim();

            // 최하위 카테고리를 찾거나 새로 생성
            Category category = categoryRepository
                    .findByCategoryNameAndParentCategory(lastCategoryName, null)
                    .orElseGet(() -> {
                        System.out.println("최하위 카테고리 생성: " + lastCategoryName);
                        Category newCategory = new Category();
                        newCategory.setCategoryName(lastCategoryName);
                        newCategory.setCategoryDepth(categoryParts.length);
                        newCategory.setParentCategory(null); // 부모 없음
                        return categoryRepository.save(newCategory);
                    });

            // 도서에 카테고리 연결
            book.addCategory(category);
            System.out.println("도서에 연결된 최하위 카테고리: " + lastCategoryName);
        }
    }



    /**
     * AladinResponse 데이터를 Book 엔티티로 매핑합니다.
     * @param response 알라딘 API에서 가져온 책 데이터
     * @return Book 엔티티 객체
     */
    private Book mapToBookEntity(AladinResponse response) {
        Publisher publisher = findOrCreatePublisher(response.getPublisher());

        Book book = new Book();
        book.setPublisher(publisher);
        book.setBookTitle(response.getTitle());
        book.setBookIndex(response.getDescription());
        book.setBookDescription(response.getDescription());
        book.setBookIsbn13(response.getIsbn13());
        book.setBookPriceStandard(BigDecimal.valueOf(response.getPriceStandard()));

        // 날짜 파싱 오류 처리
        try {
            book.setBookPubDate(LocalDate.parse(response.getPubDate(), DateTimeFormatter.ISO_DATE));
        } catch (Exception e) {
            System.err.println("날짜 파싱 실패: " + response.getPubDate());
            book.setBookPubDate(LocalDate.now()); // 기본값 설정
        }

        // 저자 처리
        if (response.getAuthor() != null) {
            for (String authorName : response.getAuthor().split(",")) {
                Author author = findOrCreateAuthor(authorName.trim());
                book.addAuthor(author);
            }
        }
        return book;
    }

    /**
     * 출판사가 존재하지 않으면 새로 생성하여 반환합니다.
     * @param publisherName 출판사 이름
     * @return Publisher 엔티티 객체
     */
    private Publisher findOrCreatePublisher(String publisherName) {
        return publisherRepository.findByPublisherName(publisherName)
                .orElseGet(() -> {
                    Publisher publisher = new Publisher();
                    publisher.setPublisherName(publisherName);
                    return publisherRepository.save(publisher);
                });
    }

    /**
     * 저자가 존재하지 않으면 새로 생성하여 반환합니다.
     * @param authorName 저자 이름
     * @return Author 엔티티 객체
     */
    private Author findOrCreateAuthor(String authorName) {
        return authorRepository.findByAuthorName(authorName)
                .orElseGet(() -> {
                    Author author = new Author();
                    author.setAuthorName(authorName);
                    return authorRepository.save(author);
                });
    }

    /**
     * 책 데이터를 기반으로 SellingBook 엔티티를 생성합니다.
     * @param book Book 엔티티 객체
     * @param bookData 알라딘 API에서 가져온 책 데이터
     * @return SellingBook 엔티티 객체
     */
    private SellingBook createSellingBook(Book book, AladinResponse bookData) {
        SellingBook sellingBook = new SellingBook();
        sellingBook.setBook(book);
        sellingBook.setSellingBookPrice(BigDecimal.valueOf(bookData.getPriceStandard()));
        sellingBook.setSellingBookPackageable(false);
        sellingBook.setSellingBookStock(100);
        sellingBook.setSellingBookStatus(SellingBook.SellingBookStatus.SELLING);
        sellingBook.setUsed(false);
        sellingBook.setSellingBookViewCount(0L);
        return sellingBook;
    }

    @Transactional
    public void saveBooksByItemIds(List<String> itemIds) {
        itemIds.forEach(itemId -> {
            // 동적 URL 생성
            String url = buildUrl(aladinLookupUrl, "&itemIdType=ItemId", "&ItemId=", itemId);

            // API 호출
            AladinBookListResponse response = callAladinApi(url);

            // 응답 처리
            if (response != null && response.getBooks() != null) {
                response.getBooks().forEach(this::processBookData);
                System.out.println("저장된 ItemId 도서: " + itemId);
            } else {
                System.err.println("ItemId 도서 조회 실패: " + itemId);
            }
        });
    }
    /**
     * 동적으로 URL을 생성하는 유틸리티 메서드.
     * @param baseUrl 기본 URL
     * @param params 추가 파라미터 배열
     * @return 완성된 URL
     */
    private String buildUrl(String baseUrl, String... params) {
        StringBuilder url = new StringBuilder(baseUrl);
        for (String param : params) {
            url.append(param);
        }
        return url.toString();
    }


    /**
     * API를 호출하고 응답을 처리하는 유틸리티 메서드.
     * @param url 호출할 URL
     * @return API 응답 객체
     */
    private AladinBookListResponse callAladinApi(String url) {
        try {
            System.out.println("API 호출 URL: " + url);
            return restTemplate.getForObject(url, AladinBookListResponse.class);
        } catch (Exception e) {
            System.err.println("API 호출 실패: " + url + ", 오류: " + e.getMessage());
            return null; // 실패 시 null 반환
        }
    }

    @Transactional
    public void saveBooksFromListApiDynamic(int startIndex, int maxResults) {
        int savedCount = 0;

        try {
            System.out.println("동적 파라미터를 통한 데이터 가져오기 시작.");

            for (int i = 0; i < 5; i++) { // 예: 최대 5번까지 반복 (파라미터 변경)
                String dynamicUrl = String.format("%s&start=%d&MaxResults=%d", aladinApiUrl, startIndex, maxResults);

                System.out.println("API 호출 URL: " + dynamicUrl);

                AladinBookListResponse response = callAladinApi(dynamicUrl);

                if (response == null || response.getBooks() == null || response.getBooks().isEmpty()) {
                    System.out.println("가져올 데이터가 없습니다. 종료.");
                    break;
                }

                // 중복 검사 후 데이터 저장
                for (AladinResponse book : response.getBooks()) {
                    if (!bookRepository.existsByBookIsbn13(book.getIsbn13())) {
                        processBookData(book);
                        savedCount++;
                    } else {
                        System.out.println("중복된 책 (ISBN13: " + book.getIsbn13() + ") 스킵됨.");
                    }
                }

                // 다음 인덱스 설정
                startIndex += maxResults;
                System.out.println("페이지 처리 완료, 다음 시작 인덱스: " + startIndex);
            }

        } catch (Exception e) {
            System.err.println("API 호출 중 오류 발생: " + e.getMessage());
            throw new RuntimeException(e);
        }

        System.out.println("총 저장된 책 수: " + savedCount);
    }

}
