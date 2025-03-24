/**
 * Created by Michael Avoyan on 01/03/2025.
 */

package com.michaelavoyan.imageservice.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageVerifierTest {

    private ImageVerifier imageVerifier;

    @Mock
    private HttpURLConnection mockConnection;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imageVerifier = new ImageVerifier();
    }

    @Test
    void testCreateConnection_Success() throws Exception {
        String testUrl = "https://example.com/image.jpg";
        HttpURLConnection connection = imageVerifier.createConnection(testUrl);
        assertNotNull(connection);
    }

    @Test
    void testIsValidImageUrl_ValidImage() throws Exception {
        when(mockConnection.getHeaderField(HttpHeaders.CONTENT_TYPE)).thenReturn("image/jpeg");

        // Load a real image from resources
        InputStream imageStream = getClass().getResourceAsStream("/js-collections.jpeg");
        assertNotNull(imageStream, "Test image not found!");

        when(mockConnection.getInputStream()).thenReturn(imageStream);
        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

        CompletableFuture<Boolean> result = imageVerifier.isValidImageUrl(mockConnection);
        assertTrue(result.get(), "Expected a valid image but got false");
    }


    @Test
    void testIsValidImageUrl_InvalidContentType() throws Exception {
        when(mockConnection.getHeaderField(HttpHeaders.CONTENT_TYPE)).thenReturn("text/html");
        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

        CompletableFuture<Boolean> result = imageVerifier.isValidImageUrl(mockConnection);
        assertFalse(result.get());
    }

    @Test
    void testIsValidImageUrl_InvalidImage() throws Exception {
        when(mockConnection.getHeaderField(HttpHeaders.CONTENT_TYPE)).thenReturn("image/jpeg");
        when(mockConnection.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

        CompletableFuture<Boolean> result = imageVerifier.isValidImageUrl(mockConnection);
        assertFalse(result.get());
    }

    @Test
    void testIsValidImageUrl_ConnectionError() throws Exception {
        when(mockConnection.getHeaderField(HttpHeaders.CONTENT_TYPE)).thenReturn("image/jpeg");
        when(mockConnection.getInputStream()).thenThrow(new IOException("Failed to read input stream"));

        CompletableFuture<Boolean> result = imageVerifier.isValidImageUrl(mockConnection);
        assertFalse(result.get());
    }
}
