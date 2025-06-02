package com.hsbc.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author zpyu
 */
@EnableCaching
@SpringBootApplication
public class HsbcTransactionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HsbcTransactionServiceApplication.class, args);
    }

}
