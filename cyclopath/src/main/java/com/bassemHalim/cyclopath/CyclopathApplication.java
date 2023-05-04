package com.bassemHalim.cyclopath;

//import com.bassemHalim.cyclopath.User.UserRepository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;

//@SpringBootApplication
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//@EnableDynamoDBRepositories(basePackageClasses = UserRepository.class)
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class CyclopathApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(CyclopathApplication.class, args);
    }
}

