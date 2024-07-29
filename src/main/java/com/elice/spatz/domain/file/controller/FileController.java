package com.elice.spatz.domain.file.controller;
import com.elice.spatz.domain.file.entity.File;
import com.elice.spatz.domain.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<File> uploadFile(@RequestParam("file") MultipartFile file,
                                           @RequestParam("messageId") Long messageId) throws IOException {
        File uploadedFile = fileService.uploadFile(file, messageId);
        return ResponseEntity.ok(uploadedFile);
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        try {
            boolean fileExists = fileService.doesFileExist(fileName);
            if (!fileExists) {
                return ResponseEntity.ok("File does not exist.");
            }

            fileService.deleteFile(fileName);
            return ResponseEntity.ok("File delete success.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File delete fail. Error: " + e.getMessage());
        }

    }
}


