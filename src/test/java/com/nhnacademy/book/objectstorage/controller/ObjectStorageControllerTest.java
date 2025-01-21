//package com.nhnacademy.book.objectstorage.controller;
//
//import com.nhnacademy.book.book.dto.response.FileUploadResponse;
//import com.nhnacademy.book.objectstorage.exception.ObjectStorageFileUploadException;
//import com.nhnacademy.book.objectstorage.service.ObjectStorageService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.anyList;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(MockitoExtension.class)
//public class ObjectStorageControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private ObjectStorageService objectStorageService;
//
//    @Spy
//    @InjectMocks
//    private ObjectStorageController objectStorageController;
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(objectStorageController).build();
//    }
//
//    @Test
//    @DisplayName("파일 업로드 성공 테스트")
//    void uploadObjects_Success() throws Exception {
//        // Given
//        MockMultipartFile file1 = new MockMultipartFile("files", "file1.txt", "text/plain", "file content".getBytes());
//        MockMultipartFile file2 = new MockMultipartFile("files", "file2.txt", "text/plain", "file content".getBytes());
//        List<String> uploadedFileNames = Arrays.asList("file1.txt", "file2.txt");
//
//        // 모킹: 서비스에서 반환할 값 설정
//        when(objectStorageService.uploadObjects(anyList())).thenReturn(uploadedFileNames);
//
//        // When & Then
//        mockMvc.perform(multipart("/api/objects/upload_file")
//                        .file(file1)
//                        .file(file2)
//                        .accept(MediaType.TEXT_PLAIN)) // 응답 형식을 TEXT_PLAIN으로 명시
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("파일 업로드 실패 테스트")
//    void uploadObjects_Failure() throws Exception {
//        // Given
//        MockMultipartFile file = new MockMultipartFile("files", "file.txt", "text/plain", "file content".getBytes());
//
//        // 모킹: 서비스에서 예외 발생
//        when(objectStorageService.uploadObjects(anyList())).thenThrow(new ObjectStorageFileUploadException("Upload error"));
//
//        // When & Then
//        mockMvc.perform(multipart("/api/objects/upload_file")
//                        .file(file)
//                        .accept(MediaType.TEXT_PLAIN)) // 응답 형식을 TEXT_PLAIN으로 명시
//                .andDo(print())
//                .andExpect(status().isInternalServerError());
//    }
//
//    @Test
//    @DisplayName("URL로 파일 업로드 성공 테스트")
//    void uploadObjectByUrl_Success() throws Exception {
//        // Given
//        String imageUrl = "http://example.com/image.jpg";
//        String uploadedFileName = "image.jpg";
//
//        // 모킹: 서비스에서 반환할 값 설정
//        when(objectStorageService.uploadObjects(anyString())).thenReturn(uploadedFileName);
//
//        // When & Then
//        mockMvc.perform(post("/api/objects/upload_url")
//                        .param("url", imageUrl)
//                        .accept(MediaType.TEXT_PLAIN)) // 응답 형식을 TEXT_PLAIN으로 명시
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("URL로 파일 업로드 실패 테스트")
//    void uploadObjectByUrl_Failure() throws Exception {
//        // Given
//        String imageUrl = "http://example.com/image.jpg";
//
//        // 모킹: 서비스에서 예외 발생
//        when(objectStorageService.uploadObjects(anyString())).thenThrow(new ObjectStorageFileUploadException("Upload error"));
//
//        // When & Then
//        mockMvc.perform(post("/api/objects/upload_url")
//                        .param("url", imageUrl)
//                        .accept(MediaType.TEXT_PLAIN)) // 응답 형식을 TEXT_PLAIN으로 명시
//                .andDo(print())
//                .andExpect(status().isInternalServerError());
//    }
//
//    @Test
//    @DisplayName("파일 업로드 URL 변환 성공 테스트")
//    void uploadObjectsToUrl_Success() throws Exception {
//        // Given
//        MockMultipartFile file1 = new MockMultipartFile("files", "file1.txt", "text/plain", "file content".getBytes());
//        MockMultipartFile file2 = new MockMultipartFile("files", "file2.txt", "text/plain", "file content".getBytes());
//        List<FileUploadResponse> uploadResponses = Arrays.asList(
//                new FileUploadResponse("file1.txt"),
//                new FileUploadResponse("file2.txt")
//        );
//
//        // 모킹: 서비스에서 반환할 값 설정
//        when(objectStorageService.uploadObjectstourl(anyList())).thenReturn(uploadResponses);
//
//        // When & Then
//        mockMvc.perform(multipart("/api/objects/upload_files")
//                        .file(file1)
//                        .file(file2)
//                        .accept(MediaType.APPLICATION_JSON)) // 응답 형식을 JSON으로 명시
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    }
//
//    @Test
//    @DisplayName("파일 업로드 URL 변환 실패 테스트")
//    void uploadObjectsToUrl_Failure() throws Exception {
//        // Given
//        MockMultipartFile file1 = new MockMultipartFile("files", "file1.txt", "text/plain", "file content".getBytes());
//
//        // 모킹: 서비스에서 예외 발생
//        when(objectStorageService.uploadObjectstourl(anyList())).thenThrow(new ObjectStorageFileUploadException("Upload error"));
//
//        // When & Then
//        mockMvc.perform(multipart("/api/objects/upload_files")
//                        .file(file1)
//                        .accept(MediaType.APPLICATION_JSON)) // 응답 형식을 JSON으로 명시
//                .andDo(print())
//                .andExpect(status().isInternalServerError());
//    }
//}
