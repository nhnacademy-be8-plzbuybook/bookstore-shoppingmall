package com.nhnacademy.book.book.service.api;

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

    @Value("${aladin.api.url2}")
    private String aladinApiUrl2;

    private final RestTemplate restTemplate;
    private final MappingService mappingService;

    public ApiService(RestTemplate restTemplate, MappingService mappingService) {
        this.restTemplate = restTemplate;
        this.mappingService = mappingService;
    }
    /**
     * 알라딘 상품 리스트 API를 사용하여 최신 도서 정보를 가져와 저장합니다. 파라미터로 한느거 씀 대량 저장
     */
    public void saveBooksFromListApi(String queryType, String searchTarget, int start, int maxResults) {
        int currentPage = 1;
        int totalBooksFetched = 0;
//        int totalBooksSaved = 0;

        try {
            while (currentPage <= 5) { // 최대 페이지 제한
                // URL에 동적 파라미터 추가
                String listUrl = String.format("%s&QueryType=%s&SearchTarget=%s&start=%d&MaxResults=%d",
                        aladinApiUrl, queryType, searchTarget, start, maxResults);

                log.info("API 호출 URL: {}", listUrl);

                AladinResponse response = callAladinApi(listUrl);

                if (response == null || response.getBooks() == null || response.getBooks().isEmpty()) {
                    log.info("더 이상 가져올 데이터가 없습니다. 현재 페이지: {}", currentPage);
                    break;
                }

                int booksFetched = response.getBooks().size();
                totalBooksFetched += booksFetched;

                for (AladinResponse bookData : response.getBooks()) {
                    boolean isSaved = mappingService.processBookData(bookData);
                    if (isSaved) {
//                        totalBooksSaved++;
                        log.info("저장된 책: {}", bookData.getTitle());
                    } else {
                        log.info("중복된 책 스킵: {}", bookData.getIsbn13());
                    }
                }

                start += maxResults;
                currentPage++;
            }
        } catch (Exception e) {
            log.error("API 호출 중 오류 발생: {}", e.getMessage(), e);
        }

        log.info("총 가져온 책 수: {}", totalBooksFetched);
//        log.info("총 저장된 책 수: {}", totalBooksSaved);
    }

    /**
     * ItemId를 기반으로 도서 정보를 저장합니다. --이거 써 isbn 으로 하는거
     *
     * @param itemIds ItemId 리스트
     * @return 저장 실패한 ItemId 리스트
     * 이거 고쳐야해
     */
    @Transactional
    public List<String> saveBooksByItemIds(List<String> itemIds) {
        List<String> failedItemIds = new ArrayList<>();
        String itemIdUrlTemplate = aladinApiUrl2 + "&itemIdType=ISBN&ItemId=%s";
        for (String itemId : itemIds) {
            try {
                String url = String.format(itemIdUrlTemplate, itemId);
                AladinResponse response = callAladinApi(url);

                if (response != null && response.getBooks() != null && !response.getBooks().isEmpty()) {
                    for (AladinResponse book : response.getBooks()) {
                        boolean isSaved = mappingService.processBookData(book);
                        if (isSaved) {
                            log.info("저장된 ItemId 도서: {}", book.getTitle());
                        } else {
                            log.info("중복된 책 스킵: {}", book.getIsbn13());
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
    private AladinResponse callAladinApi(String url) {
        try {
            log.info("API 호출 URL: {}", url);
            return restTemplate.getForObject(url, AladinResponse.class);
        } catch (Exception e) {
            log.error("API 호출 실패: {}", e.getMessage(), e);
            return null;
        }
    }

}