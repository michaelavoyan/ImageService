/**
 * Created by Michael Avoyan on 01/03/2025.
 */

package com.michaelavoyan.imageservice.repositories;

import com.michaelavoyan.imageservice.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository interface for managing Image entities.
 * This interface extends {@link JpaRepository} to provide CRUD operations for the Image entity.
 * It includes a custom query method to search for images by URL and duration.
 */
public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query("SELECT i FROM Image i WHERE i.url LIKE %:query% AND (:duration = 0 OR i.duration = :duration)")
    List<Image> searchImages(@Param("query") String query, @Param("duration") int duration);
}
