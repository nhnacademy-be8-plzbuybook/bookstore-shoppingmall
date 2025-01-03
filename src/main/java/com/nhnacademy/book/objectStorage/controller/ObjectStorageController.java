package com.nhnacademy.book.objectStorage.controller;

import com.nhnacademy.book.objectStorage.exception.ObjectStorageFileUploadException;
import com.nhnacademy.book.objectStorage.service.ObjectStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/objects")
public class ObjectStorageController {

    private final ObjectStorageService objectStorageService;

    public ObjectStorageController (ObjectStorageService objectStorageService) {
        this.objectStorageService = objectStorageService;
    }

    @PostMapping("/upload_file")
    public ResponseEntity<String> uploadObjects(@RequestParam("files") List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return ResponseEntity.badRequest().body("No files provided for upload.");
        }

        try {
            List<String> uploadedFileNames = objectStorageService.uploadObjects(files);
            return ResponseEntity.ok("Files uploaded successfully: " + String.join(", ", uploadedFileNames));
        } catch (ObjectStorageFileUploadException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload failed: " + e.getMessage());
        }
    }


    /*
        - 이미지 링크로 도서의 상세 이미지 업로드
        - @RequestParam("url")로 이미지링크를 받음
        - 업로드 성공 시 업로드된 파일 이름 반환
     */
    @PostMapping("/upload_url")
    public ResponseEntity<String> uploadObjectByUrl(@RequestParam("url") String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return ResponseEntity.badRequest().body("No URL provided for upload.");
        }

        try {
            String uploadedFileName = objectStorageService.uploadObjects(imageUrl);
            return ResponseEntity.ok("File uploaded successfully: " + uploadedFileName);
        } catch (ObjectStorageFileUploadException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload failed: " + e.getMessage());
        }
    }



}
