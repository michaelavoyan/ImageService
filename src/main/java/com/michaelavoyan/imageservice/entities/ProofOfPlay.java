/**
 * Created by Michael Avoyan on 01/03/2025.
 */

package com.michaelavoyan.imageservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entity representing a record of an image being played in a slideshow.
 * This entity is stored in the database with a unique constraint
 * ensuring that the combination of slideshow and image is unique.
 */
@Setter
@Getter
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"slideshow_id", "image_id"})})
public class ProofOfPlay {
    /**
     * Unique identifier for this proof of play entry.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The slideshow in which the image was played.
     */
    @ManyToOne
    @JoinColumn(name = "slideshow_id", nullable = false)
    private Slideshow slideshow;

    /**
     * The image that was played.
     */
    @ManyToOne
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    /**
     * The timestamp when the image was played.
     * It is automatically set before persistence.
     */
    private LocalDateTime playedAt;

    @PrePersist
    public void prePersist() {
        playedAt = LocalDateTime.now();
    }
}
