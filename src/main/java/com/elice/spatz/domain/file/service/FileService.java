package com.elice.spatz.domain.file.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import java.io.IOException;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.elice.spatz.domain.file.entity.File;
import com.elice.spatz.domain.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileService {

    private final AmazonS3 s3Client;
    private final FileRepository fileRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public File uploadFile(MultipartFile file, Long messageId) throws IOException {
        String fileName = file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        String fileUrl = s3Client.getUrl(bucketName, fileName).toString();

        File fileEntity = new File();
        fileEntity.setMessageId(messageId);
        fileEntity.setStorageUrl(fileUrl);
        return fileRepository.save(fileEntity);
    }

    public void deleteFile(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
        fileRepository.deleteByStorageUrl(s3Client.getUrl(bucketName, fileName).toString());
    }

    public boolean doesFileExist(String fileName) {
        return s3Client.doesObjectExist(bucketName, fileName);
    }
}
