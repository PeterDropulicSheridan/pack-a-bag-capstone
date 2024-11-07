package ca.sheridancollege.packofbag.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.sheridancollege.packofbag.model.Images;

public interface ImageRepository extends JpaRepository<Images, Long> {
    List<Images> findByEventName(String eventName); // Find images by event name
    List<Images> findByEventNameIsNull(); // Find images with no event
}
