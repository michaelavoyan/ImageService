/**
 * Created by Michael Avoyan on 01/03/2025.
 */

package com.michaelavoyan.imageservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This is a test class for the {@link ImageServiceApplication}.
 * It ensures that the Spring Boot application context loads successfully.
 */
@SpringBootTest
@ActiveProfiles("test") // Forces the test environment to use application-test.properties
class ImageServiceApplicationTest {
    /**
     * Test method to verify that the Spring application context loads successfully.
     */
    @Test
    void contextLoads() {
        assertThat(true).isTrue();
    }
}
