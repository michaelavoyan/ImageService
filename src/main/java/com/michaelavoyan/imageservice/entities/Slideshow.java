/**
 * Created by Michael Avoyan on 01/03/2025.
 */

package com.michaelavoyan.imageservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents a Slideshow entity that contains a list of images.
 * This entity is mapped to a database table and uses JPA annotations for persistence.
 * Each slideshow can contain multiple images, which are managed with a one-to-many relationship.
 */
@Setter
@Getter
@Entity
public class Slideshow {
    /**
     * Unique identifier for the Slideshow entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * List of images associated with this slideshow.
     * The cascade type is set to {@code PERSIST} to ensure that images are only persisted
     * after the Slideshow entity itself has an ID.
     */
    @OneToMany(mappedBy = "slideshow", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Image> images;

}
