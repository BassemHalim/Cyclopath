package com.bassemHalim.cyclopath.Config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// source: https://dynobase.dev/dynamodb-java-with-dynamodbmapper/
@Configuration
public class DynamoDBConfig {
    @Value("${amazon.aws.accesskey}")
    private String Access_key;
    @Value("${amazon.aws.secretkey}")
    private String Secret_key;


    @Bean
    public DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(builfAmazonDynamoDB());
    }

    private AmazonDynamoDB builfAmazonDynamoDB() {
        return AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(Regions.US_WEST_1)
                .withCredentials(
                        new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(
                                        Access_key,
                                        Secret_key)
                        )

                ).build();
    }
}
