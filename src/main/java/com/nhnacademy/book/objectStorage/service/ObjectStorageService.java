package com.nhnacademy.book.objectStorage.service;

import com.nhnacademy.book.objectStorage.config.ObjectStorageConfig;
import com.nhnacademy.book.objectStorage.exception.ObjectStorageFileUploadException;
import com.nhnacademy.book.objectStorage.exception.ObjectStorageTokenException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ObjectStorageService {

    // 허용된 파일 확장자 목록 (이미지, PDF, TXT 등)
    private static final List<String> ALLOWED_FILE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "pdf", "txt");

    private final ObjectStorageAuthService objectStorageAuthService;
    private final String storageUrl;
    private final String containerName;
    private final RestTemplate restTemplate;

    public ObjectStorageService(ObjectStorageConfig objectStorageConfig, ObjectStorageAuthService objectStorageAuthService,
                                RestTemplate restTemplate) {
        this.objectStorageAuthService = objectStorageAuthService;
        this.storageUrl = normalizeUrl(objectStorageConfig.getStorageUrl());
        this.containerName = objectStorageConfig.getContainerName();
        this.restTemplate = restTemplate;
    }

    public String normalizeUrl(String url) {
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    public String getUrl(String fileName) {
        return String.format("%s/%s/%s", storageUrl, containerName, fileName);
    }

    public List<String> uploadObjects(List<MultipartFile> multipartFiles) {
        if (multipartFiles == null || multipartFiles.isEmpty()) {
            throw new ObjectStorageFileUploadException("No files upload.");
        }

        String requestToken = objectStorageAuthService.requestToken();
        List<String> uploadedFileNames = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            try {
                validateFile(multipartFile); // 파일 검증
                String fileName = createUniqueFileName(multipartFile.getOriginalFilename());
                String tokenId = extractTokenId(requestToken);
                String url = getUrl(fileName);

                uploadFileToStorage(url, multipartFile.getInputStream(), tokenId); // 파일 업로드
                uploadedFileNames.add(fileName);
            } catch (HttpClientErrorException | IOException e) {
                throw new ObjectStorageFileUploadException("Failed to upload file: " + multipartFile.getOriginalFilename());
            }
        }
        return uploadedFileNames;
    }

    public String uploadObjects(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            throw new ObjectStorageFileUploadException("No URLs provided for upload.");
        }

        String requestToken = objectStorageAuthService.requestToken();
        List<String> uploadedFileNames = new ArrayList<>();
        String name = "";

        try (InputStream inputStream = createInputStreamFromUrl(fileUrl)) {
            String fileName = createUniqueFileName(getFileNameFromUrl(fileUrl));
            String tokenId = extractTokenId(requestToken);
            String url = getUrl(fileName);
            name = fileName;

            uploadFileToStorage(url, inputStream, tokenId);
            uploadedFileNames.add(fileName);
        } catch (IOException e) {
            throw new ObjectStorageFileUploadException("Failed to upload file from URL: " + fileUrl);
        }

        return name;
    }

    // 파일 검증 메서드 (이미지 및 기타 파일 형식 지원)
    private void validateFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            throw new ObjectStorageFileUploadException("File name is invalid");
        }

        String extension = getFileExtension(fileName);
        if (!ALLOWED_FILE_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new ObjectStorageFileUploadException("Unsupported file type: " + extension);
        }
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex != -1 && dotIndex < fileName.length() - 1) ? fileName.substring(dotIndex + 1) : "";
    }

    private String createUniqueFileName(String originalFileName) {
        String uuid = UUID.randomUUID().toString();
        String extension = getFileExtension(originalFileName);

        return uuid + (extension.isEmpty() ? "" : "." + extension);
    }

    private String extractTokenId(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json);

            String tokenId = rootNode.path("access").path("token").path("id").asText();
            if (tokenId == null || tokenId.isEmpty()) {
                throw new ObjectStorageTokenException("Token ID is null or empty");
            }

            return tokenId;
        } catch (IOException e) {
            throw new ObjectStorageTokenException("Failed to extract token ID");
        }
    }

    private String getFileNameFromUrl(String urlString) {
        try {
            URI uri = URI.create(urlString);
            String path = uri.getPath();
            return path.substring(path.lastIndexOf('/') + 1);
        } catch (Exception e) {
            throw new ObjectStorageFileUploadException("Invalid URL: " + urlString);
        }
    }

    private InputStream createInputStreamFromUrl(String fileUrl) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fileUrl))
                    .GET()
                    .build();

            HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());

            if (response.statusCode() != 200) {
                throw new IOException("Failed to fetch resource: HTTP " + response.statusCode());
            }

            return response.body();
        } catch (Exception e) {
            throw new ObjectStorageFileUploadException("Failed to create InputStream from URL: " + fileUrl);
        }
    }

    private void uploadFileToStorage(String url, InputStream inputStream, String tokenId) throws IOException {
        RequestCallback requestCallback = request -> {
            request.getHeaders().add("X-Auth-Token", tokenId);
            IOUtils.copy(inputStream, request.getBody());
        };

        restTemplate.execute(url, HttpMethod.PUT, requestCallback, null);
    }
}
