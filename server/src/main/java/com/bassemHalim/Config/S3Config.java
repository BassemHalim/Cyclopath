package com.bassemHalim.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {
    private final Region region = Region.US_WEST_2;
    @Value("${amazon.aws.accesskey}")
    private String Access_key;
    @Value("${amazon.aws.secretkey}")
    private String Secret_key;

    private StaticCredentialsProvider credentials() {
        return StaticCredentialsProvider
                .create(AwsBasicCredentials.create(Access_key, Secret_key));
    }

    @Bean
    public S3Client s3client() {
        return S3Client.builder()
                .region(region)
                .credentialsProvider(
                        credentials())
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(region)
                .credentialsProvider(credentials())
                .build();
    }
}
