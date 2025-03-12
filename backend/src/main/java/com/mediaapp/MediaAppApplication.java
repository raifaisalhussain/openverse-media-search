package com.mediaapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MediaAppApplication {
	public static void main(String[] args) {
		System.out.println("🚀 Application is starting...");  // Print statement
		SpringApplication.run(MediaAppApplication.class, args);
		System.out.println("✅ Application started successfully!");  // Print statement
	}
}
