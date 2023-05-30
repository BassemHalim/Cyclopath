package com.bassemHalim.cyclopath.Repositoy;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import okhttp3.HttpUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Log
public class MapRepository {

    private final S3Client s3Client;
    private final S3Presigner presigner;
    @Value("${S3_BUCKET_NAME}")
    private String BUCKET_NAME;

    public void putObject(String key, byte[] file) {
        log.info("storing object");
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();
        try {
            PutObjectResponse res = s3Client.putObject(objectRequest, RequestBody.fromBytes(file));

        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    public Optional<byte[]> getObject(String key) {
        if (!objectExists(key)) return Optional.empty();
        log.info("getting object");
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();
        ResponseInputStream<GetObjectResponse> res = s3Client.getObject(objectRequest);
        try {
            return Optional.of(res.readAllBytes());
        } catch (IOException e) {
            log.info(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean objectExists(String key) {
        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();
        try {
            HeadObjectResponse res = s3Client.headObject(headObjectRequest);
        } catch (NoSuchKeyException e) {
            return false;
        }
        return true;
    }

    public Optional<HttpUrl> generatePresignedUrl(String key) {
        if (!objectExists(key)) return Optional.empty();
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignRequest);
        return Optional.of(HttpUrl.get(presignedGetObjectRequest.url()));
    }
}
