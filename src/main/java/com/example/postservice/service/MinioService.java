package com.example.postservice.service;

import com.example.postservice.config.MinioConfig;
import com.example.postservice.config.MinioProperties;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioConfig minioConfig;
    private final MinioProperties minioProperties;

    public String upload(MultipartFile file, String objectName) {
        try {
            MinioClient minioClient = minioConfig.minioClient();

            try (InputStream is = file.getInputStream()) {

                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(minioProperties.getBucket())
                        .object(objectName)
                        .stream(is, file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());

                return minioProperties.getUrl() + "/" + minioProperties.getBucket() + "/" + objectName;

            } catch (IOException e) {
                throw new RuntimeException("MinIO upload failed" + e);
            } catch (ServerException | InsufficientDataException | ErrorResponseException | NoSuchAlgorithmException |
                     InvalidKeyException | InvalidResponseException | XmlParserException | InternalException e) {
                throw new RuntimeException(e);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
