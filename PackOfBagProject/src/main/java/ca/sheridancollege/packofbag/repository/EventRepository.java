package ca.sheridancollege.packofbag.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.sheridancollege.packofbag.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
	
	

}
