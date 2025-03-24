/**
 * Created by Michael Avoyan on 01/03/2025.
 */

package com.michaelavoyan.imageservice.repositories;

import com.michaelavoyan.imageservice.entities.Slideshow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository interface for managing Slideshow entities.
 * This interface extends {@link JpaRepository} to provide CRUD operations on the Slideshow entity.
 * It also includes a custom query method to find slideshows containing a specific Image.
 */
public interface SlideshowRepository extends JpaRepository<Slideshow, Long> {

    /**
     * Finds all slideshows that contain the specified image.
     *
     * @param imageId The ID of the image to search for.
     * @return A list of slideshows containing the given image.
     */
    @Query("SELECT s FROM Slideshow s JOIN s.images i WHERE i.id = :imageId")
    List<Slideshow> findSlideshowsContainingImage(@Param("imageId") Long imageId);
}
