package com.elice.spatz.domain.file.controller;

import com.elice.spatz.domain.file.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Controller
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileService fileService;

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        System.out.println("Test endpoint hit");
        return ResponseEntity.ok("Test successful");
    }

    @GetMapping("/frontTest")
    public String frontTest(){
        return "/FileDownloadTest.html";
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String key = fileService.uploadFile(file);
        if (!key.isEmpty()) {
            return ResponseEntity.ok(key);
        } else {
            return ResponseEntity.status(500).body("File upload failed");
        }
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

    @GetMapping("/list")
    public ResponseEntity<List<String>> listFiles() {
        List<String> fileList = fileService.listFiles();
        return ResponseEntity.ok(fileList);
    }

    @GetMapping("/download/{fileKey:.+}")
    public void downloadFile(@PathVariable String fileKey,
                               @RequestParam(required = false) String downloadFileName,
                               HttpServletRequest request,
                               HttpServletResponse response) throws BadRequestException {
        try {
            boolean success = fileService.downloadFile(fileKey, downloadFileName, request, response);
            System.out.println(success);

            if (!success) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("File not found");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().write("Error downloading file");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @GetMapping("/presigned-url/{fileName}")
    public ResponseEntity<URL> generatePresignedUrl(@PathVariable String fileName) {
        String contentType = "application/octet-stream";  // 기본 Content-Type
        // 파일 확장자를 기준으로 Content-Type 설정
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            contentType = "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            contentType = "image/png";
        } else if (fileName.endsWith(".pdf")) {
            contentType = "application/pdf";
        } else if (fileName.endsWith(".txt")) {
            contentType = "text/plain";
        } else if (fileName.endsWith(".mp4")) {
            contentType = "video/mp4";
        }

        URL presignedUrl = fileService.generatePresignedUrl(fileName, contentType);
        return ResponseEntity.ok(presignedUrl);
    }
}


