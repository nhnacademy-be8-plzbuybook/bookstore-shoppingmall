package com.nhnacademy.book.objectStorage.service;

import com.nhnacademy.book.objectStorage.config.ObjectStorageConfig;
import com.nhnacademy.book.objectStorage.exception.ObjectStorageFileUploadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObjectStorageServiceTest {

    @Mock
    private ObjectStorageAuthService objectStorageAuthService;

    @Mock
    private ObjectStorageConfig objectStorageConfig;

    @Mock
    private RestTemplate restTemplate;

    private ObjectStorageService objectStorageService;

    @BeforeEach
    void setUp() {
        when(objectStorageConfig.getStorageUrl()).thenReturn("https://plzbuybook.store");
        when(objectStorageConfig.getContainerName()).thenReturn("objectStorage");

        objectStorageService = new ObjectStorageService(objectStorageConfig, objectStorageAuthService, restTemplate);
    }

    @Test
    @DisplayName("여러 파일 업로드 성공 테스트")
    void uploadFilesSuccess() throws IOException {
        MultipartFile file1 = mock(MultipartFile.class);
        MultipartFile file2 = mock(MultipartFile.class);

        when(file1.getOriginalFilename()).thenReturn("testFile1.png");
        when(file1.getInputStream()).thenReturn(new ByteArrayInputStream("mock file content 1".getBytes()));
        when(file2.getOriginalFilename()).thenReturn("testFile2.jpg");
        when(file2.getInputStream()).thenReturn(new ByteArrayInputStream("mock file content 2".getBytes()));

        String mockTokenResponse = validTokenResponse();
        when(objectStorageAuthService.requestToken()).thenReturn(mockTokenResponse);

        List<MultipartFile> files = List.of(file1, file2);
        List<String> result = objectStorageService.uploadObjects(files);

        assertEquals(2, result.size());
        assertTrue(result.get(0).endsWith(".png"));
        assertTrue(result.get(1).endsWith(".jpg"));

        verify(objectStorageAuthService, times(1)).requestToken();
        verify(file1, times(2)).getOriginalFilename();
        verify(file2, times(2)).getOriginalFilename();
    }

    @Test
    @DisplayName("단일 파일 업로드 성공 테스트")
    void uploadFileSuccess() throws IOException {
        // Mock 객체 설정
        MultipartFile file = mock(MultipartFile.class);

        when(file.getOriginalFilename()).thenReturn("testFile1.png");
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("mock file content".getBytes()));

        // 토큰 요청 Mock
        String mockTokenResponse = validTokenResponse();
        when(objectStorageAuthService.requestToken()).thenReturn(mockTokenResponse);

        // 단일 파일 리스트로 처리
        List<MultipartFile> files = List.of(file);
        List<String> result = objectStorageService.uploadObjects(files);

        // 결과 검증
        assertEquals(1, result.size());
        assertTrue(result.getFirst().endsWith(".png"));

        // Mock 객체의 호출 횟수 검증
        verify(objectStorageAuthService, times(1)).requestToken();
        verify(file, times(2)).getOriginalFilename();
    }


    @Test
    @DisplayName("허용되지 않은 파일 확장자 업로드 시 예외 발생")
    void uploadInvalidExtension() {
        MultipartFile invalidFile = mock(MultipartFile.class);
        when(invalidFile.getOriginalFilename()).thenReturn("testFile.csv");

        List<MultipartFile> files = List.of(invalidFile);

        assertThrows(ObjectStorageFileUploadException.class, () -> objectStorageService.uploadObjects(files));
    }

    @Test
    @DisplayName("빈 파일 리스트를 업로드 시도 시 예외 발생")
    void uploadEmptyFiles() {
        List<MultipartFile> files = new ArrayList<>();

        ObjectStorageFileUploadException exception = assertThrows(ObjectStorageFileUploadException.class,
                () -> objectStorageService.uploadObjects(files));

        assertEquals("No files upload.", exception.getMessage());
    }


    @Test
    @DisplayName("여러 파일 업로드 시 올바른 토큰 요청")
    void uploadRequestToken() throws IOException {
        MultipartFile file1 = mock(MultipartFile.class);
        MultipartFile file2 = mock(MultipartFile.class);

        when(file1.getOriginalFilename()).thenReturn("testFile1.png");
        when(file1.getInputStream()).thenReturn(new ByteArrayInputStream("mock file content 1".getBytes()));
        when(file2.getOriginalFilename()).thenReturn("testFile2.jpg");
        when(file2.getInputStream()).thenReturn(new ByteArrayInputStream("mock file content 2".getBytes()));

        String mockTokenResponse = validTokenResponse();
        when(objectStorageAuthService.requestToken()).thenReturn(mockTokenResponse);

        List<MultipartFile> files = List.of(file1, file2);
        objectStorageService.uploadObjects(files);

        // 토큰 요청이 한 번만 이루어지는지 검증
        verify(objectStorageAuthService, times(1)).requestToken();
    }

    @Test
    @DisplayName("파일 하나 성공, 하나 IOException 테스트")
    void uploadOneFileSuccessOneIOException() throws IOException {
        // Mock MultipartFile 객체
        MultipartFile successFile = mock(MultipartFile.class);
        MultipartFile failFile = mock(MultipartFile.class);

        // (성공)
        when(successFile.getOriginalFilename()).thenReturn("successFile.png");
        when(successFile.getInputStream()).thenReturn(new ByteArrayInputStream("success content".getBytes()));

        //  (실패: IOException 발생)
        when(failFile.getOriginalFilename()).thenReturn("failFile.jpg");
        when(failFile.getInputStream()).thenThrow(new IOException("File read error"));

        // Mock authService의 토큰 반환
        when(objectStorageAuthService.requestToken()).thenReturn(validTokenResponse());

        // 파일 리스트 준비
        List<MultipartFile> files = List.of(successFile, failFile);

        // 테스트: Exception 발생 확인
        ObjectStorageFileUploadException exception = assertThrows(
                ObjectStorageFileUploadException.class,
                () -> objectStorageService.uploadObjects(files)
        );

        // 예외 메시지 검증
        assertTrue(exception.getMessage().contains("Failed to upload file: failFile.jpg"));

        // 성공한 파일만 검증
        verify(successFile, times(1)).getInputStream();

        // 실패한 파일 검증
        verify(failFile, times(1)).getInputStream();
    }

    @Test
    @DisplayName("파일 하나 성공, 하나 HttpClientErrorException 실패 테스트")
    void uploadOneFileSuccessAndOneHttpClientErrorException() throws IOException {
        // Mock MultipartFile 객체
        MultipartFile successFile = mock(MultipartFile.class);
        MultipartFile failFile = mock(MultipartFile.class);

        // (성공)
        when(successFile.getOriginalFilename()).thenReturn("successFile.png");
        when(successFile.getInputStream()).thenReturn(new ByteArrayInputStream("success content".getBytes()));

        //  (실패: IOException 발생)
        when(failFile.getOriginalFilename()).thenReturn("failFile.jpg");
        when(failFile.getInputStream()).thenThrow(HttpClientErrorException.class);

        // Mock authService의 토큰 반환
        when(objectStorageAuthService.requestToken()).thenReturn(validTokenResponse());

        // 파일 리스트 준비
        List<MultipartFile> files = List.of(successFile, failFile);

        // 테스트: Exception 발생 확인
        ObjectStorageFileUploadException exception = assertThrows(
                ObjectStorageFileUploadException.class,
                () -> objectStorageService.uploadObjects(files)
        );

        // 예외 메시지 검증
        assertTrue(exception.getMessage().contains("Failed to upload file: failFile.jpg"));

        verify(successFile, times(1)).getInputStream();

        verify(failFile, times(1)).getInputStream();
    }

    @Test
    @DisplayName("유효한 URL 업로드 성공 테스트")
    void uploadValidUrlSuccess() throws IOException, InterruptedException {
        String validUrl = "https://example.com/testFile.png";
        String mockTokenResponse = validTokenResponse();
        InputStream mockInputStream = new ByteArrayInputStream("mock file content".getBytes());

        when(objectStorageAuthService.requestToken()).thenReturn(mockTokenResponse);
        try (MockedStatic<HttpClient> mockedHttpClient = mockStatic(HttpClient.class)) {
            HttpClient httpClientMock = mock(HttpClient.class);
            HttpResponse responseMock = mock(HttpResponse.class);

            mockedHttpClient.when(HttpClient::newHttpClient).thenReturn(httpClientMock);
            when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(responseMock);

            when(responseMock.statusCode()).thenReturn(200);
            when(responseMock.body()).thenReturn(mockInputStream);

            String result = objectStorageService.uploadObjects(validUrl);

            assertTrue(result.endsWith(".png"));
            verify(objectStorageAuthService, times(1)).requestToken();
        }
    }

    @Test
    @DisplayName("빈 URL 입력 시 예외 발생 테스트")
    void uploadEmptyUrl() {
        String emptyUrl = "";

        ObjectStorageFileUploadException exception = assertThrows(
                ObjectStorageFileUploadException.class,
                () -> objectStorageService.uploadObjects(emptyUrl)
        );

        assertEquals("No URLs provided for upload.", exception.getMessage());
    }

    @Test
    @DisplayName("잘못된 URL 업로드 시 예외 발생 테스트")
    void uploadInvalidUrl() {
        String invalidUrl = "invalid-url";

        ObjectStorageFileUploadException exception = assertThrows(
                ObjectStorageFileUploadException.class,
                () -> objectStorageService.uploadObjects(invalidUrl)
        );

        assertTrue(exception.getMessage().contains("Failed to create InputStream from URL"));
    }

    private String validTokenResponse() {
        return """
        {
          "access": {
            "token": {
              "id": "mock-token-id"
            }
          }
        }
        """;
    }
}
