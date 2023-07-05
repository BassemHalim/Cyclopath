package com.bassemHalim;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class CyclopathApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(CyclopathApplication.class, args);
    }
}

