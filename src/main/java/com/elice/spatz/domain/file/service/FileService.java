package com.elice.spatz.domain.file.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.amazonaws.util.IOUtils;
import com.elice.spatz.domain.file.repository.FileRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    private final AmazonS3 s3Client;
    private final FileRepository fileRepository;

    @Value("${file.upload.max-size}")
    private long maxFileSize;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file) {
        String key = UUID.randomUUID() + "_" + file.getOriginalFilename();
        try {

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            PutObjectRequest request = new PutObjectRequest(bucketName, key, file.getInputStream(), metadata);
            request.withCannedAcl(CannedAccessControlList.AuthenticatedRead); // 접근권한 체크
            PutObjectResult result = s3Client.putObject(request);
            return key;
        } catch (AmazonServiceException e) {
            logger.error("uploadToAWS AmazonServiceException filePath={}, yyyymm={}, error={}", e.getMessage());
        } catch (SdkClientException e) {
            logger.error("uploadToAWS SdkClientException filePath={}, error={}", e.getMessage());
        } catch (Exception e) {
            logger.error("uploadToAWS SdkClientException filePath={}, error={}", e.getMessage());
        }

        return "";
    }

    public void deleteFile(String fileName) {
        try{
            logger.info("Attempting to delete file: {}", fileName);
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
            logger.info("File deleted from S3: {}", fileName);
            fileRepository.deleteByStorageUrl(s3Client.getUrl(bucketName, fileName).toString());
        } catch (Exception e) {
        logger.error("Error deleting file from S3: {}", fileName, e);
        throw e;
    }

    }

    public boolean doesFileExist(String fileName) {
        return s3Client.doesObjectExist(bucketName, fileName);
    }

    public List<String> listFiles() {
        ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName);
        ListObjectsV2Result result = s3Client.listObjectsV2(req);

        return result.getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
    }


    public boolean downloadFile(String fileKey, String downloadFileName, HttpServletRequest request, HttpServletResponse response) throws BadRequestException {
        if (fileKey == null) {
            return false;
        }
        S3Object fullObject = null;
        try {
            fullObject = s3Client.getObject(bucketName, fileKey);
            if (fullObject == null) {
                return false;
            }
        } catch (AmazonS3Exception e) {
            throw new BadRequestException("다운로드 파일이 존재하지 않습니다.");
        }

        OutputStream os = null;
        FileInputStream fis = null;
        boolean success = false;
        try {
            S3ObjectInputStream objectInputStream = fullObject.getObjectContent();
            byte[] bytes = IOUtils.toByteArray(objectInputStream);

            String fileName = null;
            if(downloadFileName != null) {

                fileName=  getEncodedFilename(request, downloadFileName);
            } else {
                fileName=  getEncodedFilename(request, fileKey);
            }

            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setHeader( "Content-Disposition", "attachment; filename=\"" + fileName + "\";" );
            response.setHeader("Content-Length", String.valueOf(fullObject.getObjectMetadata().getContentLength()));
            response.setHeader("Set-Cookie", "fileDownload=true; path=/");
            FileCopyUtils.copy(bytes, response.getOutputStream());
            success = true;
        } catch (IOException e) {
            logger.debug(e.getMessage(), e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                logger.debug(e.getMessage(), e);
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                logger.debug(e.getMessage(), e);
            }
        }
        return success;
    }


    private String getEncodedFilename(HttpServletRequest request, String displayFileName) throws UnsupportedEncodingException, UnsupportedEncodingException {
        String header = request.getHeader("User-Agent");

        String encodedFilename = null;
        if (header.indexOf("MSIE") > -1) {
            encodedFilename = URLEncoder.encode(displayFileName, "UTF-8").replaceAll("\\+", "%20");
        } else if (header.indexOf("Trident") > -1) {
            encodedFilename = URLEncoder.encode(displayFileName, "UTF-8").replaceAll("\\+", "%20");
        } else if (header.indexOf("Chrome") > -1) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < displayFileName.length(); i++) {
                char c = displayFileName.charAt(i);
                if (c > '~') {
                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
                } else {
                    sb.append(c);
                }
            }
            encodedFilename = sb.toString();
        } else if (header.indexOf("Opera") > -1) {
            encodedFilename = "\"" + new String(displayFileName.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (header.indexOf("Safari") > -1) {
            encodedFilename = URLDecoder.decode("\"" + new String(displayFileName.getBytes("UTF-8"), "8859_1") + "\"", "UTF-8");
        } else {
            encodedFilename = URLDecoder.decode("\"" + new String(displayFileName.getBytes("UTF-8"), "8859_1") + "\"", "UTF-8");
        }
        return encodedFilename;

    }

    public URL generatePresignedUrl(String fileName, String contentType) {
        // 유효 기간 설정 (1일)
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60 * 24; // 1일
        expiration.setTime(expTimeMillis);

        // Content-Disposition 헤더 설정
        ResponseHeaderOverrides headerOverrides = new ResponseHeaderOverrides()
                .withContentDisposition("inline; filename=\"" + fileName + "\"")
                .withContentType(contentType);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, fileName)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);

        return s3Client.generatePresignedUrl(generatePresignedUrlRequest);
    }
}
