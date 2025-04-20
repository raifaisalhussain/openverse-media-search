package com.mediaapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MediaAppApplication {

    private static final Logger logger = LoggerFactory.getLogger(MediaAppApplication.class);

    public static void main(String[] args) {
        try {
            SpringApplication.run(MediaAppApplication.class, args);
            logger.info("MediaAppApplication started successfully.");
        } catch (Exception e) {
            logger.error("Application failed to start: {}", e.getMessage(), e);
        }
    }
}
