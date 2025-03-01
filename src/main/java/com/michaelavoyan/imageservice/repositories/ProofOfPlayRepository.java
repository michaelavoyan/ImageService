/**
 * Created by Michael Avoyan on 01/03/2025.
 */

package com.michaelavoyan.imageservice.repositories;

import com.michaelavoyan.imageservice.entities.ProofOfPlay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing ProofOfPlay entities.
 * This interface extends {@link JpaRepository} to provide basic CRUD operations and
 * additional query methods for fetching ProofOfPlay records based on slideshow ID and image ID.
 */
public interface ProofOfPlayRepository extends JpaRepository<ProofOfPlay, Long> {
    /**
     * Finds a list of ProofOfPlay records associated with the given slideshow ID.
     *
     * @param slideshowId the ID of the slideshow
     * @return a list of ProofOfPlay entities related to the specified slideshow ID
     */
    List<ProofOfPlay> findBySlideshowId(Long slideshowId);

    /**
     * Finds a list of ProofOfPlay records associated with the given image ID.
     *
     * @param imageId the ID of the image
     * @return a list of ProofOfPlay entities related to the specified image ID
     */
    List<ProofOfPlay> findByImageId(Long imageId);
}
