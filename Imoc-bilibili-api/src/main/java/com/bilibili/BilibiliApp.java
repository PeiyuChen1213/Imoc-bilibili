package com.bilibili;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class BilibiliApp {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(BilibiliApp.class, args);
    }
}