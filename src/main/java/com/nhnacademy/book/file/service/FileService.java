package com.nhnacademy.book.file.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {

    private final String baseDirectory = "src/main/resources/static/images"; // 프로젝트 하위 디렉토리로 변경

    public void uploadFiles(String directoryName, List<MultipartFile> files) throws IOException {
        Path uploadPath = Paths.get(baseDirectory, directoryName);

        // 폴더가 없으면 생성
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 파일 업로드 처리
        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            String fileName = UUID.randomUUID() + "_" + originalFilename; // 고유한 이름 생성
            Path filePath = uploadPath.resolve(fileName);

            try (var inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath);
            } catch (IOException e) {
                throw new IOException("Could not save file: " + fileName, e);
            }
        }
    }

    public String getFileUrl(String directoryName, String fileName) {
        return "/images/" + directoryName + "/" + fileName;
    }



    public void deleteFile(String directoryName, String fileName) throws IOException {
        Path filePath = Paths.get(baseDirectory, directoryName, fileName);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
        } else {
            throw new IOException("File not found: " + filePath);
        }
    }

    public void deleteFiles(String directoryName, List<String> fileNames) throws IOException {
        for (String fileName : fileNames) {
            deleteFile(directoryName, fileName);
        }
    }

}
