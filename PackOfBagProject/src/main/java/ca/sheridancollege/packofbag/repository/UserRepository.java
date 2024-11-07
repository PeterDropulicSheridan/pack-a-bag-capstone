package ca.sheridancollege.packofbag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.packofbag.model.User;

@Repository
public interface UserRepository extends JpaRepository <User, Long>{

	User findByUsername(String username);
	
	// Add this method to find a user by email
	User findByEmail(String email);
	
	
}
