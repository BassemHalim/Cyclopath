//package com.bassemHalim.cyclopath;
//
//
////@SpringBootTest
////class CyclopathApplicationTests {
//
//import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.AWSCredentialsProvider;
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
//import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
//import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
//import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
//import com.amazonaws.services.dynamodbv2.util.TableUtils;
//import com.bassemHalim.cyclopath.User.User;
//import com.bassemHalim.cyclopath.User.UserRepository;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.platform.commons.logging.Logger;
//import org.junit.platform.commons.logging.LoggerFactory;
//import org.junit.runner.RunWith;
//import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {PropertyPlaceholderAutoConfiguration.class, CyclopathApplicationTests.DynamoDBConfig.class})
//public class CyclopathApplicationTests {
//    private static final Logger log = LoggerFactory.getLogger(CyclopathApplicationTests.class);
//
//    @Configuration
//    @EnableDynamoDBRepositories(basePackageClasses = UserRepository.class)
//    public static class DynamoDBConfig {
//
//        @Value("${amazon.aws.accesskey}")
//        private String amazonAWSAccessKey;
//
//        @Value("${amazon.aws.secretkey}")
//        private String amazonAWSSecretKey;
//
//        public AWSCredentialsProvider amazonAWSCredentialsProvider() {
//            return new AWSStaticCredentialsProvider(amazonAWSCredentials());
//        }
//
//        @Bean
//        public AWSCredentials amazonAWSCredentials() {
//            return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
//        }
//
//        @Bean
//        public DynamoDBMapperConfig dynamoDBMapperConfig() {
//            return DynamoDBMapperConfig.DEFAULT;
//        }
//
////        @Bean
////        public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB, DynamoDBMapperConfig config) {
////            return new DynamoDBMapper(amazonDynamoDB, config);
////        }
//
//        @Bean
//        public AmazonDynamoDB amazonDynamoDB() {
//            return AmazonDynamoDBClientBuilder.standard().withCredentials(amazonAWSCredentialsProvider())
//                    .withRegion(Regions.US_WEST_1).build();
//        }
//    }
//
//    @Autowired
//    private UserRepository userRepo;
//
//    @Test
//    public void testSave() {
//        User gosling = new User("Bassem", "bassem@email.com", "1234");
//        userRepo.save(gosling);
//
//        User hoeller = new User("Ragui", "ragui@email.com", "1234");
//        userRepo.save(hoeller);
//
////        for (User u : userRepo.findAll()) {
////            System.out.println(u);
////        }
//    }
//
//    @Autowired
//    private AmazonDynamoDB amazonDynamoDB;
//    @Autowired
//    private DynamoDBMapper mapper;
//    private boolean tableWasCreatedForTest;
//
//    @Before
//    public void init() throws Exception {
//        CreateTableRequest ctr = mapper.generateCreateTableRequest(User.class)
//                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
//        tableWasCreatedForTest = TableUtils.createTableIfNotExists(amazonDynamoDB, ctr);
////        if (tableWasCreatedForTest) {
////            log.info("Created table {}", ctr.getTableName());
////        }
//        TableUtils.waitUntilActive(amazonDynamoDB, ctr.getTableName());
////        log.info("Table {} is active", ctr.getTableName());
//    }
//
//    @After
//    public void destroy() throws Exception {
//        if (tableWasCreatedForTest) {
//            DeleteTableRequest dtr = mapper.generateDeleteTableRequest(User.class);
//            TableUtils.deleteTableIfExists(amazonDynamoDB, dtr);
////            log.info("Deleted table {}", dtr.getTableName());
//        }
//    }
//}
