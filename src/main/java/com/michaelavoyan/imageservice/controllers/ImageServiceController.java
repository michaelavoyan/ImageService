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
import jakarta.validation.Valid;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Controller class for managing images, slideshows, and proof-of-play records.
 * Provides RESTful endpoints for handling image-related CRUD operations.
 */
@RestController
@RequestMapping("/api")
public class ImageServiceController {

    private final ImageRepository imageRepository;
    private final SlideshowRepository slideshowRepository;
    private final ProofOfPlayRepository proofOfPlayRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ImageVerifier imageVerifier;

    /**
     * Constructor to initialize repositories and services.
     */
    public ImageServiceController(
            ImageRepository imageRepository,
            SlideshowRepository slideshowRepository,
            ProofOfPlayRepository proofOfPlayRepository,
            ApplicationEventPublisher eventPublisher,
            ImageVerifier imageVerifier
    ) {
        this.imageRepository = imageRepository;
        this.slideshowRepository = slideshowRepository;
        this.proofOfPlayRepository = proofOfPlayRepository;
        this.eventPublisher = eventPublisher;
        this.imageVerifier = imageVerifier;
    }

    /**
     * Adds a new image if the URL is valid.
     *
     * @param image The image entity containing the URL.
     * @return A response entity containing the saved image or an error message.
     */
    @PostMapping("/addImage")
    public CompletableFuture<ResponseEntity<?>> addImage(@Valid @RequestBody Image image) {
        return imageVerifier.isValidImageUrl(
                imageVerifier.createConnection(image.getUrl())
        ).thenApply(isValid -> {
            if (!isValid) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid image URL. The URL does not contain a valid image.");
            }
            Image savedImage = imageRepository.save(image);
            eventPublisher.publishEvent("Image added: " + savedImage.getId());
            return ResponseEntity.ok(savedImage);
        });
    }

    /**
     * Deletes an image by ID.
     *
     * @param id The ID of the image to delete.
     * @return A response entity indicating success or failure.
     */
    @DeleteMapping("/deleteImage/{id}")
    public ResponseEntity<?> deleteImage(@Valid @PathVariable Long id) {
        if (!imageRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found.");
        }
        imageRepository.deleteById(id);
        eventPublisher.publishEvent("Image deleted: " + id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Adds a new slideshow along with its images in a transactional operation.
     *
     * @param slideshow The slideshow entity containing images.
     * @return A response entity with the saved slideshow or an error message.
     */
    @PostMapping("/addSlideshow")
    @Transactional // Ensures the slideshow and its images are saved in one atomic database transaction.
    public ResponseEntity<?> addSlideshow(@Valid @RequestBody Slideshow slideshow) {
        if (slideshow.getImages() == null || slideshow.getImages().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Slideshow must contain images.");
        }

        // Temporarily store images and remove them from slideshow to prevent early persistence
        List<Image> images = slideshow.getImages();
        slideshow.setImages(null);

        // Save the Slideshow first, ensuring ID is generated
        Slideshow savedSlideshow = slideshowRepository.saveAndFlush(slideshow);

        // Assign the saved Slideshow to each Image
        for (Image image : images) {
            image.setSlideshow(savedSlideshow);
        }

        // Save images separately after slideshow ID is assigned
        List<Image> savedImages = imageRepository.saveAll(images);

        // Restore images to slideshow so it is returned in the response
        savedSlideshow.setImages(savedImages);

        // Publish event and return response
        eventPublisher.publishEvent("Slideshow added: " + savedSlideshow.getId());
        return ResponseEntity.ok(savedSlideshow);
    }

    /**
     * Deletes a slideshow by ID.
     *
     * @param id The ID of the slideshow to delete.
     * @return A response entity indicating success or failure.
     */
    @DeleteMapping("/deleteSlideshow/{id}")
    public ResponseEntity<?> deleteSlideshow(@Valid @PathVariable Long id) {
        if (!slideshowRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Slideshow not found.");
        }
        slideshowRepository.deleteById(id);
        eventPublisher.publishEvent("Slideshow deleted: " + id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Searches images based on a query string and optional duration filter.
     *
     * @param query The search query string.
     * @param duration The optional duration filter.
     * @return A list of matching images.
     */
    @Transactional // This ensures that Hibernate's session remains open during execution.
    @GetMapping("/images/search")
    public ResponseEntity<List<Image>> searchImages(
            @RequestParam String query,
            @RequestParam(required = false) Integer duration
    ) {
        List<Image> images = imageRepository.searchImages(query, (duration != null) ? duration : 0);

        // Explicitly load images from each slideshow to prevent LazyInitializationException
        images.forEach(image -> {
            if (image.getSlideshow() != null) {
                image.getSlideshow().getImages().size(); // Force Hibernate to initialize images
            }
        });

        return ResponseEntity.ok(images);
    }

    /**
     * Retrieves images for a given slideshow ID.
     *
     * @param id The slideshow ID.
     * @return A response entity containing the list of images.
     */
    @GetMapping("/slideShow/{id}/slideshowOrder")
    public ResponseEntity<?> getSlideshowImages(@Valid @PathVariable Long id) {
        return slideshowRepository.findById(id)
                .map(slideshow -> ResponseEntity.ok(slideshow.getImages()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of()));
    }

    /**
     * Records proof of play for a slideshow and image.
     *
     * @param id The slideshow ID.
     * @param imageId The image ID.
     * @return A response entity indicating success or failure.
     */
    @PostMapping("/slideShow/{id}/proof-of-play/{imageId}")
    public ResponseEntity<?> addProofOfPlay(
            @Valid @PathVariable Long id,
            @Valid @PathVariable Long imageId
    ) {
        Slideshow slideshow = slideshowRepository.findById(id).orElse(null);
        if (slideshow == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Slideshow with ID " + id + " not found.");
        }

        Image image = imageRepository.findById(imageId).orElse(null);
        if (image == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Image with ID " + imageId + " not found.");
        }

        if (!image.getSlideshow().getId().equals(slideshow.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Image ID " + imageId + " is not part of Slideshow ID " + id);
        }

        ProofOfPlay pop = new ProofOfPlay();
        pop.setSlideshow(slideshow);
        pop.setImage(image);
        proofOfPlayRepository.save(pop);

        eventPublisher.publishEvent("Proof of Play recorded: Slideshow ID " + id + ", Image ID " + imageId);
        return ResponseEntity.ok(pop);
    }
}
