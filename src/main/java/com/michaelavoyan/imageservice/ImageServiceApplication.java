/**
 * Created by Michael Avoyan on 01/03/2025.
 */

package com.michaelavoyan.imageservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The {@code ImageServiceApplication} class is the entry point for the Image Service application.
 * This application is a Spring Boot service responsible for handling image-related operations.
 * It initializes the Spring Boot framework and runs the application context.
 */
@SpringBootApplication
public class ImageServiceApplication {
    /**
     * The main method that serves as the entry point for the Spring Boot application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(ImageServiceApplication.class, args);
    }

}
