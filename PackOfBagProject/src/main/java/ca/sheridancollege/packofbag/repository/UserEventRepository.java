package ca.sheridancollege.packofbag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import ca.sheridancollege.packofbag.model.UserEvent;
import ca.sheridancollege.packofbag.model.Event;

public interface UserEventRepository extends JpaRepository<UserEvent, Long> {
    List<UserEvent> findByEvent(Event event); // Method to find UserEvents by Event
}

