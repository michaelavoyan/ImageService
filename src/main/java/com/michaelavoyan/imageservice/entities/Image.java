/**
 * Created by Michael Avoyan on 01/03/2025.
 */

package com.michaelavoyan.imageservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Represents an image entity in the image service.
 * This entity is mapped to a database table and includes attributes such as
 * a unique URL, duration, creation timestamp, and an association with a slideshow.
 */
@Setter
@Getter
@Entity
public class Image {
    /**
     * The unique identifier for the image.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The unique URL of the image. Cannot be null.
     */
    @NotNull
    @Column(nullable = false, unique = true)
    private String url;

    /**
     * The duration for which the image is displayed.
     * Must be at least 1 second.
     */
    @Min(1)
    private int duration;

    /**
     * The timestamp when the image was created.
     */
    private LocalDateTime createdAt;

    /**
     * The slideshow to which this image belongs.
     */
    @ManyToOne
    @JoinColumn(name = "slideshow_id") // This allows Hibernate to persist Image after Slideshow is saved.
    private Slideshow slideshow;

    /**
     * Automatically sets the creation timestamp before persisting the entity.
     */
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
