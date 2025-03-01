/**
 * Created by Michael Avoyan on 01/03/2025.
 */

package com.michaelavoyan.imageservice.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Utility class for verifying image URLs by checking their content type and attempting to read them.
 * This class provides methods to establish an HTTP connection to an image URL
 * and verify whether the image is valid by checking its content type and readability.
 */
@Component
public class ImageVerifier {
    /**
     * List of allowed image MIME types.
     */
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/webp", "image/gif"
    );

    /**
     * Creates an HTTP connection to the given URL.
     *
     * @param urlStr the URL string to connect to
     * @return a {@link HttpURLConnection} instance
     * @throws RuntimeException if an error occurs while opening the connection
     */
    public HttpURLConnection createConnection(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET"); // Use GET instead of HEAD
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setDoInput(true);
            return connection;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create connection: " + e.getMessage(), e);
        }
    }

    /**
     * Asynchronously checks if a given image URL is valid.
     * The method verifies the content type of the response and attempts to read the image.
     * If the content type is not allowed or the image cannot be read, the method returns {@code false}.
     *
     * @param connection the {@link HttpURLConnection} to the image URL
     * @return a {@link CompletableFuture} containing {@code true} if the image is valid, otherwise {@code false}
     */
    public CompletableFuture<Boolean> isValidImageUrl(
            HttpURLConnection connection
    ) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                connection.connect();

                String contentType = connection.getHeaderField(HttpHeaders.CONTENT_TYPE);

                if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
                    System.out.println("Invalid Content-Type: " + contentType);
                    return false;
                }

                try (InputStream inputStream = connection.getInputStream()) {
                    BufferedImage image = ImageIO.read(inputStream);
                    boolean isValid = image != null;
                    System.out.println("Image read status: " + isValid);
                    return isValid;
                }
            } catch (IOException e) {
                System.out.println("Exception in isValidImageUrl: " + e.getMessage());
                return false;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }
}
