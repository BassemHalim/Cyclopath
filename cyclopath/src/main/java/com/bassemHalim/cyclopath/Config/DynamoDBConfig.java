package com.bassemHalim.cyclopath.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

// source: https://dynobase.dev/dynamodb-java-with-dynamodbmapper/
@Configuration
public class DynamoDBConfig {
    @Value("${amazon.aws.accesskey}")
    private String Access_key;
    @Value("${amazon.aws.secretkey}")
    private String Secret_key;


//    @Bean
//    public DynamoDBMapper dynamoDBMapper() {
//        return new DynamoDBMapper(builfAmazonDynamoDB());
//    }
//
//    private AmazonDynamoDB builfAmazonDynamoDB() {
//        return AmazonDynamoDBClientBuilder
//                .standard()
//                .withRegion(Regions.US_WEST_1)
//                .withCredentials(
//                        new AWSStaticCredentialsProvider(
//                                new BasicAWSCredentials(
//                                        Access_key,
//                                        Secret_key)
//                        )
//
//                ).build();
//    }
//}

    @Bean
    DynamoDbClient amazonDynamoDBClient() {
        return getDynamoDbClient();
    }

    @Bean
    DynamoDbEnhancedClient amazonDynamoDBEnhancedClient() {
        return DynamoDbEnhancedClient.builder().dynamoDbClient(getDynamoDbClient()).build();
    }

    private DynamoDbClient getDynamoDbClient() {
        ClientOverrideConfiguration.Builder overrideConfig =
                ClientOverrideConfiguration.builder();

        return DynamoDbClient.builder()
                .overrideConfiguration(overrideConfig.build())
                .endpointOverride(URI.create("http://localhost:8000"))
                .region(Region.US_WEST_1)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(Access_key, Secret_key)))
                .build();
    }
}