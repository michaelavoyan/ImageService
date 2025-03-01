/**
 * Created by Michael Avoyan on 01/03/2025.
 */

package com.michaelavoyan.imageservice.errors;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 * Global exception handler for handling various exceptions in the application.
 * This class provides centralized exception handling using Spring's @RestControllerAdvice.
 * It handles generic exceptions, bad requests, and entity not found errors,
 * returning appropriate HTTP status codes and messages.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Handles generic exceptions.
     *
     * @param e the caught exception
     * @return a response entity with a 500 Internal Server Error status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
    }

    /**
     * Handles IllegalArgumentException, indicating a bad request.
     *
     * @param e the caught exception
     * @return a response entity with a 400 Bad Request status
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Bad Request: " + e.getMessage());
    }

    /**
     * Handles EntityNotFoundException, indicating a resource was not found.
     *
     * @param e the caught exception
     * @return a response entity with a 404 Not Found status
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Not Found: " + e.getMessage());
    }
}
