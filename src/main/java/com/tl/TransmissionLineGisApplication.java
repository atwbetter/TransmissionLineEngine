package com.tl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TransmissionLineGisApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransmissionLineGisApplication.class, args);
    }

}