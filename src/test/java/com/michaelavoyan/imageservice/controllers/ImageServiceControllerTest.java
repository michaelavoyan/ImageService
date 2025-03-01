/**
 * Created by Michael Avoyan on 01/03/2025.
 */

package com.michaelavoyan.imageservice.controllers;

import com.michaelavoyan.imageservice.entities.Image;
import com.michaelavoyan.imageservice.entities.ProofOfPlay;
import com.michaelavoyan.imageservice.entities.Slideshow;
import com.michaelavoyan.imageservice.repositories.ImageRepository;
import com.michaelavoyan.imageservice.repositories.ProofOfPlayRepository;
import com.michaelavoyan.imageservice.repositories.SlideshowRepository;
import com.michaelavoyan.imageservice.utils.ImageVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ImageServiceController}.
 * Ensures that all controller endpoints work correctly with mocked dependencies.
 */
@RestController
@RequestMapping("/api")
class ImageServiceControllerTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private SlideshowRepository slideshowRepository;

    @Mock
    private ProofOfPlayRepository proofOfPlayRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private ImageVerifier imageVerifier;

    private ImageServiceController controller;

    private Image image;
    private Slideshow slideshow;

    /**
     * Initializes mocks and test data before each test case.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new ImageServiceController(
                imageRepository,
                slideshowRepository,
                proofOfPlayRepository,
                eventPublisher,
                imageVerifier
        );

        image = new Image();
        image.setId(1L);
        image.setUrl("https://example.com/image.jpg");

        slideshow = new Slideshow();
        slideshow.setId(1L);
        slideshow.setImages(List.of(image));

        when(imageVerifier.createConnection(anyString())).thenReturn(null);
        when(imageVerifier.isValidImageUrl(any())).thenReturn(CompletableFuture.completedFuture(true));
    }
    /**
     * Tests successful image addition.
     */
    @Test
    void testAddImage_Success() throws Exception {
        when(imageRepository.save(any(Image.class))).thenReturn(image);
        when(imageVerifier.isValidImageUrl(any())).thenReturn(CompletableFuture.completedFuture(true));

        CompletableFuture<ResponseEntity<?>> response = controller.addImage(image);

        assertDoesNotThrow(() -> {
            ResponseEntity<?> entity = response.get();
            assertEquals(200, entity.getStatusCode().value());
        });
    }

    /**
     * Tests successful image deletion.
     */
    @Test
    void testDeleteImage_Success() {
        when(imageRepository.existsById(1L)).thenReturn(true);
        doNothing().when(imageRepository).deleteById(1L);

        ResponseEntity<?> response = controller.deleteImage(1L);
        assertEquals(204, response.getStatusCode().value());
    }

    /**
     * Tests deleting a non-existing image.
     */
    @Test
    void testDeleteImage_NotFound() {
        when(imageRepository.existsById(1L)).thenReturn(false);

        ResponseEntity<?> response = controller.deleteImage(1L);
        assertEquals(404, response.getStatusCode().value());
    }

    /**
     * Tests successful slideshow addition.
     */
    @Test
    void testAddSlideshow_Success() {
        when(slideshowRepository.saveAndFlush(any(Slideshow.class))).thenReturn(slideshow);
        if (slideshow.getImages() != null) {
            for (Image image : slideshow.getImages()) {
                image.setSlideshow(slideshow); // Manually assign slideshow to images
            }
        }
        when(imageRepository.saveAll(anyList())).thenReturn(slideshow.getImages());

        ResponseEntity<?> response = controller.addSlideshow(slideshow);

        assertEquals(200, response.getStatusCode().value());

        verify(slideshowRepository, times(1)).saveAndFlush(any(Slideshow.class));
        verify(imageRepository, times(1)).saveAll(anyList());
    }

    /**
     * Tests adding an empty slideshow.
     */
    @Test
    void testAddSlideshow_BadRequest() {
        Slideshow emptySlideshow = new Slideshow();
        ResponseEntity<?> response = controller.addSlideshow(emptySlideshow);
        assertEquals(400, response.getStatusCode().value());
    }

    /**
     * Tests successful slideshow deletion.
     */
    @Test
    void testDeleteSlideshow_Success() {
        when(slideshowRepository.existsById(1L)).thenReturn(true);
        doNothing().when(slideshowRepository).deleteById(1L);

        ResponseEntity<?> response = controller.deleteSlideshow(1L);
        assertEquals(204, response.getStatusCode().value());
    }

    /**
     * Tests deleting a non-existing slideshow.
     */
    @Test
    void testDeleteSlideshow_NotFound() {
        when(slideshowRepository.existsById(1L)).thenReturn(false);

        ResponseEntity<?> response = controller.deleteSlideshow(1L);
        assertEquals(404, response.getStatusCode().value());
    }

    /**
     * Tests search for images.
     */
    @Test
    void testSearchImages() {
        List<Image> images = List.of(image);

        // Use nullable(Integer.class) instead of eq(null)
        when(imageRepository.searchImages(eq("test"), nullable(Integer.class))).thenReturn(images);

        ResponseEntity<List<Image>> response = controller.searchImages("test", null);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    /**
     * Tests getting images of an existing slideshow.
     */
    @Test
    void testGetSlideshowImages_Success() {
        when(slideshowRepository.findById(1L)).thenReturn(Optional.of(slideshow));

        ResponseEntity<?> response = controller.getSlideshowImages(1L);
        assertEquals(200, response.getStatusCode().value());
    }

    /**
     * Tests getting images of a non-existing slideshow.
     */
    @Test
    void testGetSlideshowImages_NotFound() {
        when(slideshowRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.getSlideshowImages(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(List.of(), response.getBody());

        verify(slideshowRepository, times(1)).findById(1L);
    }

    /**
     * Tests adding a ProofOfPlay successfully.
     */
    @Test
    void testAddProofOfPlay_Success() {
        image.setSlideshow(slideshow);

        ProofOfPlay proofOfPlay = new ProofOfPlay();
        proofOfPlay.setSlideshow(slideshow);
        proofOfPlay.setImage(image);

        when(slideshowRepository.findById(1L)).thenReturn(Optional.of(slideshow));
        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));
        when(proofOfPlayRepository.save(any(ProofOfPlay.class))).thenReturn(proofOfPlay);

        ResponseEntity<?> response = controller.addProofOfPlay(1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof ProofOfPlay);

        ProofOfPlay returnedProofOfPlay = (ProofOfPlay) response.getBody();
        assertEquals(slideshow, returnedProofOfPlay.getSlideshow());
        assertEquals(image, returnedProofOfPlay.getImage());
    }

    /**
     * Tests adding a ProofOfPlay to a non-existing slideshow.
     */
    @Test
    void testAddProofOfPlay_SlideshowNotFound() {
        when(slideshowRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.addProofOfPlay(1L, 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Slideshow with ID 1 not found.", response.getBody());
    }

    /**
     * Tests adding a ProofOfPlay to a non-existing images.
     */
    @Test
    void testAddProofOfPlay_ImageNotFound() {
        when(slideshowRepository.findById(1L)).thenReturn(Optional.of(slideshow));
        when(imageRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.addProofOfPlay(1L, 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Image with ID 1 not found.", response.getBody());
    }
}
