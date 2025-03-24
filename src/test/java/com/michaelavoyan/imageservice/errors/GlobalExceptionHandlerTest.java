/**
 * Created by Michael Avoyan on 01/03/2025.
 */

package com.michaelavoyan.imageservice.errors;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

/**
 * Unit tests for the {@link GlobalExceptionHandler} class.
 * This test class verifies the behavior of exception handling methods in the GlobalExceptionHandler.
 * Each test method simulates a specific exception and asserts that the handler produces the expected
 * HTTP response status and message.
 */
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    /**
     * Tests that a generic {@link Exception} is correctly handled.
     * Verifies that the response contains HTTP 500 status and the expected error message.
     */
    @Test
    void testHandleGenericException() {
        Exception exception = new Exception("Unexpected error occurred");

        ResponseEntity<String> response = globalExceptionHandler.handleGenericException(exception);

        assertNotNull(response);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error: Unexpected error occurred", response.getBody());
    }

    /**
     * Tests that an {@link IllegalArgumentException} (representing a bad request) is correctly handled.
     * Verifies that the response contains HTTP 400 status and the expected error message.
     */
    @Test
    void testHandleBadRequest() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid input provided");

        ResponseEntity<String> response = globalExceptionHandler.handleBadRequest(exception);

        assertNotNull(response);
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad Request: Invalid input provided", response.getBody());
    }

    /**
     * Tests that an {@link EntityNotFoundException} (representing a not found error) is correctly handled.
     * Verifies that the response contains HTTP 404 status and the expected error message.
     */
    @Test
    void testHandleNotFound() {
        EntityNotFoundException exception = new EntityNotFoundException("Requested entity not found");

        ResponseEntity<String> response = globalExceptionHandler.handleNotFound(exception);

        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found: Requested entity not found", response.getBody());
    }
}

