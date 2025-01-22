package com.collabit.global.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class S3Service {

    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private static final String BASEURL = "https://collabit-s3.s3.ap-northeast-2.amazonaws.com/";

    public String upload(MultipartFile file, String dirName) {
        if (file.isEmpty()) {
            return null;
        }
        String fileName = dirName + "/" + UUID.randomUUID() + file.getOriginalFilename();
        try (InputStream inputStream = file.getInputStream()) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return extractUrl(amazonS3.getUrl(bucket, fileName).toString());
    }

    public boolean delete(String fileUrl) {
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileUrl));
            return true;
        } catch (AmazonServiceException e) {
            System.err.println("Error occurred while deleting object from S3: " + e.getMessage());
        } catch (SdkClientException e) {
            System.err.println("Client error while deleting object from S3: " + e.getMessage());
        }
        return false;
    }

    private String extractUrl(String Url) {
        if (Url.startsWith(BASEURL)) {
            return Url.substring(BASEURL.length());
        }
        return null;
    }
}