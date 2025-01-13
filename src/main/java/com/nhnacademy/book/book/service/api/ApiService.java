package com.nhnacademy.book.book.service.api;

import com.nhnacademy.book.book.dto.response.aladin.AladinBookListResponse;
import com.nhnacademy.book.book.dto.response.aladin.AladinResponse;
import com.nhnacademy.book.book.service.mapping.MappingService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
@Service
@Slf4j
public class ApiService {

    @Value("${aladin.api.url}")
    private String aladinApiUrl;

    private final RestTemplate restTemplate;
    private final MappingService mappingService;

    public ApiService(RestTemplate restTemplate, MappingService mappingService) {
        this.restTemplate = restTemplate;
        this.mappingService = mappingService;
    }
    /**
     * 알라딘 상품 리스트 API를 사용하여 최신 도서 정보를 가져와 저장합니다.
     */
    public void saveBooksFromListApi(String queryType, String searchTarget, int start, int maxResults) {
        int currentPage = 1;
        int totalBooksFetched = 0;
        int totalBooksSaved = 0;

        try {
            while (currentPage <= 5) { // 최대 페이지 제한
                // URL에 동적 파라미터 추가
                String listUrl = String.format("%s&QueryType=%s&SearchTarget=%s&start=%d&MaxResults=%d",
                        aladinApiUrl, queryType, searchTarget, start, maxResults);

                log.debug("API 호출 URL: {}", listUrl);

                AladinBookListResponse response = callAladinApi(listUrl);

                if (response == null || response.getBooks() == null || response.getBooks().isEmpty()) {
                    log.debug("더 이상 가져올 데이터가 없습니다. 현재 페이지: {}", currentPage);
                    break;
                }

                int booksFetched = response.getBooks().size();
                totalBooksFetched += booksFetched;

                for (AladinResponse bookData : response.getBooks()) {
                    boolean isSaved = mappingService.processBookData(bookData);
                    if (isSaved) {
                        totalBooksSaved++;
                        log.debug("저장된 책: {}", bookData.getTitle());
                    } else {
                        log.debug("중복된 책 스킵: {}", bookData.getIsbn13());
                    }
                }

                start += maxResults;
                currentPage++;
            }
        } catch (Exception e) {
            log.error("API 호출 중 오류 발생: {}", e.getMessage(), e);
        }

        log.debug("총 가져온 책 수: {}", totalBooksFetched);
        log.debug("총 저장된 책 수: {}", totalBooksSaved);
    }


    /**
     * 특정 ISBN 리스트를 기반으로 개별 도서 데이터를 조회하여 저장합니다.
     *
     * @param isbns ISBN 리스트
     */
    @Transactional
    public boolean saveBooksByIsbns(List<String> isbns) {
        boolean isSaved = false;

        // API 기본 URL (검색용)
        String searchUrlTemplate = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey=ttbfkqlaus1419001&Query=%s&QueryType=ISBN&MaxResults=1&SearchTarget=Book&output=js&Version=20131101";

        try {
            for (String isbn : isbns) {
                // 동적 URL 생성
                String url = String.format(searchUrlTemplate, isbn);

                // API 호출 및 응답 처리
                AladinBookListResponse response = restTemplate.getForObject(url, AladinBookListResponse.class);

                if (response != null && response.getBooks() != null && !response.getBooks().isEmpty()) {
                    // 각 책 데이터를 처리
                    for (AladinResponse book : response.getBooks()) {
                        mappingService.processBookData(book);
                        isSaved = true; // 저장된 경우 true로 설정
                    }
                } else {
                    log.warn("ISBN으로 검색된 데이터가 없습니다: {}", isbn);
                }
            }
        } catch (Exception e) {
            log.error("알라딘 ISBN API 호출 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("알라딘 ISBN API 호출 실패", e);
        }

        return isSaved;
    }

    /**
     * ItemId를 기반으로 도서 정보를 저장합니다.
     *
     * @param itemIds ItemId 리스트
     * @return 저장 실패한 ItemId 리스트
     */
    @Transactional
    public List<String> saveBooksByItemIds(List<String> itemIds) {
        List<String> failedItemIds = new ArrayList<>();
        String itemIdUrlTemplate = aladinApiUrl + "&SearchTarget=Book&QueryType=ItemNewAll&itemIdType=ItemId&ItemId=%s";

        for (String itemId : itemIds) {
            try {
                String url = String.format(itemIdUrlTemplate, itemId);
                AladinBookListResponse response = callAladinApi(url);

                if (response != null && response.getBooks() != null && !response.getBooks().isEmpty()) {
                    for (AladinResponse book : response.getBooks()) {
                        boolean isSaved = mappingService.processBookData(book);
                        if (isSaved) {
                            log.debug("저장된 ItemId 도서: {}", book.getTitle());
                        } else {
                            log.debug("중복된 책 스킵: {}", book.getIsbn13());
                        }
                    }
                } else {
                    log.warn("ItemId로 검색된 데이터가 없습니다: {}", itemId);
                    failedItemIds.add(itemId); // 검색 실패한 ItemId 추가
                }
            } catch (Exception e) {
                log.error("ItemId API 호출 실패: {}", itemId, e);
                failedItemIds.add(itemId); // API 호출 실패한 ItemId 추가
            }
        }
        return failedItemIds; // 실패한 ItemId 리스트 반환
    }


    /**
     * API를 호출하고 응답을 반환합니다.
     */
    private AladinBookListResponse callAladinApi(String url) {
        try {
            log.debug("API 호출 URL: {}", url);
            return restTemplate.getForObject(url, AladinBookListResponse.class);
        } catch (Exception e) {
            log.error("API 호출 실패: {}", e.getMessage(), e);
            return null;
        }
    }


    @Transactional
    public List<String> saveBooksByIsbnsDetailed(List<String> isbns) {
        List<String> failedIsbns = new ArrayList<>();

        // API 기본 URL (검색용)
        String searchUrlTemplate = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey=ttbfkqlaus1419001&Query=%s&QueryType=ISBN&MaxResults=1&SearchTarget=Book&output=js&Version=20131101";

        try {
            for (String isbn : isbns) {
                // 동적 URL 생성
                String url = String.format(searchUrlTemplate, isbn);

                // API 호출 및 응답 처리
                AladinBookListResponse response = restTemplate.getForObject(url, AladinBookListResponse.class);

                if (response != null && response.getBooks() != null && !response.getBooks().isEmpty()) {
                    try {
                        // 각 책 데이터를 처리
                        for (AladinResponse book : response.getBooks()) {
                            mappingService.processBookData(book);
                        }
                    } catch (Exception e) {
                        log.error("ISBN 데이터 처리 중 오류 발생: {}", isbn, e);
                        failedIsbns.add(isbn); // 실패한 ISBN 추가
                    }
                } else {
                    log.warn("ISBN으로 검색된 데이터가 없습니다: {}", isbn);
                    failedIsbns.add(isbn); // 검색 실패 ISBN 추가
                }
            }
        } catch (Exception e) {
            log.error("알라딘 ISBN API 호출 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("알라딘 ISBN API 호출 실패", e);
        }

        return failedIsbns;
    }


}