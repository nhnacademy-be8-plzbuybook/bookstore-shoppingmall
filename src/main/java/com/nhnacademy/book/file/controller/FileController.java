package com.nhnacademy.book.file.controller;

import com.nhnacademy.book.file.exception.FileDeleteException;
import com.nhnacademy.book.file.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }


    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("directoryName") String directoryName,
                                                    @RequestParam("files") List<MultipartFile> files) throws IOException {
        fileService.uploadFiles(directoryName, files);

        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String url = fileService.getFileUrl(directoryName, fileName);
            fileUrls.add("http://220.67.216.14:10143" + url);
        }

        return ResponseEntity.ok(fileUrls);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFiles(@RequestParam("directoryName") String directoryName,
                                            @RequestParam("fileNames") List<String> fileNames) throws IOException, FileDeleteException {
        fileService.deleteFiles(directoryName, fileNames);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{fileName}")
    public ResponseEntity<Void> deleteFile(@PathVariable("fileName") String fileName,
                                           @PathVariable("direcotryName") String directoryName) throws IOException, FileDeleteException {
        fileService.deleteFile(directoryName, fileName);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
